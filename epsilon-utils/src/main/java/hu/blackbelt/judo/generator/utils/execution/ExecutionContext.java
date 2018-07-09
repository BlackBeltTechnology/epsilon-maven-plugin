package hu.blackbelt.judo.generator.utils.execution;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import hu.blackbelt.judo.generator.utils.AbbreviateUtils;
import hu.blackbelt.judo.generator.utils.MD5Utils;
import hu.blackbelt.judo.generator.utils.UUIDUtils;
import hu.blackbelt.judo.generator.utils.execution.contexts.EglExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.EolExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.ProgramParameter;
import hu.blackbelt.judo.generator.utils.execution.exception.ScriptExecutionException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.emc.emf.tools.EmfTool;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.ModelRepository;
import org.eclipse.epsilon.profiling.Profiler;
import org.eclipse.epsilon.profiling.ProfilerTargetSummary;
import org.eclipse.epsilon.profiling.ProfilingExecutionListener;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ExecutionContext implements AutoCloseable {

    @Builder.Default
    private Map<ModelContext, IModel> modelContextMap = Maps.newConcurrentMap();

    @Builder.Default
    private Map<Object, Object> context = new HashMap();

    @Builder.Default
    private ResourceSet resourceSet = EmfUtils.initResourceSet();

    @Builder.Default
    private ModelRepository projectModelRepository = new ModelRepository();

    @Builder.Default
    private Boolean rollback = true;

    private Log log;
    private List<String> metaModels;

    private List<ModelContext> modelContexts;

    private ArtifactResolver artifactResolver;
    private File sourceDirectory;
    private Boolean profile;


    @SneakyThrows
    public void init() {

        // Check if global package registry contains the EcorePackage
        if (EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI) == null) {
            EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        }

        addMetaModels();
        addModels();

        log.info("URL converters: \n\t" + URIConverter.URI_MAP.entrySet().stream()
                .map(e -> e.getKey() + "->" + e.getValue()).collect(Collectors.joining("\n\t")));

    }


    public void rollback() {
        for (IModel model : projectModelRepository.getModels()) {
            model.setStoredOnDisposal(false);
        }
    }

    public void commit() {
        rollback = false;
    }

    public void disposeRepository() {
        if (projectModelRepository != null) {
            projectModelRepository.dispose();
        }
    }

    @SneakyThrows(Exception.class)
    public void executeProgram(EolExecutionContext eolProgram) {
        /*
        URI source = null;
        if (eolProgram.getArtifact() != null) {
            source = new URI("jar:" + getArtifact(eolProgram.getArtifact()).toURI().toString() + "!/"
                    + eolProgram.getSource());
        } else {
            source = new File(sourceDirectory, eolProgram.source).toURI();
        }
        */
        // URI source = null;
        URI source = new File(sourceDirectory, eolProgram.getSource()).toURI();
        context.put(EglExecutionContext.ARTIFACT_ROOT, source);

        IEolExecutableModule eolModule = eolProgram.getModule(context);

        // Determinate any mode have alias or not
        boolean isAliasExists = false;
        for (ModelContext model : modelContextMap.keySet()) {
            if (model.getAliases() != null) {
                isAliasExists = true;
            }
        }

        if (isAliasExists) {
            ModelRepository repository = eolModule.getContext().getModelRepository();

            for (ModelContext model : modelContextMap.keySet()) {
                model.addAliases(repository, EmfUtils.createModelReference(modelContextMap.get(model)));
            }

        } else {
            eolModule.getContext().setModelRepository(projectModelRepository);
        }

        List<ProgramParameter> params = eolProgram.getParameters();
        if (params == null) {
            params = Lists.newArrayList();
        }

        log.info("Running program: " + source);

        executeModule(eolModule, source,
                params.stream().map(p -> Variable.createReadOnlyVariable(p.getName(), p.getValue()))
                        .collect(Collectors.toList()));

        eolProgram.post(context);

        if (!eolProgram.isOk()) {
            throw new ScriptExecutionException("Program aborted: " + eolProgram.toString());
        } else {
            log.info("Execution result: " + eolProgram.toString());
        }
    }

    @SneakyThrows
    private void executeModule(IEolExecutableModule eolModule, URI source, List<Variable> parameters) {
        eolModule.parse(source);
        if (profile) {
            Profiler.INSTANCE.reset();
        }
        try {
            if (profile) {
                Profiler.INSTANCE.start(source.toString(), "", eolModule);
            }

            if (eolModule.getParseProblems().size() > 0) {
                log.error("Parse errors occured...");
                for (ParseProblem problem : eolModule.getParseProblems()) {
                    log.error(problem.toString());
                }
                throw new MojoExecutionException("Parse error");
            }
            // Adding static utils
            eolModule.getContext().getFrameStack().put(Variable.createReadOnlyVariable("UUIDUtils", new UUIDUtils()));
            eolModule.getContext().getFrameStack().put(Variable.createReadOnlyVariable("MD5Utils", new MD5Utils()));
            eolModule.getContext().getFrameStack()
                    .put(Variable.createReadOnlyVariable("AbbreviateUtils", new AbbreviateUtils()));
            eolModule.getContext().getFrameStack().put(Variable.createReadOnlyVariable("EMFTool", new EmfTool()));

            for (Variable parameter : parameters) {
                eolModule.getContext().getFrameStack().put(parameter);
            }
            eolModule.getContext().getFrameStack().put(Variable.createReadOnlyVariable("executionContext", this));

            if (profile) {
                eolModule.getContext().getExecutorFactory().addExecutionListener(new ProfilingExecutionListener());
            }

            Object result = eolModule.execute();
            // getLog().info("EolExecutionContext executeAll result: " + result.toString());
        } finally {
            if (profile) {
                Profiler.INSTANCE.stop(source.toString());
                for (ProfilerTargetSummary p : Profiler.INSTANCE.getTargetSummaries()) {
                    log.info(String.format("Index: %d Name: %s: Count: %d Individual Time: %d Aggregate time: %d",
                            p.getIndex(), p.getName(), p.getExecutionCount(), p.getExecutionTime().getIndividual(),
                            p.getExecutionTime().getAggregate()));
                }
            }

        }
    }

    @SneakyThrows
    private void addMetaModels() {
        for (String metaModel : metaModels) {
            addMetaModel(metaModel);
        }
    }

    @SneakyThrows
    public void addMetaModel(String metaModel) {
        log.info("Registering ecore: " + metaModel);
        org.eclipse.emf.common.util.URI uri = artifactResolver.getArtifactAsEclipseURI(metaModel);
        log.info("    Meta model: " + uri);
        List<EPackage> ePackages = EmfUtils.register(resourceSet, uri, true);
        log.info("    EPackages: " + ePackages.stream().map(e -> e.getNsURI()).collect(Collectors.joining(", ")));
    }


    @SneakyThrows
    private void addModels() {
        for (ModelContext modelContext : modelContexts) {
            addModel(modelContext);
        }
    }

    @SneakyThrows
    public void addModel(ModelContext modelContext) {
        log.info("Model: " + modelContext.toString());
        org.eclipse.emf.common.util.URI artifactFile = artifactResolver.getArtifactAsEclipseURI(modelContext.getArtifact());
        log.info("    Artifact file: : " + artifactFile.toString());
        modelContextMap.put(modelContext, modelContext.load(log, resourceSet, projectModelRepository, artifactFile));
    }


    @Override
    public void close() throws Exception {
        if (rollback) {
            rollback();
        }
        disposeRepository();
    }
}

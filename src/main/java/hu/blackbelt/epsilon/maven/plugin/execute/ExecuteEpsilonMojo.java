package hu.blackbelt.epsilon.maven.plugin.execute;

import com.google.common.collect.Lists;
import hu.blackbelt.epsilon.maven.plugin.MavenURIHandler;
import hu.blackbelt.epsilon.runtime.execution.ExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.api.Log;
import hu.blackbelt.epsilon.runtime.execution.impl.NioFilesystemnRelativePathURIHandlerImpl;
import hu.blackbelt.epsilon.runtime.execution.impl.Slf4jLog;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.epsilon.emc.emf.CachedResourceSet;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.ExecutionContext.executionContextBuilder;

@Mojo(name = "execute", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ExecuteEpsilonMojo extends AbstractEpsilonMojo {

    @Parameter(name = "eolPrograms", readonly = true, required = true)
    public List<Eol> eolPrograms;

    @Parameter(name = "profile", readonly = true, required = false)
    public Boolean profile = false;

    @Parameter(name = "sourceDirectory", defaultValue = "${basedir}", readonly = true)
    public File sourceDirectory;

    @Parameter(name = "addUmlPackages", readonly = true, required = false)
    public Boolean addUmlPackages = false;

    synchronized public void execute() throws MojoExecutionException, MojoFailureException {
        List modelContexts = Lists.newArrayList();

        if (emfModels != null) {
            modelContexts.addAll(emfModels.stream().map(m -> m.toModelContext()).collect(Collectors.toList()));
        }
        if (xmlModels != null) {
            modelContexts.addAll(xmlModels.stream().map(m -> m.toModelContext()).collect(Collectors.toList()));
        }
        if (plainXmlModels != null) {
            modelContexts.addAll(plainXmlModels.stream().map(m -> m.toModelContext()).collect(Collectors.toList()));
        }
        if (excelModels != null) {
            modelContexts.addAll(excelModels.stream().map(m -> m.toModelContext()).collect(Collectors.toList()));
        }

        URIHandler uriHandler = MavenURIHandler.builder()
                .repoSystem(repoSystem)
                .repositories(repositories)
                .repoSession(repoSession).build();

        // Setup resourcehandler used to load metamodels
        ResourceSet  executionResourceSet = new CachedResourceSet();
        executionResourceSet.getURIConverter().getURIHandlers().add(0, uriHandler);

        // Default logger
        Log log = new Slf4jLog();


        try (ExecutionContext executionContext = executionContextBuilder()
                .addUmlPackages(addUmlPackages)
                .resourceSet(executionResourceSet)
                .metaModels(metaModels)
                .modelContexts(modelContexts)
                .profile(profile)
                .sourceDirectory(sourceDirectory)
                .log(log)
                .build()) {

            executionContext.load();
            eolPrograms.stream().forEach(p -> { executionContext.executeProgram(p.toExecutionContext()); });
            executionContext.commit();
        } catch (Exception e) {
            throw new MojoExecutionException("Execution error: " + e.toString(), e);
        }
    }
}


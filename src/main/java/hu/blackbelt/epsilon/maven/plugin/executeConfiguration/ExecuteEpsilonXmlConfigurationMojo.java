package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import com.google.common.collect.Lists;
import hu.blackbelt.epsilon.maven.plugin.MavenLog;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.*;
import hu.blackbelt.epsilon.runtime.execution.ExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.api.Log;
import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.components.io.fileselectors.IncludeExcludeFileSelector;
import org.codehaus.plexus.components.io.resources.PlexusIoFileResourceCollection;
import org.codehaus.plexus.components.io.resources.PlexusIoResource;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.ExecutionContext.executionContextBuilder;

@Mojo(name = "executeConfiguration", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ExecuteEpsilonXmlConfigurationMojo extends AbstractMojo {

    @Component
    public RepositorySystem repoSystem;

    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true, required = true)
    public RepositorySystemSession repoSession;

    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true, required = true)
    public List<RemoteRepository> repositories;

    /**
     * List of configuration artifacts. Item could be Maven artifacts (mvn:*) or ant filesets (supporting wildcards).
     */
    @Parameter(name = "configurationArtifacts", readonly = true, required = true)
    public String[] configurationArtifacts;

    @Parameter(name = "sourceDirectory", defaultValue = "${basedir}", required = true, readonly = true)
    public File sourceDirectory;

    public Log log = new MavenLog(getLog());

    static {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
    }

    synchronized public void execute() throws MojoExecutionException, MojoFailureException {
        final List<File> configurationArtifactFiles = new LinkedList<>();
        final List<String> includes = new LinkedList<>();

        for (final String configurationArtifact : configurationArtifacts) {
            if (configurationArtifact.startsWith("mvn:")) {
                configurationArtifactFiles.add(MavenArtifactResolver.builder()
                        .repoSession(repoSession)
                        .repositories(repositories)
                        .repoSystem(repoSystem)
                        .sourceDirectory(sourceDirectory)
                        .log(log)
                        .build()
                        .getArtifact(configurationArtifact));
            } else {
                includes.add(configurationArtifact);
            }
        }
        if (!includes.isEmpty()) {
            final PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
            if (sourceDirectory != null) {
                collection.setBaseDir(sourceDirectory);
            }
            final FileSelector[] selectors;
            final IncludeExcludeFileSelector fs = new IncludeExcludeFileSelector();
            final String[] inc = new String[includes.size()];
            fs.setIncludes(includes.toArray(inc));
            selectors = new FileSelector[]{fs};
            collection.setFileSelectors(selectors);
            final Iterator<PlexusIoResource> resources;
            try {
                resources = collection.getResources();
            } catch (IOException e) {
                throw new MojoFailureException("Failed to get list of XAR XML files", e);
            }
            while (resources.hasNext()) {
                PlexusIoResource resource = resources.next();
                if (resource.isFile()) {
                    configurationArtifactFiles.add(new File(collection.getBaseDir(), resource.getName()));
                }
            }
        }

        final Set<String> completed = new TreeSet<>();
        for (final File configurationArtifactFile : configurationArtifactFiles) {
            log.info("Executing configuration: " + configurationArtifactFile);

            if (!configurationArtifactFile.exists()) {
                throw new MojoFailureException("Configuration artifact file not found: " + configurationArtifactFile);
            }

            try {
                final JAXBContext jc = JAXBContext.newInstance(ConfigurationType.class.getPackage().getName(), getClass().getClassLoader());
                final Unmarshaller unmarshaller = jc.createUnmarshaller();
                final ConfigurationType configuration = ((JAXBElement<ConfigurationType>) unmarshaller.unmarshal(configurationArtifactFile)).getValue();

                List modelContexts = Lists.newArrayList();

                if (configuration.getModels() != null) {
                    modelContexts.addAll(configuration.getModels().getModel().stream().map(m -> new Model(m).toModelContext()).collect(Collectors.toList()));
                }
                if (configuration.getXmlModels() != null) {
                    modelContexts.addAll(configuration.getXmlModels().getXmlModel().stream().map(m -> new XmlModel(m).toModelContext()).collect(Collectors.toList()));
                }
                if (configuration.getPlainXmlModels() != null) {
                    modelContexts.addAll(configuration.getPlainXmlModels().getPlainXmlModel().stream().map(m -> new PlainXmlModel(m).toModelContext()).collect(Collectors.toList()));
                }
                if (configuration.getExcelModels() != null) {
                    modelContexts.addAll(configuration.getExcelModels().getExcelModel().stream().map(m -> new ExcelModel(m).toModelContext()).collect(Collectors.toList()));
                }

                /*
                                        .artifactResolver(MavenArtifactResolver.builder()
                                .repoSession(repoSession)
                                .repositories(repositories)
                                .repoSystem(repoSystem)
                                .log(log)
                                .build())

                 */
                try (ExecutionContext executionContext = executionContextBuilder()
                        .metaModels(configuration.getMetaModels().getMetaModel())
                        .modelContexts(modelContexts)
                        .profile(configuration.isProfile() != null ? configuration.isProfile() : false)
                        .sourceDirectory(sourceDirectory)
                        .log(log)
                        .build()) {

                    executionContext.load();
                    configuration.getEolPrograms().getEclOrEglOrEgx().forEach(prg -> {
                        final EolExecutionContext eolExecutionContext;
                        if (prg instanceof EclType) {
                            eolExecutionContext = Ecl.builder().ecl((EclType) prg).build().toExecutionContext();
                        } else if (prg instanceof EglType) {
                            eolExecutionContext = Egl.builder().egl((EglType) prg).build().toExecutionContext();
                        } else if (prg instanceof EgxType) {
                            eolExecutionContext = Egx.builder().egx((EgxType) prg).build().toExecutionContext();
                        } else if (prg instanceof EmlType) {
                            eolExecutionContext = Eml.builder().eml((EmlType) prg).build().toExecutionContext();
                        } else if (prg instanceof EtlType) {
                            eolExecutionContext = Etl.builder().etl((EtlType) prg).build().toExecutionContext();
                        } else if (prg instanceof EvlType) {
                            eolExecutionContext = Evl.builder().evl((EvlType) prg).build().toExecutionContext();
                        } else {
                            eolExecutionContext = Eol.builder().eol(prg).build().toExecutionContext();
                        }
                        executionContext.executeProgram(eolExecutionContext);
                    });
                    executionContext.commit();
                    completed.add(configurationArtifactFile.getAbsolutePath());
                }
            } catch (Exception ex) {
                log.debug("Completed configuration artifacts: \n" + String.join(",\n", completed));
                throw new MojoExecutionException("Execution error: " + ex.getMessage(), ex);
            }
        }
        log.info("Completed configuration artifacts: \n" + String.join(",\n", completed));
    }
}

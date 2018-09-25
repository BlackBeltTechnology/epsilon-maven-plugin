package hu.blackbelt.epsilon.maven.plugin.execute;

import com.google.common.collect.Lists;
import hu.blackbelt.epsilon.runtime.execution.ExecutionContext;
import hu.blackbelt.epsilon.maven.plugin.AbstractEpsilonMojo;
import hu.blackbelt.epsilon.maven.plugin.MavenArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Mojo(name = "execute", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ExecuteEpsilonMojo extends AbstractEpsilonMojo {

    @Parameter(name = "eolPrograms", readonly = true, required = true)
    public List<Eol> eolPrograms;

    @Parameter(name = "profile", readonly = true, required = false)
    public Boolean profile = false;

    @Parameter(name = "sourceDirectory", defaultValue = "${baseDir}", readonly = true)
    public File sourceDirectory;


    synchronized public void execute() throws MojoExecutionException, MojoFailureException {
        List modelContexts = Lists.newArrayList();

        if (models != null) {
            modelContexts.addAll(models.stream().map(m -> m.toModelContext()).collect(Collectors.toList()));
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


        try (ExecutionContext executionContext = ExecutionContext.builder()
                .metaModels(metaModels)
                .modelContexts(modelContexts)
                .artifactResolver(MavenArtifactResolver.builder()
                        .repoSession(repoSession)
                        .repositories(repositories)
                        .repoSystem(repoSystem)
                        .log(log)
                        .build())
                .profile(profile)
                .sourceDirectory(sourceDirectory)
                .log(log)
                .build()) {

            executionContext.init();
            eolPrograms.stream().forEach(p -> { executionContext.executeProgram(p.toExecutionContext()); });
            executionContext.commit();
        } catch (Exception e) {
            throw new MojoExecutionException("Execution error: " + e.toString(), e);
        }
    }
}

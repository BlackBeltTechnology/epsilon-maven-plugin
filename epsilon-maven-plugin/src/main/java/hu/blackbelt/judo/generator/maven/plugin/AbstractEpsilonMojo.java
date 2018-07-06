package hu.blackbelt.judo.generator.maven.plugin;

import hu.blackbelt.judo.generator.utils.execution.Log;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.util.List;

public abstract class AbstractEpsilonMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    public MavenProject project;

    @Component
    public RepositorySystem repoSystem;

    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true, required = true)
    public RepositorySystemSession repoSession;

    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true, required = true)
    public List<RemoteRepository> repositories;

    @Parameter(readonly = true)
    public List<String> metaModels;

    @Parameter(readonly = true)
    public List<Model> models;

    @Parameter(readonly = true)
    public List<PlainXmlModel> plainXmlModels;

    public Log log = new MavenLog(getLog());

    static {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
    }

    /*
    public void addMetaModels(ResourceSet resourceSet) throws Exception {
        if (metaModels != null) {
            for (String metaModel : metaModels) {
        		log.info("Registering ecore: " + metaModel);
        		URI uri = MavenArtifactResolver.builder().repoSession(repoSession).repositories(repositories).repoSystem(repoSystem).log(log).build()
                        .getArtifactAsEclipseURI(metaModel);
        		log.info("    Meta model: " + uri);
                List<EPackage> ePackages = EmfUtils.register(resourceSet, uri, true);
                log.info("    EPackages: " + ePackages.stream().map(e -> e.getNsURI()).collect(Collectors.joining(", ")));
            }
        }

    }

    public void addModels(ResourceSet resourceSet, ModelRepository modelRepository, Map<Model, EmfModel> emfModels) throws MojoExecutionException {
        if (models != null) {
            for (Model emf : models) {
                log.info("Model: " + emf.toString());
                URI artifactFile = MavenArtifactResolver.builder().repoSession(repoSession).repositories(repositories).repoSystem(repoSystem).log(log).build()
                        .getArtifactAsEclipseURI(emf.getArtifact());
                log.info("    Artifact file: : " + artifactFile.toString());
                emfModels.put(emf, loadEmf(log, resourceSet, modelRepository, emf, artifactFile));
            }
        }
    }

    public void addPlainXmlModels(ResourceSet resourceSet, ModelRepository modelRepository, Map<PlainXmlModel, PlainXmlModel> xmlModelMap) throws MojoExecutionException {
        if (plainXmlModels != null) {
            for (PlainXmlModel xml : plainXmlModels) {
                log.info("XML Model: " + xml.toString());
                URI artifactFile = MavenArtifactResolver.builder().repoSession(repoSession).repositories(repositories).repoSystem(repoSystem).log(log).build()
                        .getArtifactAsEclipseURI(xml.getArtifact());
                log.info("    Artifact file: : " + artifactFile.toString());

                xmlModelMap.put(xml, loadXml(log, resourceSet, modelRepository, xml, artifactFile));
            }
        }
    }
    */

}

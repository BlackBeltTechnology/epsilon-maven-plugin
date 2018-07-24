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

    @Parameter(readonly = true)
    public List<XmlModel> xmlModels;

    @Parameter(readonly = true)
    public List<ExcelModel> excelModels;

    public Log log = new MavenLog(getLog());

    static {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
    }
}

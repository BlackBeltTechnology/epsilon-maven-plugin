package hu.blackbelt.epsilon.maven.plugin;

import hu.blackbelt.epsilon.runtime.execution.impl.NioFilesystemnRelativePathURIHandlerImpl;
import lombok.Builder;
import lombok.SneakyThrows;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

@Builder
public class MavenURIHandler extends URIHandlerImpl {
    /**
     * Creates an instance.
     */
    private RepositorySystem repoSystem;
    private List<RemoteRepository> repositories;
    private RepositorySystemSession repoSession;
    private File sourceDirectory;

    @Builder.Default
    private FileSystem fileSystem = FileSystems.getDefault();

    @Builder.Default
    private URIHandler internalUriHandler =
            new NioFilesystemnRelativePathURIHandlerImpl("mvn-internal-fs", fileSystem, "");

    @Override
    public boolean canHandle(URI uri)
    {
        return uri.scheme().equals("mvn"); // && exists(uri, null);
    }


    @SneakyThrows(MojoExecutionException.class)
    private URI getArtifactFile(URI uri) {
        Artifact artifact = new DefaultArtifact(uri.opaquePart());
        ArtifactRequest req = new ArtifactRequest().setRepositories(this.repositories).setArtifact(artifact);
        ArtifactResult resolutionResult;
        try {
            resolutionResult = this.repoSystem.resolveArtifact(this.repoSession, req);

        } catch (ArtifactResolutionException e) {
            throw new MojoExecutionException("Artifact " + uri.opaquePart() + " could not be resolved.", e);
        }
        return URI.createFileURI(resolutionResult.getArtifact().getFile().getAbsolutePath());
    }


    private URIHandler getDelegatedURIHandler() {
        return internalUriHandler;
    }


    @Override
    public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
        return getDelegatedURIHandler().createOutputStream(getArtifactFile(uri), options);
    }

    @Override
    public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
        return getDelegatedURIHandler().createInputStream(getArtifactFile(uri), options);
    }

    @Override
    public void delete(URI uri, Map<?, ?> options) throws IOException {
        getDelegatedURIHandler().delete(getArtifactFile(uri), options);
    }

    @Override
    public boolean exists(URI uri, Map<?, ?> options) {
        return getDelegatedURIHandler().exists(getArtifactFile(uri), options);
    }

    @Override
    public Map<String, ?> getAttributes(URI uri, Map<?, ?> options) {
        return getDelegatedURIHandler().getAttributes(getArtifactFile(uri), options);
    }

    @Override
    public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException {
        getDelegatedURIHandler().setAttributes(getArtifactFile(uri), attributes, options);
    }
}

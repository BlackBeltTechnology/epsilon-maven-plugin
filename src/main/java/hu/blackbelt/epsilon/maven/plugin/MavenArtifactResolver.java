package hu.blackbelt.epsilon.maven.plugin;

import hu.blackbelt.epsilon.runtime.execution.ArtifactResolver;
import hu.blackbelt.epsilon.runtime.execution.Log;
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Builder
public class MavenArtifactResolver implements ArtifactResolver {

    private RepositorySystem repoSystem;
    private List<RemoteRepository> repositories;
    private RepositorySystemSession repoSession;
    private Log log;

    protected File getArtifact(String url) throws MojoExecutionException {
        if (url.startsWith("mvn:")) {
            Artifact artifact = new DefaultArtifact(url.toString().substring(4));
            ArtifactRequest req = new ArtifactRequest().setRepositories(this.repositories).setArtifact(artifact);
            ArtifactResult resolutionResult;
            try {
                resolutionResult = this.repoSystem.resolveArtifact(this.repoSession, req);

            } catch (ArtifactResolutionException e) {
                throw new MojoExecutionException("Artifact " + url.toString() + " could not be resolved.", e);
            }

            // The file should exists, but we never know.
            File file = resolutionResult.getArtifact().getFile();
            if (file == null || !file.exists()) {
                log.warn("Artifact " + url.toString() + " has no attached file. Its content will not be copied in the target model directory.");
            }
            return file;
        }
        throw new MojoExecutionException("Artifact " + url.toString() + " could not be resolved.");
    }

    @Override
    @SneakyThrows
    public URI getArtifactAsEclipseURI(String url) {
        if (url.startsWith("mvn:")) {
            return URI.createFileURI(getArtifact(url).getAbsolutePath());
        } else if (isValidURL(url)) {
            return URI.createURI(url);
        } else {
            return URI.createFileURI(new File(url).getAbsolutePath());
        }
    }

    private boolean isValidURL(String url) {

        URL u = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }

        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }


}

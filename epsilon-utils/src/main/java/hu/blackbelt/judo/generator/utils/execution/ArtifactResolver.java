package hu.blackbelt.judo.generator.utils.execution;

import org.eclipse.emf.common.util.URI;

public interface ArtifactResolver {
    URI getArtifactAsEclipseURI(String url);
}

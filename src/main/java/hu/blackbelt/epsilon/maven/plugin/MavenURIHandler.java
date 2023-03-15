package hu.blackbelt.epsilon.maven.plugin;

/*-
 * #%L
 * epsilon-maven-plugin
 * %%
 * Copyright (C) 2018 - 2023 BlackBelt Technology
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

import hu.blackbelt.epsilon.runtime.execution.impl.NioFilesystemnRelativePathURIHandlerImpl;
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
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

    @NonNull
    private RepositorySystem repoSystem;

    @NonNull
    private List<RemoteRepository> repositories;

    @NonNull
    private RepositorySystemSession repoSession;

    @Builder.Default
    private FileSystem fileSystem = FileSystems.getDefault();

    private URIHandler internalUriHandler;

    @Override
    public boolean canHandle(URI uri)
    {
        if (uri == null || uri.scheme() == null) {
            return false;
        }
        return uri.scheme().equals("mvn"); // && exists(uri, null);
    }


    @SneakyThrows(ArtifactResolutionException.class)
    public URI getArtifactFile(URI uri) {
        Artifact artifact = new DefaultArtifact(uri.opaquePart());
        ArtifactRequest req = new ArtifactRequest().setRepositories(this.repositories).setArtifact(artifact);
        ArtifactResult resolutionResult;
        resolutionResult = this.repoSystem.resolveArtifact(this.repoSession, req);
        return URI.createFileURI(resolutionResult.getArtifact().getFile().getAbsolutePath());
    }


    private URIHandler getDelegatedURIHandler() {
        if (internalUriHandler == null) {
            internalUriHandler = new NioFilesystemnRelativePathURIHandlerImpl("mvn-internal-fs", fileSystem, "");
        }
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

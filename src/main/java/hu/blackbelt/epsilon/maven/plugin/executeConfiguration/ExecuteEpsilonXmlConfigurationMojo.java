package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import hu.blackbelt.epsilon.maven.plugin.MavenLog;
import hu.blackbelt.epsilon.maven.plugin.MavenURIHandler;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.*;
import hu.blackbelt.epsilon.runtime.execution.ExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.api.Log;
import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.impl.CompositeURIHandlerImpl;
import hu.blackbelt.epsilon.runtime.execution.impl.NioFilesystemnRelativePathURIHandlerImpl;
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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.epsilon.emc.emf.CachedResourceSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.ExecutionContext.executionContextBuilder;
import static java.lang.Class.forName;

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


    URIHandler uriHandler;

    /*
    static {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
    } */

    synchronized public void execute() throws MojoExecutionException, MojoFailureException {

        uriHandler = CompositeURIHandlerImpl.builder().uriHandlerList(
                ImmutableList.of(
                        MavenURIHandler.builder()
                                .repoSession(repoSession)
                                .repositories(repositories)
                                .repoSystem(repoSystem)
                                .build(),
                        NioFilesystemnRelativePathURIHandlerImpl.builder().urlSchema("file").build(),
                        NioFilesystemnRelativePathURIHandlerImpl.builder().urlSchema("").build()
                )).build();


        final List<URI> configurationUris = new LinkedList<>();
        final List<String> wildcards = new LinkedList<>();

        for (final String configurationArtifact : configurationArtifacts) {
            URI uri = URI.createURI(configurationArtifact);
            if (uriHandler != null && uriHandler.canHandle(uri) && uriHandler.exists(uri, ImmutableMap.of())) {
                configurationUris.add(uri);
            } else {
                wildcards.add(configurationArtifact);
            }
        }

        if (!wildcards.isEmpty()) {
            final PlexusIoFileResourceCollection collection = new PlexusIoFileResourceCollection();
            if (sourceDirectory != null) {
                collection.setBaseDir(sourceDirectory);
            }
            final FileSelector[] selectors;
            final IncludeExcludeFileSelector fs = new IncludeExcludeFileSelector();
            final String[] inc = new String[wildcards.size()];
            fs.setIncludes(wildcards.toArray(inc));
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
                    configurationUris.add(URI.createFileURI(new File(collection.getBaseDir(), resource.getName()).getAbsolutePath()));
                }
            }
        }

        final Set<String> completed = new TreeSet<>();
        for (final URI uri : configurationUris) {

            // Setup resourcehandler used to load metamodels
            ResourceSet executionResourceSet = new CachedResourceSet();
            executionResourceSet.getURIConverter().getURIHandlers().add(0, uriHandler);


            log.info("================================================================================");
            log.info("Executing configuration: " + uri);

            if (!uriHandler.exists(uri, ImmutableMap.of())) {
                throw new MojoFailureException("Configuration file not found: " + uri);
            }

            try {
                final JAXBContext jc = JAXBContext.newInstance(ConfigurationType.class.getPackage().getName(), getClass().getClassLoader());
                final Unmarshaller unmarshaller = jc.createUnmarshaller();
                final ConfigurationType configuration = ((JAXBElement<ConfigurationType>) unmarshaller.unmarshal(uriHandler.createInputStream(uri, ImmutableMap.of()))).getValue();

                List modelContexts = Lists.newArrayList();

                if (configuration.getEmfModels() != null) {
                    modelContexts.addAll(configuration.getEmfModels().getEmfModel().stream().map(m -> new EmfModel(m).toModelContext()).collect(Collectors.toList()));
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

                Map<String, Object> injectedContextMap = Maps.newHashMap();
                if (configuration.getInjectedContexts() != null && configuration.getInjectedContexts().getInject() != null) {
                    for (InjectedContextType i : configuration.getInjectedContexts().getInject()) {
                        try {
                            injectedContextMap.put(i.getName(), forName(i.getClazz()).newInstance());
                        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                            throw new MojoFailureException("Coluld not inject context", e);
                        }
                    }
                }

                try (ExecutionContext executionContext = executionContextBuilder()
                        .resourceSet(executionResourceSet)
                        .metaModels(configuration.getMetaModels().getMetaModel())
                        .modelContexts(modelContexts)
                        .profile(configuration.isProfile() != null ? configuration.isProfile() : false)
                        .addUmlPackages(configuration.isAddUmlPackages() != null ? configuration.isAddUmlPackages() : false)
                        .addEcorePackages(configuration.isAddEcorePackages() != null ? configuration.isAddEcorePackages() : false)
                        .injectContexts(injectedContextMap)
                        .log(log)
                        .build()) {

                    executionContext.load();

                    for (EolType prg : configuration.getEolPrograms().getEclOrEglOrEgx()) {
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
                    }
                    executionContext.commit();
                    completed.add(uri.toString());
                }
            } catch (Exception ex) {
                log.debug("Completed configuration artifacts: \n" + String.join(",\n", completed));
                throw new MojoExecutionException("Execution error: " + ex.getMessage(), ex);
            }
        }
        log.info("Completed configuration artifacts: \n" + String.join(",\n", completed));
    }
}

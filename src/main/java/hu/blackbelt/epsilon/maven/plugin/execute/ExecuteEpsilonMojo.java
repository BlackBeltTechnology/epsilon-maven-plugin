package hu.blackbelt.epsilon.maven.plugin.execute;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import hu.blackbelt.epsilon.maven.plugin.MavenLog;
import hu.blackbelt.epsilon.maven.plugin.MavenURIHandler;
import hu.blackbelt.epsilon.runtime.execution.ExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.api.Log;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.epsilon.emc.emf.CachedResourceSet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.ExecutionContext.executionContextBuilder;
import static java.lang.Class.forName;

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

    @Parameter(name = "addEcorePackages", readonly = true, required = false)
    public Boolean addEcorePackages = false;

    @Parameter(name = "injectedContexts", readonly = true, required = false)
    public List<InjectedContext> injectedContexts;

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
        Log log = new MavenLog(getLog());

        Map<String, Object> injectedContextMap = Maps.newHashMap();
        if (injectedContexts != null) {
            for (InjectedContext i : injectedContexts) {
                try {
                    injectedContextMap.put(i.getName(), forName(i.getClazz()).newInstance());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    log.warn(e);
                }
            }
        }

        try (ExecutionContext executionContext = executionContextBuilder()
                .addUmlPackages(addUmlPackages)
                .addEcorePackages(addEcorePackages)
                .resourceSet(executionResourceSet)
                .metaModels(metaModels)
                .modelContexts(modelContexts)
                .profile(profile)
                .log(log)
                .injectContexts(injectedContextMap)
                .build()) {

            executionContext.load();
            for (Eol p : eolPrograms) {
                executionContext.executeProgram(p.toExecutionContext());
            }
            executionContext.commit();
        } catch (Exception e) {
            throw new MojoExecutionException("Execution error: " + e.toString(), e);
        }
    }
}


package hu.blackbelt.epsilon.maven.plugin.parsehutn;

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

import hu.blackbelt.epsilon.runtime.execution.ExecutionContext;
import hu.blackbelt.epsilon.maven.plugin.execute.AbstractEpsilonMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.models.ModelRepository;
import org.eclipse.epsilon.hutn.HutnContext;
import org.eclipse.epsilon.hutn.HutnModule;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(
    name = "parseHutn",
    defaultPhase = LifecyclePhase.GENERATE_RESOURCES
)
public class ParseHutnMojo extends AbstractEpsilonMojo {
    @Parameter(defaultValue = "", readonly = true, required = true)
    private File hutnFile;

    @Parameter(defaultValue = "", readonly = true, required = true)
    private File targetFile;

    public ParseHutnMojo() {
    }

    // This logic has been extracted so that it can be stubbed out in tests
    protected EmfModel createEmfModel() {
        return new EmfModel();
    }

    synchronized public void execute() throws MojoExecutionException, MojoFailureException {
        // EclipsePlatformStreamHandlerFactory.urlMapping.clear();

        /*
                        .artifactResolver(MavenArtifactResolver.builder()
                        .repoSession(repoSession)
                        .repositories(repositories)
                        .repoSystem(repoSystem)
                        .log(log)
                        .build())

         */
        try (ExecutionContext executionContext = ExecutionContext.executionContextBuilder()
                .metaModels(metaModels)
                .modelContexts(
                        Stream.concat(
                                emfModels.stream().map(m -> m.toModelContext()),
                                plainXmlModels.stream().map(m -> m.toModelContext()))
                                .collect(Collectors.toList()))
                .log(log)
                .build()) {

            HutnModule module = new HutnModule();
            setHutnContext(module, executionContext.getProjectModelRepository());
            parseHutnAndStoreModel(module);

            executionContext.commit();
        } catch (Exception e) {
            log.error("Could parse HUTN:", e);
        }
    }

    private void parseHutnAndStoreModel(HutnModule module)
            throws Exception {
        getLog().info("Start parsing HUTN file");

        if (module.parse(hutnFile)) {
            getLog().info("Parsing successfull, storing transformed emf model");
            List<File> files = module.storeEmfModel(targetFile.getParentFile(), targetFile.getName(), "any");
            for (File file : files) {
                getLog().info("Transformed: " + file);
            }
        } else {
            StringBuffer sb = new StringBuffer();
            for (ParseProblem p : module.getParseProblems()) {
                sb.append("\n\t" + p.toString());
            }

            throw new MojoExecutionException(sb.toString());
        }
    }

    private void setHutnContext(HutnModule module, ModelRepository modelRepository) {
        HutnContext context = new HutnContext(module);
        context.setErrorStream(System.err);
        context.setWarningStream(System.out);
        context.setOutputStream(System.out);
        context.setModelRepository(modelRepository);
        module.setContext(context);
    }

}

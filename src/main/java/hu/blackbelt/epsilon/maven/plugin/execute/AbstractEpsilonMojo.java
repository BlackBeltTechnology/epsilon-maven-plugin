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

import hu.blackbelt.epsilon.maven.plugin.MavenLog;
import org.slf4j.Logger;
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
    public List<EmfModel> emfModels;

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

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
import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

public class Eol {
    @Parameter(name = "source", required = true)
    String source;

    @Parameter(name = "parameters")
    List<EolProgramParameter> parameters = Lists.newArrayList();
    
    EolExecutionContext toExecutionContext() {
        return EolExecutionContext.eolExecutionContextBuilder()
                .parameters(parameters.stream()
                        .map(p -> programParameterBuilder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(URI.create(source))
                .build();
    }

}

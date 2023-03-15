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

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EmlType;
import hu.blackbelt.epsilon.runtime.execution.contexts.EmlExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import lombok.Builder;

import java.net.URI;
import java.util.Collections;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

@Builder
public class Eml {

    EmlType eml;

    EmlExecutionContext toExecutionContext() {
        return EmlExecutionContext.emlExecutionContextBuilder()
                .parameters(eml.getParameters() != null ? eml.getParameters().getParameter().stream()
                        .map(p -> programParameterBuilder().name(p.getName()).value(p.getValue()).build())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .source(URI.create(eml.getSource()))
                .useMatchTrace(eml.getUseMatchTrace())
                .build();
    }
}

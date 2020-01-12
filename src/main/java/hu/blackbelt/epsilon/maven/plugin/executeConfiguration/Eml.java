package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

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

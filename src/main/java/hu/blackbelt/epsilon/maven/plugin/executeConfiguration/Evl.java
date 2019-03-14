package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EvlType;
import hu.blackbelt.epsilon.runtime.execution.contexts.EvlExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import lombok.Builder;

import java.util.Collections;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

@Builder
public class Evl {

    EvlType evl;

    EvlExecutionContext toExecutionContext() {
        return EvlExecutionContext.evlExecutionContextBuilder()
                .parameters(evl.getParameters() != null ? evl.getParameters().getParameter().stream()
                        .map(p -> programParameterBuilder().name(p.getName()).value(p.getValue()).build())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .source(evl.getSource())
                .expectedErrors(evl.getExpectedErrors() != null ? evl.getExpectedErrors().getExpectedError() : null)
                .expectedWarnings(evl.getExpectedWarnings() != null ? evl.getExpectedWarnings().getExpectedWarning() : null)
                .build();
    }
}

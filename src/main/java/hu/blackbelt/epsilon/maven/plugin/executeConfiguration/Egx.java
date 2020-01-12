package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EgxType;
import hu.blackbelt.epsilon.runtime.execution.contexts.EglExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EgxExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import lombok.Builder;

import java.net.URI;
import java.util.Collections;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

@Builder
public class Egx {

    EgxType egx;

    EglExecutionContext toExecutionContext() {
        return EgxExecutionContext.egxExecutionContextBuilder()
                .parameters(egx.getParameters() != null ? egx.getParameters().getParameter().stream()
                        .map(p -> programParameterBuilder().name(p.getName()).value(p.getValue()).build())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .source(URI.create(egx.getSource()))
                .build();
    }
}

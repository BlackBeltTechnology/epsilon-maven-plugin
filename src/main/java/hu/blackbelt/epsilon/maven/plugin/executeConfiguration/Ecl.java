package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EclType;
import hu.blackbelt.epsilon.runtime.execution.contexts.EclExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import lombok.Builder;

import java.net.URI;
import java.util.Collections;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

@Builder
public class Ecl {

    EclType ecl;

    EclExecutionContext toExecutionContext() {
        return EclExecutionContext.eclExecutionContextBuilder()
                .parameters(ecl.getParameters() != null ? ecl.getParameters().getParameter().stream()
                        .map(p -> programParameterBuilder().name(p.getName()).value(p.getValue()).build())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .source(URI.create(ecl.getSource()))
                .exportMatchTrace(ecl.getExportMatchTrace())
                .useMatchTrace(ecl.getUseMatchTrace())
                .build();
    }
}

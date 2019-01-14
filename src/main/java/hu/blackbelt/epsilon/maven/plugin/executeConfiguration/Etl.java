package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EtlType;
import hu.blackbelt.epsilon.runtime.execution.contexts.EtlExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import lombok.Builder;

import java.util.Collections;
import java.util.stream.Collectors;

@Builder
public class Etl {

    EtlType etl;

    EtlExecutionContext toExecutionContext() {
        return EtlExecutionContext.etlExecutionContextBuilder()
                .parameters(etl.getParameters() != null ? etl.getParameters().getParameter().stream()
                        .map(p -> ProgramParameter.builder().name(p.getName()).value(p.getValue()).build())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .source(etl.getSource())
                .build();
    }
}

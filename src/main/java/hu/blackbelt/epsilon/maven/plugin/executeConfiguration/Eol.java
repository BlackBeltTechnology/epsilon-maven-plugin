package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EolType;
import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import lombok.Builder;

import java.util.Collections;
import java.util.stream.Collectors;

@Builder
public class Eol {

    EolType eol;

    EolExecutionContext toExecutionContext() {
        return EolExecutionContext.eolExecutionContextBuilder()
                .parameters(eol.getParameters() != null ? eol.getParameters().getParameter().stream()
                        .map(p -> ProgramParameter.builder().name(p.getName()).value(p.getValue()).build())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .source(eol.getSource()).build();
    }
}

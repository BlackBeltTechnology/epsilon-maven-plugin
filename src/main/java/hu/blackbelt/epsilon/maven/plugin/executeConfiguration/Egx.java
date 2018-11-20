package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EgxType;
import hu.blackbelt.epsilon.runtime.execution.contexts.EglExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EgxExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import lombok.Builder;

import java.util.Collections;
import java.util.stream.Collectors;

@Builder
public class Egx {

    EgxType egx;

    EglExecutionContext toExecutionContext() {
        return EgxExecutionContext.egxExecutionContextBuilder()
                .parameters(egx.getParameters() != null ? egx.getParameters().getParameter().stream()
                        .map(p -> ProgramParameter.builder().name(p.getName()).value(p.getValue()).build())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .outputRoot(egx.getOutputRoot())
                .source(egx.getSource()).build();
    }
}

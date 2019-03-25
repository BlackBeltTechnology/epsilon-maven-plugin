package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EglType;
import hu.blackbelt.epsilon.runtime.execution.contexts.EglExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import lombok.Builder;

import java.util.Collections;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

@Builder
public class Egl {

    EglType egl;

    EglExecutionContext toExecutionContext() {
        return EglExecutionContext.eglExecutionContextBuilder()
                .parameters(egl.getParameters() != null ? egl.getParameters().getParameter().stream()
                        .map(p -> programParameterBuilder().name(p.getName()).value(p.getValue()).build())
                        .collect(Collectors.toList()) : Collections.emptyList())
                .source(egl.getSource())
                .outputRoot(egl.getOutputRoot())
                .build();
    }
}

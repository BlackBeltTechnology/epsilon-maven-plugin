package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.contexts.EgxExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;

import java.util.stream.Collectors;

public class Egx extends Egl {

    @Override
    EolExecutionContext toExecutionContext() {
        return EgxExecutionContext.egxExecutionContextBuilder()
                .parameters(parameters.stream()
                        .map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .outputRoot(outputRoot)
                .source(source).build();
    }


}
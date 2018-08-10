package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EvlExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;

import java.util.stream.Collectors;


public class Evl extends Eol {

    EolExecutionContext toExecutionContext() {
        return EvlExecutionContext.evlExecutionContextBuilder()
                .parameters(parameters.stream()
                        .map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(source)
                .build();
    }
}

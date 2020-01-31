package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.contexts.EmlExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

public class Eml extends Etl {

    @Parameter(property = "useMatchTrace", defaultValue = "matchTrace")
    private String useMatchTrace;

    @Override
    EolExecutionContext toExecutionContext() {
        return EmlExecutionContext.emlExecutionContextBuilder()
                .parameters(parameters.stream()
                        .map(p -> programParameterBuilder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(URI.create(source))
                .useMatchTrace(useMatchTrace)
                .build();
    }

}

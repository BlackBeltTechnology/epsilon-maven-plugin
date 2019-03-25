package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EvlExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

public class Evl extends Eol {

    @Parameter(name = "expectedErrors")
    List<String> expectedErrors;

    @Parameter(name = "expectedWarnings")
    List<String> expectedWarnings;

    EolExecutionContext toExecutionContext() {
        return EvlExecutionContext.evlExecutionContextBuilder()
                .parameters(parameters.stream()
                        .map(p -> programParameterBuilder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(source)
                .expectedErrors(expectedErrors)
                .expectedWarnings(expectedWarnings)
                .build();
    }
}

package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EtlExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.stream.Collectors;

public class Etl extends Eol {

    @Parameter(name = "exportTransformationState", readonly = false, required = true)
    String exportTransformationState;


    @Override
    EolExecutionContext toExecutionContext() {
        return EtlExecutionContext.etlExecutionContextBuilder()
                .parameters(parameters.stream()
                        .map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .exportTransformationTrace(exportTransformationState)
                .source(source)
                .build();
    }

}

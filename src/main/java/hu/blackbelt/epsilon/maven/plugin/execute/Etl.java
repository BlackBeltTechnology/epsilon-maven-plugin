package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EtlExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

public class Etl extends Eol {

    @Parameter(name = "exportTransformationTrace", readonly = false, required = true)
    String exportTransformationTrace;


    @Override
    EolExecutionContext toExecutionContext() {
        return EtlExecutionContext.etlExecutionContextBuilder()
                .parameters(parameters.stream()
                        .map(p -> programParameterBuilder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .exportTransformationTrace(exportTransformationTrace)
                .source(URI.create(source))
                .build();
    }

}

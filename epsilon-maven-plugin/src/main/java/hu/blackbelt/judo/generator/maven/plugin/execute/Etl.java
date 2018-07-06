package hu.blackbelt.judo.generator.maven.plugin.execute;

import java.util.stream.Collectors;

import hu.blackbelt.judo.generator.utils.execution.contexts.EolExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.EtlExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.ProgramParameter;

public class Etl extends Eol {

    @Override
    EolExecutionContext toExecutionContext() {
        return EtlExecutionContext.etlExecutionContextBuilder()
                .artifact(artifact)
                .parameters(parameters.stream()
                        .map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(source)
                .build();
    }

}

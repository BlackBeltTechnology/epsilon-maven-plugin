package hu.blackbelt.judo.generator.maven.plugin.execute;

import hu.blackbelt.judo.generator.utils.execution.contexts.EgxExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.EolExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.ProgramParameter;

import java.util.stream.Collectors;

public class Egx extends Egl {

    @Override
    EolExecutionContext toExecutionContext() {
        return EgxExecutionContext.egxExecutionContextBuilder()
                .artifact(artifact)
                .parameters(parameters.stream()
                        .map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(source).build();
    }


}

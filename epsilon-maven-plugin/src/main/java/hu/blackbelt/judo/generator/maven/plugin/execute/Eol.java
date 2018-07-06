package hu.blackbelt.judo.generator.maven.plugin.execute;

import hu.blackbelt.judo.generator.utils.execution.contexts.EolExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;
import java.util.stream.Collectors;

public class Eol {
    @Parameter(name = "source", required = true)
    String source;

    @Parameter(name = "parameters")
    List<EolProgramParameter> parameters;
    
    @Parameter(name = "artifact")
    String artifact;

    EolExecutionContext toExecutionContext() {
        return EolExecutionContext.eolExecutionContextBuilder()
                .artifact(artifact)
                .parameters(parameters.stream()
                        .map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(source)
                .build();
    }

}

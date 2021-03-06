package hu.blackbelt.epsilon.maven.plugin.execute;

import com.google.common.collect.Lists;
import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

public class Eol {
    @Parameter(name = "source", required = true)
    String source;

    @Parameter(name = "parameters")
    List<EolProgramParameter> parameters = Lists.newArrayList();
    
    EolExecutionContext toExecutionContext() {
        return EolExecutionContext.eolExecutionContextBuilder()
                .parameters(parameters.stream()
                        .map(p -> programParameterBuilder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(URI.create(source))
                .build();
    }

}

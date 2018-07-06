package hu.blackbelt.judo.generator.maven.plugin.execute;

import java.util.stream.Collectors;

import hu.blackbelt.judo.generator.utils.execution.contexts.EmlExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.EolExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

public class Eml extends Etl {
	
	@Parameter(property = "useMatchTrace", defaultValue = "matchTrace")
	private String useMatchTrace;
	
	@Override
    EolExecutionContext toExecutionContext() {
        return EmlExecutionContext.emlExecutionContextBuilder()
                .artifact(artifact)
                .parameters(parameters.stream()
                        .map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
                        .collect(Collectors.toList()))
                .source(source)
                .useMatchTrace(useMatchTrace)
                .build();
    }

}

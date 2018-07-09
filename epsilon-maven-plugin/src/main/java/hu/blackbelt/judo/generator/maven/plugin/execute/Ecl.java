package hu.blackbelt.judo.generator.maven.plugin.execute;

import hu.blackbelt.judo.generator.utils.execution.contexts.EclExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.EolExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.stream.Collectors;

public class Ecl extends Eol {
	
	@Parameter(property = "exportMatchTrace", defaultValue = "matchTrace")
	private String exportMatchTrace;

	@Parameter(property = "useMatchTrace", defaultValue = "matchTrace")
	private String useMatchTrace;

    @Override
	EolExecutionContext toExecutionContext() {
		return EclExecutionContext.eclExecutionContextBuilder()
				.parameters(parameters.stream()
						.map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
						.collect(Collectors.toList()))
				.source(source)
				.exportMatchTrace(exportMatchTrace)
				.useMatchTrace(useMatchTrace)
				.build();
	}
}

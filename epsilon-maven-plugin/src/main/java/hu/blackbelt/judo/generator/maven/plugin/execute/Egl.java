package hu.blackbelt.judo.generator.maven.plugin.execute;

import hu.blackbelt.judo.generator.utils.execution.contexts.EglExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.EolExecutionContext;
import hu.blackbelt.judo.generator.utils.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;
import java.util.stream.Collectors;

public class Egl extends Eol {

	@Parameter(property = "outputRoot", defaultValue = "${project.basedir}/target/generated-sources")
	protected String outputRoot;

	@Override
	EolExecutionContext toExecutionContext() {
		return EglExecutionContext.eglExecutionContextBuilder()
				.parameters(parameters.stream()
						.map(p -> ProgramParameter.builder().name(p.name).value(p.value).build())
						.collect(Collectors.toList()))
				.source(source)
				.outputRoot(outputRoot)
				.build();
	}

}

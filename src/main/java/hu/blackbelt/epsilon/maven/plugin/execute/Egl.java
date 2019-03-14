package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.contexts.EglExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.EolExecutionContext;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter.programParameterBuilder;

public class Egl extends Eol {

	@Parameter(property = "outputRoot", defaultValue = "${project.basedir}/target/generated-sources")
	protected String outputRoot;

	@Override
	EolExecutionContext toExecutionContext() {
		return EglExecutionContext.eglExecutionContextBuilder()
				.parameters(parameters.stream()
						.map(p -> programParameterBuilder().name(p.name).value(p.value).build())
						.collect(Collectors.toList()))
				.source(source)
				.outputRoot(outputRoot)
				.build();
	}

}

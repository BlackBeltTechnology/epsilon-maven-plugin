package hu.blackbelt.epsilon.maven.plugin.execute;

import org.apache.maven.plugins.annotations.Parameter;

public class EolProgramParameter {

    @Parameter(name = "name")
    String name;

    @Parameter(name = "value")
    String value;
}

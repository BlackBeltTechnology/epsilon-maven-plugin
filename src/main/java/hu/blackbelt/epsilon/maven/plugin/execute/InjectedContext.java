package hu.blackbelt.epsilon.maven.plugin.execute;

import lombok.Getter;
import lombok.Setter;
import org.apache.maven.plugins.annotations.Parameter;

@Getter
@Setter
public class InjectedContext {

    @Parameter(name = "name")
    String name;

    @Parameter(name = "class")
    String clazz;
}

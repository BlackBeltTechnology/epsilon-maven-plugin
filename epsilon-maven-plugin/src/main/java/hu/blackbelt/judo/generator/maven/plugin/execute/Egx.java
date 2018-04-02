package hu.blackbelt.judo.generator.maven.plugin.execute;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.eol.IEolExecutableModule;

import java.util.Map;

public class Egx extends Egl {

    private EgxModule module;

    IEolExecutableModule getModule(Map<Object, Object> context) throws MojoExecutionException {
        module = new EgxModule(getTemplateFactory(context));
        return module;
    }

}

package hu.blackbelt.judo.generator.utils.execution.contexts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.eol.IEolExecutableModule;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class EgxExecutionContext extends EglExecutionContext {

    @Builder(builderMethodName = "egxExecutionContextBuilder")
    public EgxExecutionContext(String source, List<ProgramParameter> parameters, String artifact, String outputRoot) {
        super(source, parameters, artifact, outputRoot);
    }

    @Override
    public IEolExecutableModule getModule(Map<Object, Object> context) throws MojoExecutionException {
        EgxModule module = new EgxModule(getTemplateFactory(context));
        return module;
    }

}

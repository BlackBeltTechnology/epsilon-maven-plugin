package hu.blackbelt.judo.generator.utils.execution.contexts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.egl.exceptions.EglRuntimeException;
import org.eclipse.epsilon.eol.IEolExecutableModule;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class EglExecutionContext extends EolExecutionContext {

    public static final String ARTIFACT_ROOT = "ARTIFACT_ROOT";

    @Getter
    @NonNull
    private String outputRoot;

    @Builder(builderMethodName = "eglExecutionContextBuilder")
    public EglExecutionContext(String source, List<ProgramParameter> parameters, String outputRoot) {
        super(source, parameters);
        this.outputRoot = outputRoot;
    }

    protected EglTemplateFactory getTemplateFactory(Map<Object, Object> context) throws MojoExecutionException {
        EglTemplateFactory templateFactory;
        try {
            templateFactory = EglFileGeneratingTemplateFactory.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e1) {
            // TODO Auto-generated catch block
            throw new MojoExecutionException("Could not isntattiate tempalte factory", e1);
        }

        File outputRootDir = new File(outputRoot);
        if (!outputRootDir.exists()) {
            outputRootDir.mkdirs();
        }

        if (templateFactory instanceof EglFileGeneratingTemplateFactory && outputRoot != null) {
            try {
                ((EglFileGeneratingTemplateFactory) templateFactory).setOutputRoot(outputRootDir.getAbsolutePath());
                if (context.get(ARTIFACT_ROOT)!= null) {
                    URI main = (URI)context.get(ARTIFACT_ROOT);
                    ((EglFileGeneratingTemplateFactory) templateFactory).setRoot(main);
                } else {
                    throw new MojoExecutionException("Artifact must be set!");
                }
            } catch (EglRuntimeException e) {
                throw new MojoExecutionException("Could not create tempalte factory", e);
            }
        }
        return templateFactory;
    }


    @Override
    public IEolExecutableModule getModule(Map<Object, Object> context) throws MojoExecutionException {
        EglTemplateFactoryModuleAdapter module = new EglTemplateFactoryModuleAdapter(getTemplateFactory(context));
        return module;
    }

}

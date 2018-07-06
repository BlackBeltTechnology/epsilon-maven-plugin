package hu.blackbelt.judo.generator.utils.execution.contexts;

import lombok.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.IEolExecutableModule;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class EolExecutionContext {
    @Getter
    @NonNull
    private String source;

    @Getter
    @NonNull
    private List<ProgramParameter> parameters;

    @Getter
    @NonNull
    private String artifact;

    @Builder.Default
    private EolModule module = new EolModule();

    @Builder(builderMethodName = "eolExecutionContextBuilder")
    public EolExecutionContext(String source, List<ProgramParameter> parameters, String artifact) {
        this.source = source;
        this.parameters = parameters;
        this.artifact = artifact;
    }

    public IEolExecutableModule getModule(Map<Object, Object> context) throws MojoExecutionException {
        return module;
    };

    public boolean isOk() {
        return true;
    }

    public String toString() {
        return "";
    }
    
    public void post(Map<Object, Object> context) {}

}

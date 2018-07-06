package hu.blackbelt.judo.generator.utils.execution.contexts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.etl.EtlModule;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class EtlExecutionContext extends EolExecutionContext {

    @Builder.Default
    EtlModule etlModule = new EtlModule();

    @Builder(builderMethodName = "etlExecutionContextBuilder")
    public EtlExecutionContext(String source, List<ProgramParameter> parameters, String artifact) {
        super(source, parameters, artifact);
    }

    @Override
    public IEolExecutableModule getModule(Map<Object, Object> context) {
        return etlModule;
    };

}

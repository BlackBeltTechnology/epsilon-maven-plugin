package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.epsilon.common.util.StringProperties;

import java.io.File;
import java.util.List;

import static hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext.emfModelContextBuilder;

@Data
public class EmfModel {

    @Parameter(name = "artifact", readonly = true, required = true)
    String artifact;

    @Parameter(name = "name", required = true, readonly = true)
    String name;

    @Parameter(name = "aliases", readonly = true)
    List<String> aliases;

    @Parameter(name = "readOnLoad", defaultValue = "true", readonly = true)
    boolean readOnLoad = true;

    @Parameter(name = "storeOnDisposal", defaultValue = "true", readonly = true)
    boolean storeOnDisposal = false;

    @Parameter(name = "cached", defaultValue = "true", readonly = true)
    boolean cached = true;

    @Parameter(name = "referenceUri", readonly = true)
    String referenceUri;

    /**
     * One of the keys used to construct the first argument to {@link org.eclipse.epsilon.emc.emf.EmfModel#load(StringProperties, String)}.
     *
     * When paired with "true", external references will be resolved during loading.
     * Otherwise, external references are not resolved.
     *
     * Paired with "true" by default.
     */
    @Parameter(name = "expand", defaultValue = "true", readonly = true)
    boolean expand;

    /**
     * Validate model against Ecore metamodel and fail on validation errors.
     */
    @Parameter(name = "validateModel", defaultValue = "true", readonly = true)
    boolean validateModel;


    @Override
    public String toString() {
        return "EmfModel{" +
                "artifact='" + artifact + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", referenceUri='" + referenceUri + '\'' +
                ", expand=" + expand +
                ", validateModel='" + validateModel + '\'' +
                '}';
    }

    public EmfModelContext toModelContext() {
        return emfModelContextBuilder()
                .aliases(aliases)
                .model(artifact)
                .cached(cached)
                .expand(expand)
                .name(name)
                .referenceUri(referenceUri)
                .readOnLoad(readOnLoad)
                .storeOnDisposal(storeOnDisposal)
                .validateModel(validateModel)
                .build();
    }
}

package hu.blackbelt.epsilon.maven.plugin.execute;

import com.google.common.collect.Lists;
import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.epsilon.common.util.StringProperties;

import java.util.List;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext.emfModelContextBuilder;

@Data
public class EmfModel {

    @Parameter(name = "emf", readonly = true, required = true)
    String emf;

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

    @Parameter(name = "expand", defaultValue = "true", readonly = true)
    boolean expand;

    /**
     * Validate model against Ecore metamodel and fail on validation errors.
     */
    @Parameter(name = "validateModel", defaultValue = "true", readonly = true)
    boolean validateModel;

    @Parameter(name = "uriMap")
    List<URIConverterMapEntry> uriMap = Lists.newArrayList();


    @Override
    public String toString() {
        return "EmfModel{" +
                "emf='" + emf + '\'' +
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
                .emf(emf)
                .cached(cached)
                .expand(expand)
                .name(name)
                .referenceUri(referenceUri)
                .readOnLoad(readOnLoad)
                .storeOnDisposal(storeOnDisposal)
                .validateModel(validateModel)
                .uriConverterMap(uriMap.stream().collect(Collectors.toMap(URIConverterMapEntry::getFromURI, URIConverterMapEntry::getToURI)))
                .build();
    }
}

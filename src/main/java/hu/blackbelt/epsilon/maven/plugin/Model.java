package hu.blackbelt.epsilon.maven.plugin;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.common.util.StringProperties;

import java.io.File;
import java.util.List;

@Data
public class Model {

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

    @Parameter(name = "metaModelFile", readonly = true)
    File metaModelFile;

    @Parameter(name = "platformAlias", readonly = true)
    String platformAlias;

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
     * One of the keys used to construct the first argument to {@link org.eclipse.epsilon.emc.emf.EmfModel#load(StringProperties, String)}.
     *
     * This key is a comma-separated list of zero or more namespaces URI of some of the metamodels to which
     * this model conforms. Users may combine this key with  to loadEmf "fileBasedMetamodelUris"
     * both file-based and URI-based metamodels at the same time.
     */
    @Parameter(name = "metaModelUris", readonly = true)
    List<String> metaModelUris;

    /**
     * One of the keys used to construct the first argument to {@link org.eclipse.epsilon.emc.emf.EmfModel#load(StringProperties, String)}.
     *
     * This key is a comma-separated list of zero or more {@link URI}s that can be used to locate some of the
     * metamodels to which this model conforms. Users may combine this key with "metaModelUris"
     * to loadEmf both file-based and URI-based metamodels at the same time.
     */
    @Parameter(name = "fileBasedMetamodelUris", readonly = true)
    List<String> fileBasedMetamodelUris;


    /**
     * One of the keys used to construct the first argument to {@link org.eclipse.epsilon.emc.emf.EmfModel#load(StringProperties, String)}.
     *
     * This key is paired with a {@link URI} that can be used to locate this model.
     * This key must always be paired with a value.
     */
    // public static final String PROPERTY_MODEL_URI = "modelUri";

    @Parameter(name = "modelUri", readonly = true)
    String modelUri;

    /**
     * One of the keys used to construct the first argument to
     * {@link org.eclipse.epsilon.emc.emf.EmfModel#load(StringProperties, String)}.
     *
     * This key is a Boolean value that if set to <code>true</code> (the
     * default), tries to reuse previously registered file-based EPackages that
     * have not been modified since the last time they were registered.
     */
    @Parameter(name = "reuseUnmodifiedFileBasedMetamodels", defaultValue = "true", readonly = true)
    boolean reuseUnmodifiedFileBasedMetamodels;


    @Override
    public String toString() {
        return "Model{" +
                "artifact='" + artifact + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", metaModelFile=" + metaModelFile +
                ", platformAlias='" + platformAlias + '\'' +
                ", expand=" + expand +
                ", metaModelUris=" + metaModelUris +
                ", fileBasedMetamodelUris=" + fileBasedMetamodelUris +
                ", modelUri='" + modelUri + '\'' +
                ", reuseUnmodifiedFileBasedMetamodels=" + reuseUnmodifiedFileBasedMetamodels +
                '}';
    }

    public EmfModelContext toModelContext() {
        return EmfModelContext.builder()
                .aliases(aliases)
                .artifacts(ImmutableMap.of("model", artifact))
                .cached(cached)
                .expand(expand)
                .fileBasedMetamodelUris(fileBasedMetamodelUris)
                .metaModelFile(metaModelFile)
                .metaModelUris(metaModelUris)
                .modelUri(modelUri)
                .name(name)
                .platformAlias(platformAlias)
                .readOnLoad(readOnLoad)
                .reuseUnmodifiedFileBasedMetamodels(reuseUnmodifiedFileBasedMetamodels)
                .storeOnDisposal(storeOnDisposal)
                .build();
    }
}

package hu.blackbelt.judo.generator.maven.plugin;

import hu.blackbelt.judo.generator.utils.execution.model.emf.EmfModelContext;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.models.ModelReference;
import org.eclipse.epsilon.eol.models.ModelRepository;

import java.io.File;
import java.util.List;

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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public boolean isReadOnLoad() {
        return readOnLoad;
    }

    public void setReadOnLoad(boolean readOnLoad) {
        this.readOnLoad = readOnLoad;
    }

    public boolean isStoreOnDisposal() {
        return storeOnDisposal;
    }

    public void setStoreOnDisposal(boolean storeOnDisposal) {
        this.storeOnDisposal = storeOnDisposal;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public List<String> getMetaModelUris() {
        return metaModelUris;
    }

    public void setMetaModelUris(List<String> metaModelUris) {
        this.metaModelUris = metaModelUris;
    }

    public List<String> getFileBasedMetamodelUris() {
        return fileBasedMetamodelUris;
    }

    public void setFileBasedMetamodelUris(List<String> fileBasedMetamodelUris) {
        this.fileBasedMetamodelUris = fileBasedMetamodelUris;
    }

    public String getModelUri() {
        return modelUri;
    }

    public void setModelUri(String modelUri) {
        this.modelUri = modelUri;
    }

    public boolean isReuseUnmodifiedFileBasedMetamodels() {
        return reuseUnmodifiedFileBasedMetamodels;
    }

    public void setReuseUnmodifiedFileBasedMetamodels(boolean reuseUnmodifiedFileBasedMetamodels) {
        this.reuseUnmodifiedFileBasedMetamodels = reuseUnmodifiedFileBasedMetamodels;
    }

    public File getMetaModelFile() {
        return metaModelFile;
    }

    public void setMetaModelFile(File metaModelFile) {
        this.metaModelFile = metaModelFile;
    }

    public String getPlatformAlias() {
        return platformAlias;
    }

    public void setPlatformAlias(String platformAlias) {
        this.platformAlias = platformAlias;
    }

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }
        
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

    public void addAliases(ModelRepository repository, ModelReference ref) {
        ref.setName(this.getName());
        if (this.getAliases() != null) {
            for (String alias : this.getAliases()) {
                ref.getAliases().add(alias);
            }
        }
        repository.addModel(ref);
    }

    public EmfModelContext toModelContext() {
        return EmfModelContext.builder()
                .aliases(aliases)
                .artifact(artifact)
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

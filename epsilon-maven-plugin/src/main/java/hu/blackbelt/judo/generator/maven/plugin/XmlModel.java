package hu.blackbelt.judo.generator.maven.plugin;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

public class XmlModel {

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

    @Parameter(name = "platformAlias", readonly = true)
    String platformAlias;


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
        return "XmlModel{" +
                "artifact='" + artifact + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", platformAlias='" + platformAlias + '\'' +
                '}';
    }
}

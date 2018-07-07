package hu.blackbelt.judo.generator.maven.plugin;

import hu.blackbelt.judo.generator.utils.execution.model.plainxml.PlainXmlModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

@Data
public class PlainXmlModel {

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

    @Override
    public String toString() {
        return "PlainXmlModel{" +
                "artifact='" + artifact + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", platformAlias='" + platformAlias + '\'' +
                '}';
    }

    public PlainXmlModelContext toModelContext() {
        return PlainXmlModelContext.builder()
                .aliases(aliases)
                .artifact(artifact)
                .cached(cached)
                .name(name)
                .platformAlias(platformAlias)
                .readOnLoad(readOnLoad)
                .storeOnDisposal(storeOnDisposal)
                .build();
    }

}

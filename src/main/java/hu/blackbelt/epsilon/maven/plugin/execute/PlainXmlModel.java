package hu.blackbelt.epsilon.maven.plugin.execute;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.epsilon.runtime.execution.model.plainxml.PlainXmlModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

@Data
public class PlainXmlModel {

    @Parameter(name = "xml", readonly = true, required = true)
    String xml;

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

    @Override
    public String toString() {
        return "PlainXmlModel{" +
                "xml='" + xml + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                '}';
    }

    public PlainXmlModelContext toModelContext() {
        return PlainXmlModelContext.plainXmlModelContextBuilder()
                .aliases(aliases)
                .xml(xml)
                .cached(cached)
                .name(name)
                .readOnLoad(readOnLoad)
                .storeOnDisposal(storeOnDisposal)
                .build();
    }

}

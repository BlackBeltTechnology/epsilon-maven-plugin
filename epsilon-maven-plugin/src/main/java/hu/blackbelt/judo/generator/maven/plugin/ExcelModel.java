package hu.blackbelt.judo.generator.maven.plugin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import hu.blackbelt.judo.generator.utils.execution.model.excel.ExcelModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.epsilon.common.util.StringProperties;

import java.io.File;
import java.util.List;

@Data
public class ExcelModel {

    @Parameter(name = "artifact", readonly = true, required = true)
    String artifact;

    @Parameter(name = "configurationArtifact", readonly = true, required = true)
    String configurationArtifact;

    @Parameter(name = "spreadSheetPassword", readonly = true, required = false)
    String spreadSheetPassword;


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


    @Override
    public String toString() {
        return "ExcelModel{" +
                "artifact='" + artifact + '\'' +
                "configurationArtifact='" + configurationArtifact + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", metaModelFile=" + metaModelFile +
                ", platformAlias='" + platformAlias + '\'' +
                '}';
    }

    public ExcelModelContext toModelContext() {
        return ExcelModelContext.builder()
                .aliases(aliases)
                .artifacts(ImmutableMap.of("excelSheet", artifact, "excelConfiguration", configurationArtifact))
                .cached(cached)
                .name(name)
                .readOnLoad(readOnLoad)
                .storeOnDisposal(storeOnDisposal)
                .spreadSheetPassword(spreadSheetPassword)
                .build();
    }
}

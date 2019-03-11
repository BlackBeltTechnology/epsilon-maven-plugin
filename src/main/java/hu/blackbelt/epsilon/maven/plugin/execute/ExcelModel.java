package hu.blackbelt.epsilon.maven.plugin.execute;

import hu.blackbelt.epsilon.runtime.execution.model.excel.ExcelModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

import static hu.blackbelt.epsilon.runtime.execution.model.excel.ExcelModelContext.excelModelContextBuilder;

@Data
public class ExcelModel {

    @Parameter(name = "excel", readonly = true, required = true)
    String excel;

    @Parameter(name = "excelConfiguration", readonly = true, required = true)
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

    @Parameter(name = "referenceUri", readonly = true)
    String platformAlias;


    @Override
    public String toString() {
        return "ExcelModel{" +
                "excel='" + excel + '\'' +
                "configurationArtifact='" + configurationArtifact + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", referenceUri='" + platformAlias + '\'' +
                '}';
    }

    public ExcelModelContext toModelContext() {
        return excelModelContextBuilder()
                .aliases(aliases)
                .excel(excel)
                .excelConfiguration(configurationArtifact)
                .cached(cached)
                .name(name)
                .readOnLoad(readOnLoad)
                .storeOnDisposal(storeOnDisposal)
                .spreadSheetPassword(spreadSheetPassword)
                .build();
    }
}

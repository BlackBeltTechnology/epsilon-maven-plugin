package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.ExcelModelType;
import hu.blackbelt.epsilon.runtime.execution.model.excel.ExcelModelContext;

import java.util.Collections;

public class ExcelModel {

    final ExcelModelType excelModel;

    ExcelModel(ExcelModelType excelModel) {
        this.excelModel = excelModel;
    }

    public ExcelModelContext toModelContext() {
        return ExcelModelContext.builder()
                .aliases(excelModel.getAliases() != null ? excelModel.getAliases().getAlias() : Collections.emptyList())
                .artifacts(ImmutableMap.of("excelSheet", excelModel.getArtifact(), "excelConfiguration", excelModel.getConfigurationArtifact()))
                .cached(excelModel.isCached() != null ? excelModel.isCached() : true)
                .name(excelModel.getName())
                .readOnLoad(excelModel.isReadOnLoad() != null ? excelModel.isReadOnLoad() : true)
                .storeOnDisposal(excelModel.isStoreOnDisposal() != null ? excelModel.isStoreOnDisposal() : true)
                .spreadSheetPassword(excelModel.getSpreadSheetPassword())
                .build();
    }
}
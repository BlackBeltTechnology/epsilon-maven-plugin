package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.ModelType;
import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;

import java.io.File;
import java.util.Collections;

public class Model {

    final ModelType model;

    Model(ModelType model) {
        this.model = model;
    }

    public EmfModelContext toModelContext() {
        return EmfModelContext.builder()
                .aliases(model.getAliases() != null ? model.getAliases().getAlias() : Collections.emptyList())
                .artifacts(ImmutableMap.of("model", model.getArtifact()))
                .cached(model.isCached() != null ? model.isCached() : true)
                .expand(model.isExpand() != null ? model.isExpand() : true)
                .fileBasedMetamodelUris(model.getFileBasedMetamodelUris() != null ? model.getFileBasedMetamodelUris().getFileBasedMetamodelUri() : Collections.emptyList())
                .metaModelFile(model.getMetaModelFile() != null ? new File(model.getMetaModelFile()) : null)
                .metaModelUris(model.getMetaModelUris() != null ? model.getMetaModelUris().getParam() : Collections.emptyList())
                .modelUri(model.getModelUri())
                .name(model.getName())
                .platformAlias(model.getPlatformAlias())
                .readOnLoad(model.isReadOnLoad() != null ? model.isReadOnLoad() : true)
                .reuseUnmodifiedFileBasedMetamodels(model.isReuseUnmodifiedFileBasedMetamodels() != null ? model.isReuseUnmodifiedFileBasedMetamodels() : true)
                .storeOnDisposal(model.isStoreOnDisposal() != null ? model.isStoreOnDisposal() : true)
                .validateModel(model.isValidateModel() != null ? model.isValidateModel() : true)
                .build();
    }
}

package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.XmlModelType;
import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;
import hu.blackbelt.epsilon.runtime.execution.model.xml.XmlModelContext;

import java.io.File;
import java.util.Collections;

public class XmlModel {

    final XmlModelType xmlModel;

    XmlModel(XmlModelType xmlModel) {
        this.xmlModel = xmlModel;
    }

    public EmfModelContext toModelContext() {
        return XmlModelContext.builder()
                .aliases(xmlModel.getAliases() != null ? xmlModel.getAliases().getAlias() : Collections.emptyList())
                .artifacts(ImmutableMap.of("xml", xmlModel.getArtifact(), "xsd", xmlModel.getXsd()))
                .cached(xmlModel.isCached() != null ? xmlModel.isCached() : true)
                .expand(xmlModel.isExpand() != null ? xmlModel.isExpand() : true)
                .fileBasedMetamodelUris(xmlModel.getFileBasedMetamodelUris() != null ? xmlModel.getFileBasedMetamodelUris().getFileBasedMetamodelUri() : Collections.emptyList())
                .metaModelFile(xmlModel.getMetaModelFile() != null ? new File(xmlModel.getMetaModelFile()) : null)
                .metaModelUris(xmlModel.getMetaModelUris() != null ? xmlModel.getMetaModelUris().getParam() : Collections.emptyList())
                .modelUri(xmlModel.getModelUri())
                .name(xmlModel.getName())
                .platformAlias(xmlModel.getPlatformAlias())
                .readOnLoad(xmlModel.isReadOnLoad() != null ? xmlModel.isReadOnLoad() : true)
                .reuseUnmodifiedFileBasedMetamodels(xmlModel.isReuseUnmodifiedFileBasedMetamodels() != null ? xmlModel.isReuseUnmodifiedFileBasedMetamodels() : true)
                .storeOnDisposal(xmlModel.isStoreOnDisposal() != null ? xmlModel.isStoreOnDisposal() : true)
                .build();
    }
}

package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.PlainXmlModelType;
import hu.blackbelt.epsilon.runtime.execution.model.plainxml.PlainXmlModelContext;

import java.util.Collections;

public class PlainXmlModel {

    final PlainXmlModelType plainXmlModel;

    PlainXmlModel(PlainXmlModelType plainXmlModel) {
        this.plainXmlModel = plainXmlModel;
    }

    public PlainXmlModelContext toModelContext() {
        return PlainXmlModelContext.builder()
                .aliases(plainXmlModel.getAliases() != null ? plainXmlModel.getAliases().getAlias() : Collections.emptyList())
                .artifacts(ImmutableMap.of("xml", plainXmlModel.getArtifact()))
                .cached(plainXmlModel.isCached() != null ? plainXmlModel.isCached() : true)
                .name(plainXmlModel.getName())
                .platformAlias(plainXmlModel.getPlatformAlias())
                .readOnLoad(plainXmlModel.isReadOnLoad() != null ? plainXmlModel.isReadOnLoad() : true)
                .storeOnDisposal(plainXmlModel.isStoreOnDisposal() != null ? plainXmlModel.isStoreOnDisposal() : true)
                .build();
    }
}

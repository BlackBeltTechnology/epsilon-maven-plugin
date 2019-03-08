package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.ModelType;
import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;

import java.io.File;
import java.util.Collections;

import static hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext.emfModelContextBuilder;

public class Model {

    final ModelType model;

    Model(ModelType model) {
        this.model = model;
    }

    public EmfModelContext toModelContext() {
        return emfModelContextBuilder()
                .aliases(model.getAliases() != null ? model.getAliases().getAlias() : Collections.emptyList())
                .model(model.getArtifact())
                .cached(model.isCached() != null ? model.isCached() : true)
                .expand(model.isExpand() != null ? model.isExpand() : true)
                .name(model.getName())
                .referenceUri(model.getReferenceUri())
                .readOnLoad(model.isReadOnLoad() != null ? model.isReadOnLoad() : true)
                .storeOnDisposal(model.isStoreOnDisposal() != null ? model.isStoreOnDisposal() : true)
                .validateModel(model.isValidateModel() != null ? model.isValidateModel() : true)
                .build();
    }
}

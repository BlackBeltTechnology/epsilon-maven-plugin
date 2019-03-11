package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.EmfModelType;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.URIConverterMapType;
import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;

import java.util.Collections;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext.emfModelContextBuilder;

public class EmfModel {

    final EmfModelType emfModel;

    EmfModel(EmfModelType emfModel) {
        this.emfModel = emfModel;
    }

    public EmfModelContext toModelContext() {
        return emfModelContextBuilder()
                .aliases(emfModel.getAliases() != null ? emfModel.getAliases().getAlias() : Collections.emptyList())
                .emf(emfModel.getEmf())
                .cached(emfModel.isCached() != null ? emfModel.isCached() : true)
                .expand(emfModel.isExpand() != null ? emfModel.isExpand() : true)
                .name(emfModel.getName())
                .referenceUri(emfModel.getReferenceUri())
                .uriConverterMap( emfModel.getUriMap() != null ?
                        emfModel.getUriMap().getEntry().stream()
                                .collect(Collectors.toMap(URIConverterMapType::getFromURI, URIConverterMapType::getToURI))
                        : ImmutableMap.of())
                .readOnLoad(emfModel.isReadOnLoad() != null ? emfModel.isReadOnLoad() : true)
                .storeOnDisposal(emfModel.isStoreOnDisposal() != null ? emfModel.isStoreOnDisposal() : true)
                .validateModel(emfModel.isValidateModel() != null ? emfModel.isValidateModel() : true)
                .build();
    }
}

package hu.blackbelt.epsilon.maven.plugin.executeConfiguration;

/*-
 * #%L
 * epsilon-maven-plugin
 * %%
 * Copyright (C) 2018 - 2023 BlackBelt Technology
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */

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
                                .collect(Collectors.toMap(URIConverterMapType::getLogicalURI, URIConverterMapType::getPhysicalURI))
                        : ImmutableMap.of())
                .readOnLoad(emfModel.isReadOnLoad() != null ? emfModel.isReadOnLoad() : true)
                .storeOnDisposal(emfModel.isStoreOnDisposal() != null ? emfModel.isStoreOnDisposal() : true)
                .validateModel(emfModel.isValidateModel() != null ? emfModel.isValidateModel() : true)
                .build();
    }
}

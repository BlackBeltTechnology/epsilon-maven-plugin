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
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.URIConverterMapType;
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.XmlModelType;
import hu.blackbelt.epsilon.runtime.execution.contexts.ProgramParameter;
import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;
import hu.blackbelt.epsilon.runtime.execution.model.xml.XmlModelContext;

import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.model.xml.XmlModelContext.xmlModelContextBuilder;

public class XmlModel {

    final XmlModelType xmlModel;

    XmlModel(XmlModelType xmlModel) {
        this.xmlModel = xmlModel;
    }

    public EmfModelContext toModelContext() {
        return xmlModelContextBuilder()
                .aliases(xmlModel.getAliases() != null ? xmlModel.getAliases().getAlias() : Collections.emptyList())
                .xml(xmlModel.getXml())
                .xsd(xmlModel.getXsd())
                .cached(xmlModel.isCached() != null ? xmlModel.isCached() : true)
                .expand(xmlModel.isExpand() != null ? xmlModel.isExpand() : true)
                .name(xmlModel.getName())
                .referenceUri(xmlModel.getReferenceUri())
                .uriConverterMap( xmlModel.getUriMap() != null ?
                        xmlModel.getUriMap().getEntry().stream()
                                .collect(Collectors.toMap(URIConverterMapType::getLogicalURI, URIConverterMapType::getPhysicalURI))
                        : ImmutableMap.of())
                .readOnLoad(xmlModel.isReadOnLoad() != null ? xmlModel.isReadOnLoad() : true)
                .storeOnDisposal(xmlModel.isStoreOnDisposal() != null ? xmlModel.isStoreOnDisposal() : true)
                .build();
    }
}

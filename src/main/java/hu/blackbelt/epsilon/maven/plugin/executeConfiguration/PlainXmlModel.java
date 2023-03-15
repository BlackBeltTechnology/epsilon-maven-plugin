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
import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.PlainXmlModelType;
import hu.blackbelt.epsilon.runtime.execution.model.plainxml.PlainXmlModelContext;

import java.util.Collections;

public class PlainXmlModel {

    final PlainXmlModelType plainXmlModel;

    PlainXmlModel(PlainXmlModelType plainXmlModel) {
        this.plainXmlModel = plainXmlModel;
    }

    public PlainXmlModelContext toModelContext() {
        return PlainXmlModelContext.plainXmlModelContextBuilder()
                .aliases(plainXmlModel.getAliases() != null ? plainXmlModel.getAliases().getAlias() : Collections.emptyList())
                .xml(plainXmlModel.getXml())
                .cached(plainXmlModel.isCached() != null ? plainXmlModel.isCached() : true)
                .name(plainXmlModel.getName())
                .readOnLoad(plainXmlModel.isReadOnLoad() != null ? plainXmlModel.isReadOnLoad() : true)
                .storeOnDisposal(plainXmlModel.isStoreOnDisposal() != null ? plainXmlModel.isStoreOnDisposal() : true)
                .build();
    }
}

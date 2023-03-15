package hu.blackbelt.epsilon.maven.plugin.execute;

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

import com.google.common.collect.Lists;
import hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;
import java.util.stream.Collectors;

import static hu.blackbelt.epsilon.runtime.execution.model.emf.EmfModelContext.emfModelContextBuilder;

@Data
public class EmfModel {

    @Parameter(name = "emf", readonly = true, required = true)
    String emf;

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
    String referenceUri;

    @Parameter(name = "expand", defaultValue = "true", readonly = true)
    boolean expand;

    /**
     * Validate model against Ecore metamodel and fail on validation errors.
     */
    @Parameter(name = "validateModel", defaultValue = "true", readonly = true)
    boolean validateModel;

    @Parameter(name = "uriMap")
    List<URIConverterMapEntry> uriMap = Lists.newArrayList();


    @Override
    public String toString() {
        return "EmfModel{" +
                "emf='" + emf + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", referenceUri='" + referenceUri + '\'' +
                ", expand=" + expand +
                ", validateModel='" + validateModel + '\'' +
                '}';
    }

    public EmfModelContext toModelContext() {
        return emfModelContextBuilder()
                .aliases(aliases)
                .emf(emf)
                .cached(cached)
                .expand(expand)
                .name(name)
                .referenceUri(referenceUri)
                .readOnLoad(readOnLoad)
                .storeOnDisposal(storeOnDisposal)
                .validateModel(validateModel)
                .uriConverterMap(uriMap.stream().collect(Collectors.toMap(URIConverterMapEntry::getLogicalURI, URIConverterMapEntry::getPhysicalURI)))
                .build();
    }
}

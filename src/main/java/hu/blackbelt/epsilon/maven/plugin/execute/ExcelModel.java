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

import hu.blackbelt.epsilon.runtime.execution.model.excel.ExcelModelContext;
import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

import static hu.blackbelt.epsilon.runtime.execution.model.excel.ExcelModelContext.excelModelContextBuilder;

@Data
public class ExcelModel {

    @Parameter(name = "excel", readonly = true, required = true)
    String excel;

    @Parameter(name = "excelConfiguration", readonly = true, required = true)
    String configurationArtifact;

    @Parameter(name = "spreadSheetPassword", readonly = true, required = false)
    String spreadSheetPassword;

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
    String platformAlias;


    @Override
    public String toString() {
        return "ExcelModel{" +
                "excel='" + excel + '\'' +
                "configurationArtifact='" + configurationArtifact + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", referenceUri='" + platformAlias + '\'' +
                '}';
    }

    public ExcelModelContext toModelContext() {
        return excelModelContextBuilder()
                .aliases(aliases)
                .excel(excel)
                .excelConfiguration(configurationArtifact)
                .cached(cached)
                .name(name)
                .readOnLoad(readOnLoad)
                .storeOnDisposal(storeOnDisposal)
                .spreadSheetPassword(spreadSheetPassword)
                .build();
    }
}

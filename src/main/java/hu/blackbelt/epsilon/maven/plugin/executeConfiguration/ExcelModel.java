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

import hu.blackbelt.epsilon.maven.plugin.v1.xml.ns.definition.ExcelModelType;
import hu.blackbelt.epsilon.runtime.execution.model.excel.ExcelModelContext;

import java.util.Collections;

import static hu.blackbelt.epsilon.runtime.execution.model.excel.ExcelModelContext.excelModelContextBuilder;

public class ExcelModel {

    final ExcelModelType excelModel;

    ExcelModel(ExcelModelType excelModel) {
        this.excelModel = excelModel;
    }

    public ExcelModelContext toModelContext() {
        return excelModelContextBuilder()
                .aliases(excelModel.getAliases() != null ? excelModel.getAliases().getAlias() : Collections.emptyList())
                .excel(excelModel.getExcel())
                .excelConfiguration(excelModel.getExcelConfiguration())
                .cached(excelModel.isCached() != null ? excelModel.isCached() : true)
                .name(excelModel.getName())
                .readOnLoad(excelModel.isReadOnLoad() != null ? excelModel.isReadOnLoad() : true)
                .storeOnDisposal(excelModel.isStoreOnDisposal() != null ? excelModel.isStoreOnDisposal() : true)
                .spreadSheetPassword(excelModel.getSpreadSheetPassword())
                .build();
    }
}

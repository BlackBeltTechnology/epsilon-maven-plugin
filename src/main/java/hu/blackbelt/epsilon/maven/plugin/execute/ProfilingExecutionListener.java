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

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.control.IExecutionListener;
import org.eclipse.epsilon.profiling.Profiler;

public class ProfilingExecutionListener implements IExecutionListener {
    public ProfilingExecutionListener() {
    }

    public void aboutToExecute(ModuleElement ast, IEolContext context) {
        Profiler.INSTANCE.start(this.getLabel(ast), "", ast);
    }

    public void finishedExecuting(ModuleElement ast, Object evaluatedAst, IEolContext context) {
        Profiler.INSTANCE.stop(this.getLabel(ast));
    }

    public void finishedExecutingWithException(ModuleElement ast, EolRuntimeException exception, IEolContext context) {
    }

    protected String getLabel(ModuleElement ast) {
        return ast.getClass().getSimpleName() + " (" + ast.getRegion().getStart().getLine() + ":" + ast.getRegion().getStart().getColumn() + ")";
    }
}

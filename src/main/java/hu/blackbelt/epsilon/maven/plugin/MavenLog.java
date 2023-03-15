package hu.blackbelt.epsilon.maven.plugin;

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


import hu.blackbelt.epsilon.runtime.execution.api.Log;
import hu.blackbelt.epsilon.runtime.execution.impl.LogLevel;
import hu.blackbelt.epsilon.runtime.execution.impl.StringBuilderLogger;
import org.slf4j.Logger;

public class MavenLog implements Log {

    final org.apache.maven.plugin.logging.Log log;

    public MavenLog(org.apache.maven.plugin.logging.Log log) {
        this.log = log;
    }

    public void trace(CharSequence charSequence) {
        log.debug(charSequence.toString());
    }

    public void trace(CharSequence charSequence, Throwable throwable) {
        log.debug(charSequence.toString(), throwable);
    }

    public void trace(Throwable throwable) {
        log.debug("", throwable);
    }

    public void debug(CharSequence charSequence) {
        log.debug(charSequence.toString());
    }

    public void debug(CharSequence charSequence, Throwable throwable) {
        log.debug(charSequence.toString(), throwable);
    }

    public void debug(Throwable throwable) {
        log.debug("", throwable);
    }

    public void info(CharSequence charSequence) {
        log.info(charSequence.toString());
    }

    public void info(CharSequence charSequence, Throwable throwable) {
        log.info(charSequence.toString(), throwable);
    }

    public void info(Throwable throwable) {
        log.info("", throwable);
    }

    public void warn(CharSequence charSequence) {
        log.warn(charSequence.toString());
    }

    public void warn(CharSequence charSequence, Throwable throwable) {
        log.warn(charSequence.toString(), throwable);
    }

    public void warn(Throwable throwable) {
        log.warn("", throwable);
    }

    public void error(CharSequence charSequence) {
        log.error(charSequence.toString());
    }

    public void error(CharSequence charSequence, Throwable throwable) {
        log.error(charSequence.toString(), throwable);
    }

    public void error(Throwable throwable) {
        log.error("", throwable);
    }

    public static LogLevel determinateLogLevel(org.apache.maven.plugin.logging.Log log) {
        if (log.isDebugEnabled()) {
            return  LogLevel.DEBUG;
        } else if (log.isInfoEnabled()) {
            return  LogLevel.INFO;
        } else if (log.isWarnEnabled()) {
            return LogLevel.WARN;
        } else if (log.isErrorEnabled()) {
            return LogLevel.ERROR;
        }
        return  LogLevel.INFO;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void flush() {
    }

}

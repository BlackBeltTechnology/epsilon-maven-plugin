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


import org.slf4j.Logger;
import hu.blackbelt.epsilon.runtime.execution.impl.LogLevel;
import hu.blackbelt.epsilon.runtime.execution.impl.StringBuilderLogger;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class MavenLog implements Logger {

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
    public String getName() {
        return "Slf4j Maven logger";
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String msg) {
    }

    @Override
    public void trace(String format, Object arg) {
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
    }

    @Override
    public void trace(String format, Object... arguments) {
    }

    @Override
    public void trace(String msg, Throwable t) {
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String msg) {
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        log.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        log.debug(String.format(format, arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log.debug(String.format(format, arg1, arg2));
    }

    @Override
    public void debug(String format, Object... arguments) {
        log.debug(String.format(format, arguments));
    }

    @Override
    public void debug(String msg, Throwable t) {
        log.debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(Marker marker, String msg) {
        log.debug(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        log.debug(String.format(format, arg));
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        log.debug(String.format(format, arg1, arg2));
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        log.debug(String.format(format, arguments));
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        log.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        log.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        log.info(String.format(format, arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        log.info(String.format(format, arg1, arg2));
    }

    @Override
    public void info(String format, Object... arguments) {
        log.info(String.format(format, arguments));
    }

    @Override
    public void info(String msg, Throwable t) {
        log.info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled();
    }

    @Override
    public void info(Marker marker, String msg) {
        log.info(msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        log.info(String.format(format, arg));
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        log.info(String.format(format, arg1, arg2));
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        log.info(String.format(format, arguments));
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        log.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        log.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        log.warn(String.format(format, arg));
    }

    @Override
    public void warn(String format, Object... arguments) {
        log.warn(String.format(format, arguments));
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log.warn(String.format(format, arg1, arg2));
    }

    @Override
    public void warn(String msg, Throwable t) {
        log.warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String msg) {
        log.warn(msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        log.warn(String.format(format, arg));
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        log.warn(String.format(format, arg1, arg2));
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        log.warn(String.format(format, arguments));
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        log.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        log.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        log.error(String.format(format, arg));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log.error(String.format(format, arg1, arg2));
    }

    @Override
    public void error(String format, Object... arguments) {
        log.error(String.format(format, arguments));
    }

    @Override
    public void error(String msg, Throwable t) {
        log.error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isErrorEnabled();
    }

    @Override
    public void error(Marker marker, String msg) {
        log.error(msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        log.error(String.format(format, arg));
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        log.error(String.format(format, arg1, arg2));
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        log.error(String.format(format, arguments));
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        log.error(msg, t);
    }
}

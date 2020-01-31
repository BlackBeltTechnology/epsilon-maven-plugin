package hu.blackbelt.epsilon.maven.plugin;


import hu.blackbelt.epsilon.runtime.execution.api.Log;
import hu.blackbelt.epsilon.runtime.execution.impl.StringBuilderLogger;
import org.slf4j.Logger;

public class MavenLog extends StringBuilderLogger implements Log {

    final org.apache.maven.plugin.logging.Log log;

    public MavenLog(org.apache.maven.plugin.logging.Log log) {
        super(LogLevel.DEBUG);
        this.log = log;
    }

    public void trace(CharSequence charSequence) {
        super.trace(charSequence);
        log.debug(charSequence.toString());
    }

    public void trace(CharSequence charSequence, Throwable throwable) {
        super.trace(charSequence, throwable);
        log.debug(charSequence.toString(), throwable);
    }

    public void trace(Throwable throwable) {
        super.trace(throwable);
        log.debug("", throwable);
    }

    public void debug(CharSequence charSequence) {
        super.debug(charSequence);
        log.debug(charSequence.toString());
    }

    public void debug(CharSequence charSequence, Throwable throwable) {
        super.debug(charSequence, throwable);
        log.debug(charSequence.toString(), throwable);
    }

    public void debug(Throwable throwable) {
        super.debug(throwable);
        log.debug("", throwable);
    }

    public void info(CharSequence charSequence) {
        super.info(charSequence);
        log.info(charSequence.toString());
    }

    public void info(CharSequence charSequence, Throwable throwable) {
        super.info(charSequence, throwable);
        log.info(charSequence.toString(), throwable);
    }

    public void info(Throwable throwable) {
        super.info(throwable);
        log.info("", throwable);
    }

    public void warn(CharSequence charSequence) {
        super.warn(charSequence);
        log.warn(charSequence.toString());
    }

    public void warn(CharSequence charSequence, Throwable throwable) {
        super.warn(charSequence, throwable);
        log.warn(charSequence.toString(), throwable);
    }

    public void warn(Throwable throwable) {
        super.warn(throwable);
        log.warn("", throwable);
    }

    public void error(CharSequence charSequence) {
        super.error(charSequence);
        log.error(charSequence.toString());
    }

    public void error(CharSequence charSequence, Throwable throwable) {
        super.error(charSequence, throwable);
        log.error(charSequence.toString(), throwable);
    }

    public void error(Throwable throwable) {
        super.error(throwable);
        log.error("", throwable);
    }

    @Override
    public String getBuffer() {
        return super.getBuffer();
    }

    public static LogLevel determinateLogLevel(Logger log) {
        if (log.isTraceEnabled()) {
            return  LogLevel.TRACE;
        } else if (log.isDebugEnabled()) {
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

}

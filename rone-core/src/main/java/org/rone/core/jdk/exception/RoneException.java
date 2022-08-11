package org.rone.core.jdk.exception;

/**
 * 自定义异常，应提供至少两个构造器（默认无参的，默认参数为异常说明的）
 * @author rone
 */
public class RoneException extends Exception {

    public RoneException() {
    }

    public RoneException(String message) {
        super(message);
    }

    public RoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoneException(Throwable cause) {
        super(cause);
    }

    public RoneException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
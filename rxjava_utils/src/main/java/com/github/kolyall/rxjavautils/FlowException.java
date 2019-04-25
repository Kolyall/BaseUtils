package com.github.kolyall.rxjavautils;

public class FlowException extends RuntimeException {
    public FlowException() {
    }

    public FlowException(String message) {
        super(message);
    }

    public FlowException(Throwable cause) {
        super(cause);
    }
}

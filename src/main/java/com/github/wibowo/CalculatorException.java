package com.github.wibowo;

public class CalculatorException extends RuntimeException {

    public CalculatorException(final String message) {
        super(message);
    }

    public CalculatorException(final String message,
                               final Throwable cause) {
        super(message, cause);
    }
}

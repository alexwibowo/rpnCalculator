package com.github.wibowo;

public class CalculatorException extends RuntimeException {

    private static CalculatorException DIVISION_BY_ZERO = new CalculatorException("/ by zero");

    public static CalculatorException divisionByZero() {
        return DIVISION_BY_ZERO;
    }

    public static CalculatorException invalidNumber(final String numberAsString) {
        return new CalculatorException(String.format("Unable to convert [%s] into a number.", numberAsString));
    }

    private CalculatorException(final String message) {
        super(message);
    }

}

package com.github.wibowo;

public class CalculatorException extends RuntimeException {

    private static CalculatorException DIVISION_BY_ZERO = new CalculatorException("/ by zero");

    public static CalculatorException divisionByZero() {
        return DIVISION_BY_ZERO;
    }

    /**
     * Indicates failure to convert the string number into a number
     * @param numberAsString String to be converted into number
     * @return instance of this exception
     */
    public static CalculatorException invalidNumber(final String numberAsString) {
        return new CalculatorException(String.format("Unable to convert [%s] into a number.", numberAsString));
    }

    private CalculatorException(final String message) {
        super(message);
    }

}

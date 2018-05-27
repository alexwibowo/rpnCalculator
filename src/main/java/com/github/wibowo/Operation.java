package com.github.wibowo;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.EnumSet.complementOf;
import static java.util.EnumSet.of;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum Operation {
    Plus("+",2, true){
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            verifyArguments(this, arguments);
            final RealNumber firstNumber = arguments.get(0);
            final RealNumber secondNumber = arguments.get(1);
            return RealNumber.of(firstNumber.eval().add(secondNumber.eval()));
        }

    },
    Minus("-", 2, true){
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            verifyArguments(this, arguments);
            final RealNumber firstNumber = arguments.get(0);
            final RealNumber secondNumber = arguments.get(1);
            return RealNumber.of(secondNumber.eval().subtract(firstNumber.eval()));
        }
    },
    Multiply("*", 2, true){
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            verifyArguments(this, arguments);
            final RealNumber firstNumber = arguments.get(0);
            final RealNumber secondNumber = arguments.get(1);
            return RealNumber.of(secondNumber.eval().multiply(firstNumber.eval()));
        }
    },
    Divide("/", 2, true){
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            verifyArguments(this, arguments);
            final BigDecimal firstNumber = arguments.get(0).eval();
            final BigDecimal secondNumber = arguments.get(1).eval();
            if (firstNumber.equals(BigDecimal.ZERO)) {
                throw CalculatorException.divisionByZero();
            }
            return RealNumber.of(secondNumber.divide(firstNumber, RealNumber.DEFAULT_SCALE, RoundingMode.HALF_EVEN));
        }
    },
    Sqrt("sqrt", 1, true) {
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            verifyArguments(this, arguments);
            final RealNumber firstNumber = arguments.get(0);
            return RealNumber.of(BigDecimalMath.sqrt(firstNumber.eval(), MathContext.DECIMAL64));
        }
    },
    Undo("undo",0, false),
    Clear("clear",0, false),
    Push("", 1, false) {
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            verifyArguments(this, arguments);
            return arguments.get(0);
        }

        @Override
        public boolean matches(String operationString) {
            return operationString.matches("^[-+]?[0-9]*\\.?[0-9]+$");
        }
    },
    UnsupportedOperation("", 0, false){
        @Override
        public boolean matches(String operationString) {
            return false;
        }
    },
    Help("?", 0, false),
    Quit("quit", 0, false);

    private static void verifyArguments(final Operation operation,
                                        final List<RealNumber> arguments) {
        Preconditions.checkArgument(arguments.size() == operation.numArguments,
                String.format("%s operation requires %d arguments. Received: %s", operation.name(), operation.numArguments, arguments)
        );
    }

    static final Map<String, Operation> dictionary = complementOf(of(Operation.Push, Operation.UnsupportedOperation))
                .stream()
                .collect(toMap(Operation::command, identity()));

    /**
     * String representation of the command (to match what user will give)
     */
    final String command;

    /**
     * Number of arguments required to execute the command
     */
    final int numArguments;

    /**
     * <code>true</code> means that we need to push the argument back to the stack on 'undo' operation
     */
    final boolean pushArgumentsOnUndo;

    Operation(final String command,
              final int numArguments,
              final boolean pushArgumentsOnUndo) {
        this.command = command;
        this.numArguments = numArguments;
        this.pushArgumentsOnUndo = pushArgumentsOnUndo;
    }

    /**
     * Find operation for a given String.
     *
     * @param operationString operation to be searched for
     * @return Operation if found, {@link Operation#UnsupportedOperation} otherwise.
     */
    public static @NotNull Operation findOperation(final @NotNull String operationString) {
        final Operation operation = dictionary.get(operationString.toLowerCase());
        if (operation != null) {
            return operation;
        } else if (Push.matches(operationString)) {
            return Push;
        } else {
            return UnsupportedOperation;
        }
    }

    public String command() {
        return command;
    }

    public RealNumber evaluate(final List<RealNumber> arguments) {
        throw new UnsupportedOperationException(String.format("Should not try to evaluate %s operation. This is most likely a programming error.", this.name()));
    }

    boolean matches(final String operationString){
        return Objects.equals(operationString, command);
    }
}

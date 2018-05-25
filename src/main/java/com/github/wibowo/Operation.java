package com.github.wibowo;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.EnumSet.complementOf;
import static java.util.EnumSet.of;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum Operation {
    Plus("+",2){
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            Preconditions.checkArgument(arguments.size() == 2, "Plus operation requires two arguments. Received: {}", arguments);
            final RealNumber firstNumber = arguments.get(0);
            final RealNumber secondNumber = arguments.get(1);
            return RealNumber.of(firstNumber.eval().add(secondNumber.eval()));
        }

    },
    Sqrt("sqrt", 1) {
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            throw new RuntimeException("Not implemented yet");

        }
    },
    Undo("undo",0) {
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            throw new UnsupportedOperationException("Should not try to evaluate Undo operation. This is most likely a programming error.");
        }
    },
    Clear("clear",0) {
        @Override
        public RealNumber evaluate(List<RealNumber> arguments) {
            throw new RuntimeException("Not implemented yet");

        }
    },
    Push("",0) {
        @Override
        public RealNumber evaluate(final List<RealNumber> arguments) {
            Preconditions.checkArgument(arguments.size() == 1,
                    "Unexpected argument to push operation. Push operation expects only 1 argument. Received: {}", arguments);
            return arguments.get(0);
        }

        @Override
        public boolean matches(String operationString) {
            return operationString.matches("^[-+]?[0-9]*\\.?[0-9]+$");
        }
    };

    static final Map<String, Operation> dictionary = complementOf(of(Operation.Push))
                .stream()
                .collect(toMap(Operation::command, identity()));

    final String command;
    final int numArguments;

    Operation(final String command,
              final int numArguments) {
        this.command = command;
        this.numArguments = numArguments;
    }

    public String command() {
        return command;
    }

    public abstract RealNumber evaluate(final List<RealNumber> arguments);

    public boolean matches(final String operationString){
        return Objects.equals(operationString, command);
    }
}

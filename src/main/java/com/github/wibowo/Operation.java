package com.github.wibowo;

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
        public RealNumber evaluate(List<RealNumber> arguments) {
            throw new RuntimeException("Not implemented yet");
        }

    },
    Sqrt("sqrt", 1) {
        @Override
        public RealNumber evaluate(List<RealNumber> arguments) {
            throw new RuntimeException("Not implemented yet");

        }
    },
    Undo("undo",0) {
        @Override
        public RealNumber evaluate(List<RealNumber> arguments) {
            throw new RuntimeException("Not implemented yet");

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
        public RealNumber evaluate(List<RealNumber> arguments) {
            throw new UnsupportedOperationException("Should not call evaluate on Push command. This might be a programming issue.");
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

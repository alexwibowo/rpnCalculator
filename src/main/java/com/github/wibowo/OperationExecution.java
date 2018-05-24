package com.github.wibowo;

import com.google.common.collect.Lists;

import java.util.List;

public final class OperationExecution {

    private final Operation operation;

    private final List<RealNumber> arguments;

    private final RealNumber result;

    public OperationExecution(final Operation operation,
                              final List<RealNumber> arguments) {
        this.operation = operation;
        this.arguments = arguments;
        this.result = operation.evaluate(this.arguments);
    }

    public OperationExecution(final Operation operation,
                              final RealNumber arguments) {
        this(operation, Lists.newArrayList(arguments));
    }

    public Operation getOperation() {
        return operation;
    }

    public List<RealNumber> getArguments() {
        return arguments;
    }

    public RealNumber getResult() {
        return result;
    }
}

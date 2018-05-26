package com.github.wibowo;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A snapshot of operation execution. Having this snapshot allows us to rollback with exact
 * parameter used to perform the execution. E.g. for {@link Operation#Undo} or when we want to rollback
 * due to arithmetic exception.
 */
public final class OperationExecution {

    /**
     * Operation that was attempted
     */
    private final Operation operation;

    /**
     *  Arguments required by the operation
     */
    private final List<RealNumber> arguments;

    /**
     * Result of the operation execution
     */
    private final RealNumber result;

    public OperationExecution(final @NotNull Operation operation,
                              final @NotNull List<RealNumber> arguments) {
        this.operation = operation;
        this.arguments = arguments;
        this.result = operation.evaluate(this.arguments);
    }

    public OperationExecution(final @NotNull Operation operation,
                              final @NotNull RealNumber arguments) {
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

    @Override
    public String toString() {
        return result.toString();
    }
}

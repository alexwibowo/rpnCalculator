package com.github.wibowo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public final class RPNCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RPNCalculator.class);
    public static final String BANNER_MESSAGE = "RPN Calculator";

    public static void main(final String[] args) throws CalculatorException {
        LOGGER.info(BANNER_MESSAGE);
        final Scanner scanner = new Scanner(System.in);
        final RPNStack<OperationExecution> operationExecutions = new RPNStack<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (!line.trim().isEmpty()) {
                try {
                    processLine(operationExecutions, line);
                } finally {
                    LOGGER.info("{}", operationExecutions);
                }
            }
        }
    }

    private static OperationExecutionStatus processLine(final RPNStack<OperationExecution> operationExecutions,
                                                        final String line) {
        final String[] tokens = line.split("\\s+");
        OperationExecutionStatus currentStatus = OperationExecutionStatus.Success;
        for (int i = 0; i < tokens.length; i++) {
            final String token = tokens[i];
            final Operation operation = findOperation(token);
            if (operation == Operation.Push) {
                operationExecutions.push(new OperationExecution(operation, RealNumber.of(token)));
            } else if (operation == Operation.Clear) {
                currentStatus = performClear(operationExecutions);
            } else if (operation == Operation.Undo) {
                currentStatus = performUndo(operationExecutions);
            } else if (operation == Operation.UnsupportedOperation) {
                LOGGER.warn("operator {} (position: {}): unsupported operation", token, line.indexOf(token));
            } else {
                final int operationIndex = i;
                currentStatus = performOperation(operationExecutions, operation, new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        return CommandStringHelper.findTokenPositionInOriginalLine(line, operationIndex);
                    }
                });
            }

            if (currentStatus == OperationExecutionStatus.Failed) {
                return currentStatus;
            }
        }
        return currentStatus;
    }

    private static OperationExecutionStatus performClear(final RPNStack<OperationExecution> operationExecutions) {
        operationExecutions.clear();
        return OperationExecutionStatus.Success;
    }

    private static OperationExecutionStatus performOperation(final RPNStack<OperationExecution> operationExecutions,
                                         final Operation operation,
                                         final Supplier<Integer> operationPositionFinder) {
        if (operationExecutions.size() < operation.numArguments) {
            final Integer operationPosition = operationPositionFinder.get();
            LOGGER.warn("operator {} (position: {}): insufficient parameters", operation.command(), operationPosition);
            return OperationExecutionStatus.Failed;
        } else {
            final Iterable<OperationExecution> executions = operationExecutions.pop(operation.numArguments);
            final List<RealNumber> arguments = stream(executions.spliterator(), false)
                    .map(OperationExecution::getResult)
                    .collect(toList());
            operationExecutions.push(new OperationExecution(operation, arguments));
            return OperationExecutionStatus.Success;
        }
    }

    private static OperationExecutionStatus performUndo(final RPNStack<OperationExecution> operationExecutions) {
        final OperationExecution pop = operationExecutions.pop();
        if (pop.getOperation().pushArgumentsOnUndo) {
            final List<RealNumber> arguments = pop.getArguments();
            for (int i = arguments.size()-1; i >= 0; i--) {
                final OperationExecution operationExecution = new OperationExecution(Operation.Push, arguments.get(i));
                operationExecutions.push(operationExecution);
            }
        }
        return OperationExecutionStatus.Success;
    }

    private static Operation findOperation(final String operationString) throws CalculatorException {
        final Operation operation = Operation.dictionary.get(operationString);
        if (operation != null) {
            return operation;
        } else if (Operation.Push.matches(operationString)) {
            return Operation.Push;
        } else {
            return Operation.UnsupportedOperation;
        }
    }
}

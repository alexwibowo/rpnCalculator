package com.github.wibowo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public final class RPNCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RPNCalculator.class);
    public static final String BANNER_MESSAGE = "RPN Calculator";

    public static void main(final String[] args) throws CalculatorException {
        LOGGER.info(BANNER_MESSAGE);
        final Scanner scanner = new Scanner(System.in, "UTF-8");

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

    private static CommandExecutionStatus processLine(final RPNStack<OperationExecution> operationExecutions,
                                                      final String line) {
        final String[] tokens = line.trim().split("\\s+");
        CommandExecutionStatus currentStatus = CommandExecutionStatus.Success;
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
                        return TokenPositionFinder.findTokenPositionInOriginalLine(line, operationIndex);
                    }
                });
            }

            if (currentStatus == CommandExecutionStatus.Failed) {
                return currentStatus;
            }
        }
        return currentStatus;
    }

    private static CommandExecutionStatus performClear(final RPNStack<OperationExecution> operationExecutions) {
        operationExecutions.clear();
        return CommandExecutionStatus.Success;
    }

    private static CommandExecutionStatus performOperation(final RPNStack<OperationExecution> operationExecutions,
                                                           final Operation operation,
                                                           final Supplier<Integer> operationPositionFinder) {
        if (operationExecutions.size() < operation.numArguments) {
            final Integer operationPosition = operationPositionFinder.get();
            LOGGER.warn("operator {} (position: {}): insufficient parameters", operation.command(), operationPosition);
            return CommandExecutionStatus.Failed;
        } else {
            final Iterable<OperationExecution> executions = operationExecutions.pop(operation.numArguments);
            final List<RealNumber> arguments = stream(executions.spliterator(), false)
                    .map(OperationExecution::getResult)
                    .collect(toList());
            try {
                operationExecutions.push(new OperationExecution(operation, arguments));
                return CommandExecutionStatus.Success;
            } catch (final Exception exception) {
                // rollback first
                rollback(operationExecutions, arguments);

                // then return error
                final Integer operationPosition = operationPositionFinder.get();
                LOGGER.warn("operator {} (position: {}): operation execution failed due to: [{}]", operation.command(), operationPosition, exception.getMessage());
                return CommandExecutionStatus.Failed;
            }
        }
    }

    private static CommandExecutionStatus performUndo(final RPNStack<OperationExecution> operationExecutions) {
        final OperationExecution pop = operationExecutions.pop();
        if (pop.getOperation().pushArgumentsOnUndo) {
            final List<RealNumber> arguments = pop.getArguments();
            rollback(operationExecutions, arguments);
        }
        return CommandExecutionStatus.Success;
    }

    private static void rollback(final RPNStack<OperationExecution> operationExecutions,
                                 final List<RealNumber> arguments) {
        for (int i = arguments.size()-1; i >= 0; i--) {
            final OperationExecution operationExecution = new OperationExecution(Operation.Push, arguments.get(i));
            operationExecutions.push(operationExecution);
        }
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

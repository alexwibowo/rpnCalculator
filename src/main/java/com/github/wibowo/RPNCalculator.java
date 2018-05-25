package com.github.wibowo;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class RPNCalculator {

    public static void main(String[] args) throws CalculatorException {
        System.out.println("RPN Calculator");

        final Scanner scanner = new Scanner(System.in);
        final RPNStack<OperationExecution> operationExecutions = new RPNStack<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (!line.trim().isEmpty()) {
                processLine(operationExecutions, line);
                System.out.println(operationExecutions.toString());
            }
        }

    }

    private static void processLine(final RPNStack<OperationExecution> operatorStack,
                                    final String line) {
        for (final String lineComponent : line.split("\\s+")) {
            final Operation operation = findOperation(lineComponent);
            if (operation == Operation.Push) {
                operatorStack.push(new OperationExecution(operation, RealNumber.of(lineComponent)));
            } else if (operation == Operation.Clear) {

            } else if (operation == Operation.Undo) {
                final OperationExecution pop = operatorStack.pop();
                final List<RealNumber> arguments = pop.getArguments();
                for (int i = arguments.size()-1; i >= 0; i--) {
                    final OperationExecution operationExecution = new OperationExecution(Operation.Push, arguments.get(i));
                    operatorStack.push(operationExecution);
                }
            } else {
                final Iterable<OperationExecution> executions = operatorStack.pop(operation.numArguments);
                final List<RealNumber> arguments = StreamSupport.stream(executions.spliterator(), false)
                        .map(OperationExecution::getResult)
                        .collect(Collectors.toList());
                final OperationExecution operationExecution = new OperationExecution(operation, arguments);
                operatorStack.push(operationExecution);
            }
        }
    }

    private static Operation findOperation(final String operationString) throws CalculatorException {
        final Operation operation = Operation.dictionary.get(operationString);
        if (operation != null) {
            return operation;
        } else {
            if (Operation.Push.matches(operationString)) {
                return Operation.Push;
            }
        }
        throw new CalculatorException(String.format("Unsupported operation [%s]", operationString));
    }
}

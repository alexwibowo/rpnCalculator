package com.github.wibowo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class RPNCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RPNCalculator.class);

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
                for (RealNumber argument : arguments) {
                    operatorStack.push(new OperationExecution(Operation.Push, argument));
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

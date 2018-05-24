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
        LOGGER.info("RPN Calculator");

        final Scanner scanner = new Scanner(System.in);
        final RPNStack<OperationExecution> operatorStack = new RPNStack<>();

        while (scanner.hasNextLine()) {
            final String[] split = scanner.nextLine().split("\\s+");
            for (final String s : split) {
                final Operation operation = findOperation(s);
                if (operation == Operation.Push) {
                    operatorStack.push(new OperationExecution(operation, RealNumber.of(s)));
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
            System.out.println(operatorStack.toString());
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

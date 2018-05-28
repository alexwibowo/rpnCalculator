# RPN Calculator

[![Build Status](https://travis-ci.org/alexwibowo/rpnCalculator.svg?branch=master)](https://travis-ci.org/alexwibowo/rpnCalculator)
[![BCH compliance](https://bettercodehub.com/edge/badge/alexwibowo/rpnCalculator?branch=master)](https://bettercodehub.com/)

The calculator has a stack that can contain real numbers.
* The calculator waits for user input and expects to receive strings containing whitespace separated lists of numbers and operators.
* Numbers are pushed on to the stack. Operators operate on numbers that are on the stack.
* Available operators are +, -, *, /, sqrt, undo, clear
* Operators pop their parameters off the stack, and push their results back onto the stack.
* The *clear* operator removes all items from the stack.
* The *undo* operator undoes the previous operation. *undo undo* will undo the previous two operations.
* *sqrt* performs a square root on the top item from the stack
* The *+*, *-*, *, */* operators perform addition, subtraction, multiplication and division respectively on the top two items from the stack.
* After processing an input string, the calculator displays the current contents of the stack as a space-separated list.
* Numbers should be stored on the stack to at least 15 decimal places of precision, but displayed to 10 decimal places (or less if it causes no loss of precision).
* All numbers should be formatted as plain decimal strings (ie. no engineering formatting).
* If an operator cannot find a sufficient number of parameters on the stack, a warning is displayed:

   operator *operator* (position: *pos*): insufficient parameters

* After displaying the warning, all further processing of the string terminates and the current state of the stack is displayed.


## Requirements

* Gradle 4.7
* Java 8

## Gradle Tasks

* *gradle shadowJar*: to produce uber jar. You should be able to find rpnCalculator-all.jar inside the build/libs directory
* *gradle clean test check*: to run test along with static analysis of the code
* *gradle clean test jacocoTestReport*: to produce coverage test report

## Design

A command given by user consists of multiple *token*. Each token is wrapped into *OperationExecution* and pushed into the *RPNStack*.

An *OperationExecution* encapsulates:
1. Operation type
2. Arguments to the operation
3. Result of the operation execution

E.g. given command "5 2 +", this will result in three *OperationExecution* being pushed into the stack.
1. [PUSH | <5> | <5>], with stack of [5]
2. [PUSH | <2> | <2>], with stack of [5 2]
3. [PLUS | <2, 5> | <10>], with stack of [10]

Say the next operation given by user is *Undo*, then we simply pop the stack, which is [PLUS | <2, 5> | <10>].
We get the arguments, and pushed them back into the stack. 

The alternative design is to have "revert" method on each *Operation*. This way we dont need to store all arguments. E.g. to 
revert a plus, we simply subtract. To revert a division, we multiply. However, there is a drawback with this approach. With the division, for example,
we cant get the **exact** stack state prior to executing the operation (due to number rounding).

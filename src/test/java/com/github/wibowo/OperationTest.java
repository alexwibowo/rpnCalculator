package com.github.wibowo;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    void test_dictionary() {
        assertNotNull(Operation.dictionary.get("+"));
        assertNotNull(Operation.dictionary.get("-"));
        assertNotNull(Operation.dictionary.get("*"));
        assertNotNull(Operation.dictionary.get("/"));
        assertNotNull(Operation.dictionary.get("sqrt"));
        assertNotNull(Operation.dictionary.get("undo"));
        assertNotNull(Operation.dictionary.get("clear"));
    }

    @Test
    void test_operation_matching() {
        assertTrue(Operation.Plus.matches("+"));
        assertTrue(Operation.Minus.matches("-"));
        assertTrue(Operation.Multiply.matches("*"));
        assertTrue(Operation.Divide.matches("/"));
        assertTrue(Operation.Sqrt.matches("sqrt"));
        assertTrue(Operation.Undo.matches("undo"));
        assertTrue(Operation.Clear.matches("clear"));
    }

    @Test
    void test_number_matching() {
        assertTrue(Operation.Push.matches("42"));
        assertTrue(Operation.Push.matches("42.123456"));
        assertTrue(Operation.Push.matches("-42"));
        assertTrue(Operation.Push.matches("-42.123456"));
        assertTrue(Operation.Push.matches("0"));
        assertTrue(Operation.Push.matches("0.0009"));
    }

    @Test
    void test_plus() {
        assertThat(Operation.Plus.evaluate(TestHelper.getArguments("2", "3")))
            .isEqualTo(RealNumber.of("5"));
        assertThat(Operation.Plus.evaluate(TestHelper.getArguments("2", "3")))
                .isEqualTo(Operation.Plus.evaluate(TestHelper.getArguments("3", "2")));
    }

    @Test
    void plus_requires_two_arguments() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Operation.Plus.evaluate(TestHelper.getArguments("2")));
        assertThat(exception.getMessage())
                .isEqualTo("Plus operation requires 2 arguments. Received: [2]");
    }

    @Test
    void test_sqrt() {
        assertThat(Operation.Sqrt.evaluate(TestHelper.getArguments("2")))
                .isEqualTo(RealNumber.of("1.4142135623"));
        assertThat(Operation.Sqrt.evaluate(TestHelper.getArguments("9")))
                .isEqualTo(RealNumber.of("3"));
    }

    @Test
    void sqrt_requires_one_arguments() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Operation.Sqrt.evaluate(TestHelper.getArguments("2","4")));
        assertThat(exception.getMessage())
                .isEqualTo("Sqrt operation requires 1 arguments. Received: [2, 4]");
    }

    @Test
    void test_minus() {
        assertThat(Operation.Minus.evaluate(TestHelper.getArguments("2", "5")))
                .isEqualTo(RealNumber.of("3"));
        assertThat(Operation.Minus.evaluate(TestHelper.getArguments("5", "2")))
                .isEqualTo(RealNumber.of("-3"));
    }

    @Test
    void minus_requires_two_arguments() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Operation.Minus.evaluate(TestHelper.getArguments("2")));
        assertThat(exception.getMessage())
                .isEqualTo("Minus operation requires 2 arguments. Received: [2]");
    }

    @Test
    void test_multiply() {
        assertThat(Operation.Multiply.evaluate(TestHelper.getArguments("2", "3")))
                .isEqualTo(RealNumber.of("6"));
        assertThat(Operation.Multiply.evaluate(TestHelper.getArguments("3", "2")))
                .isEqualTo(Operation.Multiply.evaluate(TestHelper.getArguments("2", "3")));
        assertThat(Operation.Multiply.evaluate(TestHelper.getArguments("-2", "3")))
                .isEqualTo(RealNumber.of("-6"));
        assertThat(Operation.Multiply.evaluate(TestHelper.getArguments("0", "3")))
                .isEqualTo(RealNumber.of("0"));
        assertThat(Operation.Multiply.evaluate(TestHelper.getArguments("-2.12345", "5")))
                .isEqualTo(RealNumber.of("-10.61725"));
    }

    @Test
    void multiply_requires_two_arguments() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Operation.Multiply.evaluate(TestHelper.getArguments("2")));
        assertThat(exception.getMessage())
                .isEqualTo("Multiply operation requires 2 arguments. Received: [2]");
    }

    @Test
    void test_divide() {
        assertThat(Operation.Divide.evaluate(TestHelper.getArguments("4", "42")))
                .isEqualTo(RealNumber.of("10.5"));
    }

    @Test
    void test_divide_by_zero() {
        final ArithmeticException arithmeticException = assertThrows(ArithmeticException.class, () -> Operation.Divide.evaluate(TestHelper.getArguments("0", "4")));
        assertThat(arithmeticException.getMessage())
                .isEqualTo("/ by zero");
    }

    @Test
    void undo_cant_be_evaluated() {
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> Operation.Undo.evaluate(TestHelper.getArguments("4", "42")));
        assertThat(exception.getMessage()).isEqualTo("Should not try to evaluate Undo operation. This is most likely a programming error.");
    }

    @Test
    void clear_cant_be_evaluated() {
        final UnsupportedOperationException exception =  assertThrows(UnsupportedOperationException.class, () -> Operation.Clear.evaluate(TestHelper.getArguments("4", "42")));
        assertThat(exception.getMessage()).isEqualTo("Should not try to evaluate Clear operation. This is most likely a programming error.");
    }

    @Test
    void UnsupportedOperation_cant_be_evaluated() {
        final UnsupportedOperationException exception =  assertThrows(UnsupportedOperationException.class, () -> Operation.UnsupportedOperation.evaluate(TestHelper.getArguments("4", "42")));
        assertThat(exception.getMessage()).isEqualTo("Should not try to evaluate UnsupportedOperation operation. This is most likely a programming error.");
    }

    @Test
    void UnsupportedOperation_should_not_match_any_existing_operation() {
        assertThat(Operation.UnsupportedOperation.matches("+")).isFalse();
        assertThat(Operation.UnsupportedOperation.matches("-")).isFalse();
        assertThat(Operation.UnsupportedOperation.matches("*")).isFalse();
        assertThat(Operation.UnsupportedOperation.matches("/")).isFalse();
        assertThat(Operation.UnsupportedOperation.matches("sqrt")).isFalse();
        assertThat(Operation.UnsupportedOperation.matches("undo")).isFalse();
        assertThat(Operation.UnsupportedOperation.matches("clear")).isFalse();

    }

    @Test
    void whitespace_should_be_trimmed_for_number() {
        assertFalse(Operation.Push.matches("   0.0009    "));
    }
}
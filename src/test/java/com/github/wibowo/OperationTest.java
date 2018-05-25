package com.github.wibowo;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    void test_dictionary() {
        assertNotNull(Operation.dictionary.get("+"));
        assertNotNull(Operation.dictionary.get("-"));
        assertNotNull(Operation.dictionary.get("sqrt"));
        assertNotNull(Operation.dictionary.get("undo"));
        assertNotNull(Operation.dictionary.get("clear"));
    }

    @Test
    void test_operation_matching() {
        assertTrue(Operation.Plus.matches("+"));
        assertTrue(Operation.Minus.matches("-"));
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
    void whitespace_should_be_trimmed_for_number() {
        assertFalse(Operation.Push.matches("   0.0009    "));
    }
}
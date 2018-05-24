package com.github.wibowo;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationTest {

    @Test
    void test_dictionary() {
        assertNotNull(Operation.dictionary.get("+"));
        assertNotNull(Operation.dictionary.get("sqrt"));
        assertNotNull(Operation.dictionary.get("undo"));
        assertNotNull(Operation.dictionary.get("clear"));
    }

    @Test
    void test_operation_matching() {
        assertTrue(Operation.Plus.matches("+"));
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
    void whitespace_should_be_trimmed_for_number() {
        assertFalse(Operation.Push.matches("   0.0009    "));
    }
}
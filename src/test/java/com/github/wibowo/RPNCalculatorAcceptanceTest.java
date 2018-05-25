package com.github.wibowo;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class RPNCalculatorAcceptanceTest {

    @Test
    void push_single_number_to_stack() {
        givenInput("5\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 5");
    }

    @Tag("ExampleTest")
    @DisplayName("Example 1")
    @Test
    void push_multiple_number_to_stack() {
        givenInput("5 2\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 5 2");
    }

    @Test
    void test_plus_inlined() {
        givenInput("5 2 +\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 7");
    }

    @Test
    void test_plus_on_current_stack() {
        givenInput(
                "5 2\n" +
                "+\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(2);
        assertThat(output[0]).isEqualTo("stack: 5 2");
        assertThat(output[1]).isEqualTo("stack: 7");
    }

    @Test
    void test_undo() {
        givenInput("" +
                "5 2 +\n" +
                "undo\n");

        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(2);
        assertThat(output[0]).isEqualTo("stack: 7");
        assertThat(output[1]).isEqualTo("stack: 5 2");
    }

    @Test
    void test_squareRoot() {
        givenInput("" +
                "2 sqrt\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 1.4142135623");
    }

    @Test
    void test_clear() {
        givenInput("5 2 +\n" +
                    "clear\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(2);
        assertThat(output[0]).isEqualTo("stack: 7");
        assertThat(output[1]).isEqualTo("stack: ");
    }

    @Tag("ExampleTest")
    @DisplayName("Example 2")
    @Test
    void test_operations_mixed_with_clear() {
        givenInput("" +
                "2 sqrt\n" +
                "clear 9 sqrt\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(2);
        assertThat(output[0]).isEqualTo("stack: 1.4142135623");
        assertThat(output[1]).isEqualTo("stack: 3");
    }


    @NotNull
    private String[] executeAndGetOutput() {
        final ByteArrayOutputStream outputStream = setupOutput();
        RPNCalculator.main(new String[]{});
        final String actual = outputStream.toString();
        final String[] split = actual.split("\\r?\\n");
        return Arrays.copyOfRange(split, 1, split.length);
    }

    @NotNull
    private ByteArrayOutputStream setupOutput() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        return baos;
    }

    private void givenInput(final String input) {
        System.setIn(
                new ByteArrayInputStream((input + "\n") // add new line to end program
                        .getBytes())
        );
    }

}

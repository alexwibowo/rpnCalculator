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

    @Test
    void test_minus() {
        givenInput("5 2 -\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 3");
    }

    @Tag("ExampleTest")
    @DisplayName("Example 3")
    @Test
    void test_minus_sample_2() {
        givenInput("" +
                "5 2 -\n" +
                "3 -\n"+
                "clear\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(3);
        assertThat(output[0]).isEqualTo("stack: 3");
        assertThat(output[1]).isEqualTo("stack: 0");
        assertThat(output[2]).isEqualTo("stack: ");
    }

    @Test
    void test_undo_push_number() {
        givenInput("" +
                "5 4\n" +
                "undo\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(2);
        assertThat(output[0]).isEqualTo("stack: 5 4");
        assertThat(output[1]).isEqualTo("stack: 5");
    }

    @Tag("ExampleTest")
    @DisplayName("Example 4")
    @Test
    void test_complex_undo() {
        givenInput("" +
                "5 4 3 2\n" +
                "undo undo *\n"+
                "5 *\n" +
                "undo\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(4);
        assertThat(output[0]).isEqualTo("stack: 5 4 3 2");
        assertThat(output[1]).isEqualTo("stack: 20");
        assertThat(output[2]).isEqualTo("stack: 100");
        assertThat(output[3]).isEqualTo("stack: 20 5");
    }

    @Tag("ExampleTest")
    @DisplayName("Example 5")
    @Test
    void test_division() {
        givenInput("" +
                "7 12 2 /\n" +
                "*\n"+
                "4 /\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(3);
        assertThat(output[0]).isEqualTo("stack: 7 6");
        assertThat(output[1]).isEqualTo("stack: 42");
        assertThat(output[2]).isEqualTo("stack: 10.5");
    }

    @Test
    void should_not_print_in_scientific_notation() {
        givenInput("" +
                "1000000 10 *\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 10000000");
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

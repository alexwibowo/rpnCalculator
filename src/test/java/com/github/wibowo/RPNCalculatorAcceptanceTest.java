package com.github.wibowo;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class RPNCalculatorAcceptanceTest {

    @Test
    void push_single_number_to_stack() throws Exception {
        givenInput("5\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 5");
    }

    @Test
    void push_multiple_number_to_stack() throws Exception {
        givenInput("5 2\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 5 2");
    }

    @Test
    void test_plus_inlined() throws Exception {
        givenInput("5 2 +\n");
        final String[] output = executeAndGetOutput();
        assertThat(output).containsExactly("stack: 7");
    }

    @Test
    void test_plus_on_current_stack() throws Exception {
        givenInput(
                "5 2\n" +
                "+\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(2);
        assertThat(output[0]).isEqualTo("stack: 5 2");
        assertThat(output[1]).isEqualTo("stack: 7");
    }

    @Test
    void test_undo() throws Exception {
        givenInput("" +
                "5 2 plus\n" +
                "undo\n");

        final String[] output = executeAndGetOutput();
        assertThat(output[0]).isEqualTo("stack: 7");
        assertThat(output[1]).isEqualTo("stack: 5 2");
    }


    @NotNull
    private String[] executeAndGetOutput() throws Exception {
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

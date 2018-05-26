package com.github.wibowo;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class RPNCalculatorAcceptanceTest {

    private ListAppender listAppender;
    private LoggerConfig rootLoggerConfig;

    @BeforeEach
    void setUp() {
        final org.apache.logging.log4j.core.LoggerContext loggerContext = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        final Configuration configuration = loggerContext.getConfiguration();
        rootLoggerConfig = configuration.getLoggerConfig("");

        listAppender = new ListAppender("testAppender");
        listAppender.start();
        rootLoggerConfig.addAppender(listAppender, Level.ALL, null);
    }

    @AfterEach
    void tearDown() {
        listAppender.stop();
        rootLoggerConfig.removeAppender("testAppender");
    }

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

    @Tag("ExampleTest")
    @DisplayName("Example 6")
    @Test
    void test_negative_number() {
        givenInput("" +
                "1 2 3 4 5\n" +
                "*\n"+
                "clear 3 4 -\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(3);
        assertThat(output[0]).isEqualTo("stack: 1 2 3 4 5");
        assertThat(output[1]).isEqualTo("stack: 1 2 3 20");
        assertThat(output[2]).isEqualTo("stack: -1");
    }

    @Tag("ExampleTest")
    @DisplayName("Example 7")
    @Test
    void test_multiple_multiplication() {
        givenInput("" +
                "1 2 3 4 5\n" +
                "* * * *\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(2);
        assertThat(output[0]).isEqualTo("stack: 1 2 3 4 5");
        assertThat(output[1]).isEqualTo("stack: 120");
    }

    @Tag("ExampleTest")
    @DisplayName("Example 8")
    @Test
    void test_invalid() {
        givenInput("" +
                "1 2 3 * 5 + * * 6 5\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(2);
        assertThat(output[0]).isEqualTo("operator * (position: 15): insufficient parameters");
        assertThat(output[1]).isEqualTo("stack: 11");
    }

    @Test
    void test_division_by_zero() {
        givenInput("" +
                "1 2 3 0 / + * * 6 5\n" +
                "+ 5\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(3);
        assertThat(output[0]).isEqualTo("operator / (position: 9): operation execution failed due to: [/ by zero]");
        assertThat(output[1]).isEqualTo("stack: 1 2 3 0");
        assertThat(output[2]).isEqualTo("stack: 1 2 3 5");

    }

    @Test
    void test_invalid_operation() {
        givenInput("" +
                "1 2 (\n" +
                "5 +\n");
        final String[] output = executeAndGetOutput();
        assertThat(output.length).isEqualTo(3);
        assertThat(output[0]).isEqualTo("operator ( (position: 4): unsupported operation");
        assertThat(output[1]).isEqualTo("stack: 1 2");
        assertThat(output[2]).isEqualTo("stack: 1 7");
    }


    @NotNull
    private String[] executeAndGetOutput() {
        RPNCalculator.main(new String[]{});

        final List<LogEvent> events = listAppender.getEvents();
        return events.stream().map(LogEvent::getMessage).map(Message::getFormattedMessage)
                .filter(message -> !Objects.equals(message, RPNCalculator.BANNER_MESSAGE))
                .toArray(String[]::new);

    }

    private void givenInput(final String input)  {
        try {
            System.setIn(
                    new ByteArrayInputStream((input + "\n") // add new line to end program
                            .getBytes("UTF-8"))
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}

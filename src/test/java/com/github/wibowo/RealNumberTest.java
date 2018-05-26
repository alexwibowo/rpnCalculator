package com.github.wibowo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RealNumberTest {

    @Test
    void test_equality() {
        assertThat(RealNumber.of("5"))
            .isEqualTo(RealNumber.of("5"));
        assertThat(RealNumber.of("5"))
                .isEqualTo(RealNumber.of("   5   "));
    }

    @Test
    void strip_trailing_zeroes() {
        assertThat(RealNumber.of("5.0000000000000"))
                .isEqualTo(RealNumber.of("5"));
    }

    @Test
    void eval_value_from_bigDecimal() {
        final BigDecimal source = new BigDecimal("42.12345");
        assertThat(RealNumber.of(source).eval())
                .isEqualTo(source);
    }

    @Test
    void eval_value_with_fractions_and_default_scaling() {
        assertThat(RealNumber.of("42.5678956789567895678956789").eval())
                .isEqualTo(new BigDecimal("42.5678956789"));
    }

    @Test
    void eval_value_with_fractions_and_specific_scaling() {
        assertThat(RealNumber.of("42.567895678956789567895678956789", 20).eval())
                .isEqualTo(new BigDecimal("42.56789567895678956789"));
    }

    @Test
    void eval_whole_integer() {
        assertThat(RealNumber.of("42").eval())
                .isEqualTo(new BigDecimal("42"));
    }

    @Test
    void eval_returns_the_value_passed_in() {
        assertThat(RealNumber.of("42.5").eval())
                .isEqualTo(new BigDecimal("42.5"));
    }

    @Test
    void eval_negative_value() {
        assertThat(RealNumber.of("-42.5").eval())
                .isEqualTo(new BigDecimal("-42.5"));
    }

    @Test
    void eval_value_with_whitespaces() {
        assertThat(RealNumber.of("    42.0\t\t\t").eval())
                .isEqualTo(new BigDecimal("42"));
    }

    @Test
    void eval_invalid_value() {
        final CalculatorException exception = assertThrows(CalculatorException.class, () -> RealNumber.of("abcdef"));
        assertThat(exception.getMessage()).isEqualTo("Unable to convert [abcdef] into a number.");
    }

    @Test
    void toString_returns_plain_decimal_string() {
        assertThat(RealNumber.of("9000000000000").toString())
                .isEqualTo("9000000000000");
    }
}
package com.github.wibowo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RealNumberTest {

    @Test
    void eval_value_from_bigDecimal() {
        final BigDecimal source = new BigDecimal("42.12345");
        assertThat(RealNumber.of(source).eval())
                .isSameAs(source);
    }

    @Test
    void eval_value_with_fractions_and_default_scaling() throws Exception{
        assertThat(RealNumber.of("42.5678956789567895678956789").eval())
                .isEqualTo(new BigDecimal("42.5678956789567896"));
    }

    @Test
    void eval_value_with_fractions_and_specific_scaling() throws Exception{
        assertThat(RealNumber.of("42.5678956789567895678956789", 20).eval())
                .isEqualTo(new BigDecimal("42.56789567895678956790"));
    }

    @Test
    void eval_whole_integer() throws Exception{
        assertThat(RealNumber.of("42").eval())
                .isEqualTo(new BigDecimal("42"));
    }

    @Test
    void eval_returns_the_value_passed_in() throws Exception{
        assertThat(RealNumber.of("42.0").eval())
                .isEqualTo(new BigDecimal("42.0"));
    }

    @Test
    void eval_negative_value() throws Exception{
        assertThat(RealNumber.of("-42.0").eval())
                .isEqualTo(new BigDecimal("-42.0"));
    }

    @Test
    void eval_value_with_whitespaces() throws Exception{
        assertThat(RealNumber.of("    42.0\t\t\t").eval())
                .isEqualTo(new BigDecimal("42.0"));
    }

    @Test
    void eval_invalid_value() {
        final CalculatorException exception = assertThrows(CalculatorException.class, () -> RealNumber.of("abcdef"));
        assertThat(exception.getMessage()).isEqualTo("Unable to convert [abcdef] into a number.");
    }

    @Test
    void toString_returns_plain_decimal_string() throws Exception {
        assertThat(RealNumber.of("9000000000000").toString())
                .isEqualTo("9,000,000,000,000");
    }
}
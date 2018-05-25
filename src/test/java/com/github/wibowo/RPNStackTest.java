package com.github.wibowo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RPNStackTest {

    private RPNStack<RealNumber> stack = new RPNStack<>();

    @Test
    void print_empty_stack() {
        final String expectedReport = "stack: ";

        assertThat(stack.toString())
                .isEqualTo(expectedReport);
    }

    @Test
    void add_new_item_to_stack() {
        final RealNumber number = RealNumber.of("42.0");

        stack.push(number);

        assertThat(stack.size())
                .isEqualTo(1);
        assertThat(stack.toString())
                .isEqualTo("stack: 42");
        assertThat(stack.pop())
                .isSameAs(number);
    }
}
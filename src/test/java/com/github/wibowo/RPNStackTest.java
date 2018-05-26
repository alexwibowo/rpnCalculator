package com.github.wibowo;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void clear_stack() {
        final RealNumber number = RealNumber.of("42.0");

        stack.push(number).clear();
        assertThat(stack.size())
                .isEqualTo(0);
        assertThat(stack.toString())
                .isEqualTo("stack: ");
    }

    @Test
    void popping_empty_stack_throws_exception() {
        assertThrows(EmptyStackException.class, () -> stack.pop());
        assertThrows(EmptyStackException.class, () -> stack.pop(2));
    }

    @Test
    void throws_exception_when_trying_to_pop_more_items_than_stack_size() {
        final RealNumber number = RealNumber.of("42.0");

        stack.push(number);

        final IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> stack.pop(2));
        assertThat(illegalStateException.getMessage())
                .isEqualTo("Not enough item in stack. Current stack size is 1");
    }

    @Test
    void popping_single_item_from_stack() {
        final RealNumber number1 = RealNumber.of("42.0");
        final RealNumber number2 = RealNumber.of("45.0");
        stack.push(number1);
        stack.push(number2);
        assertThat(stack.toString())
                .isEqualTo("stack: 42 45");


        final RealNumber pop = stack.pop();
        assertThat(pop)
                .isEqualTo(number2);
        assertThat(stack.size())
                .isEqualTo(1);
        assertThat(stack.toString())
                .isEqualTo("stack: 42");
    }

    @Test
    void popping_multiple_items_from_stack() {
        final RealNumber number1 = RealNumber.of("42.0");
        final RealNumber number2 = RealNumber.of("45.0");
        stack.push(number1);
        stack.push(number2);
        assertThat(stack.toString())
                .isEqualTo("stack: 42 45");


        final List<RealNumber> pop = Lists.newArrayList(stack.pop(2));
        assertThat(pop).hasSize(2);
        assertThat(pop.get(0)).isSameAs(number2);
        assertThat(pop.get(1)).isSameAs(number1);

        assertThat(stack.size())
                .isEqualTo(0);
        assertThat(stack.toString())
                .isEqualTo("stack: ");
    }
}
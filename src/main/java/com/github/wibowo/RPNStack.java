package com.github.wibowo;

import java.util.*;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.joining;

public class RPNStack<E> {

    private final java.util.LinkedList<E> stack;

    public RPNStack(){
        stack = new LinkedList<>();
    }

    public RPNStack push(final E item) {
        stack.push(item);
        return this;
    }

    public int size() {
        return stack.size();
    }

    @Override
    public String toString() {
        return StreamSupport.stream(Spliterators.spliterator(stack.descendingIterator(), stack.size(), Spliterator.ORDERED), false)
                .map(Object::toString)
                .collect(joining(" ", "stack: ", ""));
    }

    public E pop() throws EmptyStackException {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.pop();
    }

    public Iterable<E> pop(final int count) throws IllegalStateException {
        if (stack.size() < count) {
            throw new IllegalStateException("Not enough item in stack. Current stack size is " + stack.size());
        }

        final List<E> expressions = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            expressions.add(pop());
        }

        return expressions;
    }

}

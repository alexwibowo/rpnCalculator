package com.github.wibowo;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class TestHelper {
    @NotNull
    public static List<RealNumber> getArguments(final String... arguments) {
        return Arrays.stream(arguments).map(RealNumber::of).collect(Collectors.toList());
    }
}

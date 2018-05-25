package com.github.wibowo;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public final class RealNumber  {
    private static final int DEFAULT_SCALE = 16;

    private final ThreadLocal<DecimalFormat> decimalFormatter = ThreadLocal.withInitial(() -> {
        final DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setMaximumFractionDigits(DEFAULT_SCALE);
        decimalFormatter.setMinimumFractionDigits(0);
        decimalFormatter.setGroupingUsed(true);
        return decimalFormatter;
    });

    private final BigDecimal value;

    public static RealNumber of(final @NotNull String numberAsString) {
        Objects.requireNonNull(numberAsString);
        try {
            BigDecimal value = new BigDecimal(numberAsString.trim());
            final int scale = value.scale();
            if (scale > DEFAULT_SCALE) {
                value = value.setScale(DEFAULT_SCALE, RoundingMode.HALF_EVEN);
            }
            return new RealNumber(value);
        } catch (final Exception exception) {
            throw new CalculatorException(String.format("Unable to convert [%s] into a number.", numberAsString));
        }
    }

    public static RealNumber of(final String constantAsString,
                                final int scale) {
        Objects.requireNonNull(constantAsString);
        return new RealNumber(
                new BigDecimal(constantAsString)
                    .setScale(scale, RoundingMode.HALF_EVEN)
        );
    }

    public static RealNumber of(final BigDecimal number) {
        return new RealNumber(number);
    }

    public BigDecimal eval() {
        return value;
    }

    private RealNumber(final @NotNull BigDecimal value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return decimalFormatter.get().format(value);
    }
}

package com.github.wibowo;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public final class RealNumber  {
    private static final int DEFAULT_SCALE = 10;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.FLOOR;

    private final ThreadLocal<DecimalFormat> decimalFormatter = ThreadLocal.withInitial(() -> {
        final DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setMaximumFractionDigits(DEFAULT_SCALE);
        decimalFormatter.setMinimumFractionDigits(0);
        decimalFormatter.setGroupingUsed(false);
        return decimalFormatter;
    });

    private final BigDecimal value;

    public static RealNumber of(final @NotNull String numberAsString) {
        Objects.requireNonNull(numberAsString);
        try {
            BigDecimal value = new BigDecimal(numberAsString.trim());
            final int scale = value.scale();
            if (scale > DEFAULT_SCALE) {
                value = value.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
            }
            return new RealNumber(value.stripTrailingZeros());
        } catch (final Exception exception) {
            throw new CalculatorException(String.format("Unable to convert [%s] into a number.", numberAsString));
        }
    }

    public static RealNumber of(final String constantAsString,
                                final int scale) {
        Objects.requireNonNull(constantAsString);
        return new RealNumber(
                new BigDecimal(constantAsString)
                    .setScale(scale, DEFAULT_ROUNDING_MODE)
                    .stripTrailingZeros()
        );
    }

    public static RealNumber of(final BigDecimal number) {
        return new RealNumber(number);
    }

    public static RealNumber of(double number) {
        return new RealNumber(
                new BigDecimal(number)
                    .setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
                    .stripTrailingZeros()
        );
    }

    public BigDecimal eval() {
        return value;
    }

    private RealNumber(final @NotNull BigDecimal value) {
        this.value = Objects.requireNonNull(value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealNumber that = (RealNumber) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return decimalFormatter.get().format(value);
    }
}

package com.github.wibowo;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Abstraction of number, with default formatting and scaling
 *
 * Default scaling used is {@link #DEFAULT_SCALE}, unless specified {@link RealNumber#of(String, int)}
 * Note that the {@link #DEFAULT_SCALE_FOR_PRINTING} is used for the String representation.
 */
public final class RealNumber  {
    public static final int DEFAULT_SCALE = 16;
    public static final int DEFAULT_SCALE_FOR_PRINTING = 10;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.FLOOR;

    private final ThreadLocal<DecimalFormat> decimalFormatter = ThreadLocal.withInitial(() -> {
        final DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setMaximumFractionDigits(DEFAULT_SCALE_FOR_PRINTING);
        decimalFormatter.setMinimumFractionDigits(0);
        decimalFormatter.setRoundingMode(DEFAULT_ROUNDING_MODE);
        decimalFormatter.setGroupingUsed(false);
        return decimalFormatter;
    });

    private final BigDecimal value;

    /**
     * Construct instance of this class from String, with default scaling.
     *
     * @param numberAsString number to construct from
     * @return instance of this class
     */
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

    /**
     * Construct instance of this class from String, with specified scaling.
     *
     * @param numberAsString number to construct from
     * @param scale scaling to be used
     * @return instance of this class
     */
    public static RealNumber of(final @NotNull String numberAsString,
                                final int scale) {
        Objects.requireNonNull(numberAsString);
        return new RealNumber(
                new BigDecimal(numberAsString)
                        .setScale(scale, DEFAULT_ROUNDING_MODE)
                        .stripTrailingZeros()
        );
    }

    /**
     * Construct from given BigDecimal, with default scaling
     *
     * @param number number to construct from
     * @return instance of this class
     */
    public static RealNumber of(final @NotNull BigDecimal number) {
        Objects.requireNonNull(number);
        return new RealNumber(
                number
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

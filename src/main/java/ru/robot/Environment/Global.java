package ru.robot.Environment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Global {

    public static final MathContext MC6 = new MathContext(6, RoundingMode.HALF_UP);

    public static final BigDecimal minusONE = BigDecimal.ONE.multiply(new BigDecimal("-1.00000"));
    public static final BigDecimal ZERO = new BigDecimal("0.00000");
    public static final BigDecimal ONE = new BigDecimal("1.00000");
    public static final BigDecimal TWO = new BigDecimal("2.00000");
    public static final BigDecimal THREE = new BigDecimal("3.00000");
    public static final BigDecimal FOUR = new BigDecimal("4.00000");
    public static final BigDecimal FIVE = new BigDecimal("5.00000");
    public static final BigDecimal SIX = new BigDecimal("6.00000");
}

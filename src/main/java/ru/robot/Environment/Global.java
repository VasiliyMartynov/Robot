package ru.robot.Environment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Global {

    public static final MathContext MC6 = new MathContext(6, RoundingMode.HALF_UP);
    public static final BigDecimal minusONE = BigDecimal.ONE.multiply(new BigDecimal("-1"));
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal TWO = new BigDecimal("2");
    public static final BigDecimal FIVE = new BigDecimal("5");
}

package ru.robot.Model.CoordinateSystem.Cartesian.Utils;

import ru.robot.Environment.Global;

import java.math.BigDecimal;
import static ru.robot.Environment.Global.*;

public class Utils {


    /**
     * just negate input value
     * @param x
     * @return negative valuew of x
     */
    public static BigDecimal minus(BigDecimal x){
        return x.multiply(Global.minusONE);
    }


    /**
     * Determines whether a scalar is small enough to be treated as zero
     * :param z: A scalar input to check
     *     :return: -1 if z is close to zero, 0 or 1 otherwise
     *     Example Input:
     *         z = -1e-7
     *     Output:
     *         True
     */
    public static int nearZero(BigDecimal z){
        return z.abs(MC6).compareTo(new BigDecimal("0.000001"));
    }





}







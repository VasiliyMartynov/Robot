package ru.robot.Model.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Objects;

import static ru.robot.Environment.Global.*;

public class Utils {
    private static final Logger LOGGER = LogManager.getLogger();


    /**
     * just negate input value
     * @param x input BigDecimal
     * @return negative valuew of x
     */
    public static BigDecimal minus(BigDecimal x){
        return x.multiply(new BigDecimal("-1.00000"), MC6);
    }


    /**
     Determines whether a scalar is small enough to be treated as zero
     <p>Example Input:
     <br>z = -1e-7
     <p>Output:
     <br>True
     * @param z A scalar input to check
     * @return -1 if z is close to zero, 0 or 1 otherwise
     */
    public static int nearZero(BigDecimal z){
        var result = z.abs(MC6).compareTo(new BigDecimal("0.0000001"));
        //LOGGER.debug("nearZero is '{}'", result);
        return result;
    }

    public static BigDecimal Rround(BigDecimal i){
        var z = nearZero(i) == -1;
        BigDecimal result;
        //LOGGER.debug("Rround Z is '{}'", z);
        if (z) {
            result = ZERO;
        } else {
            result =  i.round(MC6);
        }
        //LOGGER.debug("Rround result is '{}'", result);
        return result;

    }







}







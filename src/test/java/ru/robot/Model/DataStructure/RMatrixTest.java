package ru.robot.Model.DataStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ujmp.core.Matrix;
import org.ujmp.core.bigdecimalmatrix.impl.DefaultDenseBigDecimalMatrix2D;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RMatrixTest {

    @BeforeEach
    void init(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    private Matrix getMatrix(){
        Matrix a = new DefaultDenseBigDecimalMatrix2D(3, 3);
        a.setAsBigDecimal(new BigDecimal("0.804738"), 0, 0);
        a.setAsBigDecimal(new BigDecimal("-0.310617"), 0, 1);
        a.setAsBigDecimal(new BigDecimal("0.505879"), 0, 2);
        a.setAsBigDecimal(new BigDecimal("0.505879"), 1, 0);
        a.setAsBigDecimal(new BigDecimal("0.804738"), 1, 1);
        a.setAsBigDecimal(new BigDecimal("-0.310617"), 1, 2);
        a.setAsBigDecimal(new BigDecimal("-0.310617"), 2, 0);
        a.setAsBigDecimal(new BigDecimal("0.505879"), 2, 1);
        a.setAsBigDecimal(new BigDecimal("0.804738"), 2, 2);
        return a;
    }

    private Matrix getBadMatrix(){
        Matrix a = new DefaultDenseBigDecimalMatrix2D(3, 3);
        a.setAsBigDecimal(new BigDecimal("0.004738"), 0, 0);
        a.setAsBigDecimal(new BigDecimal("-0.310617"), 0, 1);
        a.setAsBigDecimal( new BigDecimal("0.505879"), 0, 2);
        a.setAsBigDecimal(new BigDecimal("0.505879"), 1, 0);
        a.setAsBigDecimal(new BigDecimal("0.804738"), 1, 1);
        a.setAsBigDecimal(new BigDecimal("-0.310617"), 1, 2);
        a.setAsBigDecimal(new BigDecimal("-0.310617"), 2, 0);
        a.setAsBigDecimal(new BigDecimal("0.505879"), 2, 1);
        a.setAsBigDecimal(new BigDecimal("0.804738"), 2, 2);
        return a;
    }

    @Test
    void RMatrixConstructorTestNotOK(){
        assertThrows(IllegalArgumentException.class, () -> new RMatrix(getBadMatrix()));

    }

    @Test
    void RMatrixConstructorTestOK(){
        new RMatrix(getMatrix());
    }
}

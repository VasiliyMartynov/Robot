package ru.robot.cartesian.spatial.rotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.robot.cartesian.utils.RMatrix;
import ru.robot.cartesian.utils.YESNO;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.robot.cartesian.spatial.rotation.AngleAxis.MatrixLog3;
import static ru.robot.cartesian.utils.GVARS.*;
import static ru.robot.cartesian.utils.Utils.minus;

public class AngleAxisTest {

    @BeforeEach
    void init(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    private RMatrix getGoodRotationMatrix(){
        RMatrix a = new RMatrix(3);
        a.set(new BigDecimal("0.804738"), 0, 0);
        a.set(new BigDecimal("-0.310617"), 0, 1);
        a.set(new BigDecimal("0.505879"), 0, 2);
        a.set(new BigDecimal("0.505879"), 1, 0);
        a.set(new BigDecimal("0.804738"), 1, 1);
        a.set(new BigDecimal("-0.310617"), 1, 2);
        a.set(new BigDecimal("-0.310617"), 2, 0);
        a.set(new BigDecimal("0.505879"), 2, 1);
        a.set(new BigDecimal("0.804738"), 2, 2);
        return a;
    }

    private RMatrix getBadRotationMatrix(){
        RMatrix a = new RMatrix(3);
        a.set(new BigDecimal("0.004738"), 0, 0);
        a.set(new BigDecimal("-0.310617"), 0, 1);
        a.set( new BigDecimal("0.505879"), 0, 2);
        a.set(new BigDecimal("0.505879"), 1, 0);
        a.set(new BigDecimal("0.804738"), 1, 1);
        a.set(new BigDecimal("-0.310617"), 1, 2);
        a.set(new BigDecimal("-0.310617"), 2, 0);
        a.set(new BigDecimal("0.505879"), 2, 1);
        a.set(new BigDecimal("0.804738"), 2, 2);
        return a;
    }

    @Test
    void MatrixLog3Test() throws InstantiationException {
        var SO3 = RMatrix.setValues3x3(Arrays.asList(ZERO,ZERO,ONE, ONE,ZERO,ZERO,ZERO,ONE,ZERO), YESNO.YES);
        var n = new BigDecimal("1.20919958");
        var minusN = minus(n);
        var so3 = RMatrix.setValues3x3(Arrays.asList(ZERO, minusN,n, n,ZERO,minusN,minusN,n,ZERO), YESNO.NO);
        System.out.println(SO3.toString());
        System.out.println(so3.toString());
        System.out.println(MatrixLog3(SO3).toString());

    }
}

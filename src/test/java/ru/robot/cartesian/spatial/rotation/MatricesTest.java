package ru.robot.cartesian.spatial.rotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.robot.Model.Rigid.BodyRotation;
import ru.robot.Model.CoordinateSystem.Cartesian.AXIS;
import ru.robot.Model.DataStructure.RMatrix;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.Matrices.getRotationAroundFixedAxis;
import static ru.robot.Model.DataStructure.RMatrix.getIdentityMatrix;

public class MatricesTest {

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
    void getNewInstanceOfRotationAroundFixedAxis() throws InstantiationException {
        var c = new BodyRotation(getIdentityMatrix(3));
        var angle = new BigDecimal("0.785398");
        var a = getRotationAroundFixedAxis(c.getLocalRotationMatrix(),angle, AXIS.X);
        System.out.println(a.toString());
        assertEquals(1.0, a.getDouble(0,0));
        assertEquals(0.0, a.getDouble(0,1));
        assertEquals(0.0, a.getDouble(0,2));
        assertEquals(0.0, a.getDouble(1,0));
        assertEquals(0.707107, a.getDouble(1,1));
        assertEquals(-0.707107, a.getDouble(1,2));
        assertEquals(0.0, a.getDouble(2,0));
        assertEquals(0.707107, a.getDouble(2,1));
        assertEquals(0.707107, a.getDouble(2,2));
    }
}

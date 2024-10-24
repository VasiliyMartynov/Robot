package ru.robot.cartesian.spatial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static ru.robot.cartesian.spatial.BodyRotation.getRotationFromAngleAxis;
import static ru.robot.cartesian.spatial.RMatrix.getIdentityMatrix;

public class BodyRotationTest {

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
    void BodyRotationListConstructorTestIsOK() throws InstantiationException {
        var m = getGoodRotationMatrix();
        BodyRotation b = new BodyRotation(m);
        for(int i = 0; i < b.rotationMatrixData.getRowCount(); i++) {
            for(int j = 0; j < b.rotationMatrixData.getColumnCount(); j++) {
                assertEquals(m.get(i,j), b.getItem(i, j));
            }
        }
    }

    @Test
    void BodyRotationListConstructorTestNotOK(){
        assertThrows(InstantiationException.class, () -> new BodyRotation(getBadRotationMatrix()));

    }



    @Test
    void RotationTest() throws InstantiationException {
        var a = new BodyRotation(getGoodRotationMatrix());
        var b = new BodyRotation(getGoodRotationMatrix());
        a.rotate(b);
        System.out.println(a.toString());
        assertEquals(0.333334, a.getItem(0,0).doubleValue());
        assertEquals(-0.244017, a.getItem(0,1).doubleValue());
        assertEquals(0.910683, a.getItem(0,2).doubleValue());
        assertEquals(0.910683, a.getItem(1,0).doubleValue());
        assertEquals(0.333334, a.getItem(1,1).doubleValue());
        assertEquals(-0.244017, a.getItem(1,2).doubleValue());
        assertEquals(-0.244017, a.getItem(2,0).doubleValue());
        assertEquals(0.910683, a.getItem(2,1).doubleValue());
        assertEquals(0.333334, a.getItem(2,2).doubleValue());
    }

    @Test
    void getNewInstanceOFRotationTest() throws InstantiationException {
        var c = new BodyRotation(getGoodRotationMatrix());
        var b = new BodyRotation(getGoodRotationMatrix());

        var a = c.getMatrixOfRotation(b);
        System.out.println(a.toString());
        assertEquals(0.333334, a.getItem(0,0).doubleValue());
        assertEquals(-0.244017, a.getItem(0,1).doubleValue());
        assertEquals(0.910683, a.getItem(0,2).doubleValue());
        assertEquals(0.910683, a.getItem(1,0).doubleValue());
        assertEquals(0.333334, a.getItem(1,1).doubleValue());
        assertEquals(-0.244017, a.getItem(1,2).doubleValue());
        assertEquals(-0.244017, a.getItem(2,0).doubleValue());
        assertEquals(0.910683, a.getItem(2,1).doubleValue());
        assertEquals(0.333334, a.getItem(2,2).doubleValue());
    }


    @Test
    void getNewInstanceOfRotationAroundFixedAxis() throws InstantiationException {
        var c = new BodyRotation(getIdentityMatrix());
        var angle = new BigDecimal("0.785398");
        var a = c.getRotationAroundFixedAxis(angle,AXIS.X);
        System.out.println(a.toString());
        assertEquals(1.0, a.getItemAsDouble(0,0));
        assertEquals(0.0, a.getItemAsDouble(0,1));
        assertEquals(0.0, a.getItemAsDouble(0,2));
        assertEquals(0.0, a.getItemAsDouble(1,0));
        assertEquals(0.707107, a.getItemAsDouble(1,1));
        assertEquals(-0.707107, a.getItemAsDouble(1,2));
        assertEquals(0.0, a.getItemAsDouble(2,0));
        assertEquals(0.707107, a.getItemAsDouble(2,1));
        assertEquals(0.707107, a.getItemAsDouble(2,2));
    }


    @Test
    void getRotationAroundVectorTest() throws InstantiationException {
        var angle = new BigDecimal("0.785398");
        var x = new BigDecimal("0.577350");
        var y = new BigDecimal("0.577350");
        var z = new BigDecimal("0.577350");
        var a = getRotationFromAngleAxis(angle,x,y,z);
        System.out.println(a.toString());
        assertEquals(0.804738, a.getItemAsDouble(0,0));
        assertEquals(-0.310617, a.getItemAsDouble(0,1));
        assertEquals(0.505879, a.getItemAsDouble(0,2));
        assertEquals(0.505879, a.getItemAsDouble(1,0));
        assertEquals(0.804738, a.getItemAsDouble(1,1));
        assertEquals(-0.310617, a.getItemAsDouble(1,2));
        assertEquals(-0.310617, a.getItemAsDouble(2,0));
        assertEquals(0.505879, a.getItemAsDouble(2,1));
        assertEquals(0.804738, a.getItemAsDouble(2,2));

    }

    @Test
    void calcAxisAngleCaseCaseAifMatrixIsIdentity() throws InstantiationException {
        var a = new BodyRotation(getIdentityMatrix());
        var b = a.getAngleAxisFromLocalSO3();
        assertEquals(0.0, b.getAsDouble(0,0));
        assertEquals(0.0, b.getAsDouble(1,0));
        assertEquals(0.0, b.getAsDouble(2,0));
        assertEquals(0.0, b.getAsDouble(3,0));

    }
//
    @Test
    void calcAxisAngleCaseCifTraceIsMinusOne() throws InstantiationException {
        var a = new BodyRotation(getGoodRotationMatrix());
        var b = a.getAngleAxisFromLocalSO3();
        //0.577350 0.577350 0.577350 0.785398
        assertEquals(0.577350, b.getAsDouble(0,0));
        assertEquals(0.577350, b.getAsDouble(1,0));
        assertEquals(0.577350, b.getAsDouble(2,0));
        assertEquals(0.785398, b.getAsDouble(3,0));
    }

//    @Test
//    void getAxisAngleCaseBTest() throws InstantiationException {
//        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
//
//        //axis angle 0.707 0.707 0, 0.519
//        var a = new BodyRotation(m);
//        var b = a.getAxisAngle();
//        assertEquals(.707, b.get(0));
//        assertEquals(.707, b.get(1));
//        assertEquals(0, b.get(2));
//        assertEquals(0.519, b.get(3));
//    }

}

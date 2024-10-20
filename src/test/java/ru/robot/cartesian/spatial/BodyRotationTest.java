package ru.robot.cartesian.spatial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.ujmp.core.Matrix;
import org.ujmp.core.bigdecimalmatrix.DenseBigDecimalMatrix2D;
import org.ujmp.core.bigdecimalmatrix.impl.DefaultDenseBigDecimalMatrix2D;
import ru.robot.cartesian.utils.MatrixUtils;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class BodyRotationTest {

    @BeforeEach
    void init(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    private Matrix getGoodRotationMatrix(){
        DenseBigDecimalMatrix2D a = new DefaultDenseBigDecimalMatrix2D(3, 3);
        a.setAsBigDecimal(new BigDecimal("0.804738"), 0, 0);
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

    private Matrix getBadRotationMatrix(){
        DenseBigDecimalMatrix2D a = new DefaultDenseBigDecimalMatrix2D(3, 3);
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
    void BodyRotationListConstructorTestIsOK() throws InstantiationException {
        var m = getGoodRotationMatrix();
        BodyRotation b = new BodyRotation(m);
        for(int i = 0; i < b.rotationMatrixData.getRowCount(); i++) {
            for(int j = 0; j < b.rotationMatrixData.getColumnCount(); j++) {
                assertEquals(m.getAsBigDecimal(i,j), b.getItem(i, j));
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
    void getNewInstanceOfRotationAroundXTest() throws InstantiationException {
        var c = new BodyRotation(MatrixUtils.getIdentityMatrix());
        var b = new BigDecimal("0.785398");
        var a = c.getRotationAroundX(b);
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
    void getNewInstanceOfRotationAroundYTest() throws InstantiationException {
        var c = new BodyRotation(MatrixUtils.getIdentityMatrix());
        var b = new BigDecimal("0.785398");
        var a = c.getRotationAroundY(b);
        System.out.println(a.toString());
        assertEquals(0.707107, a.getItem(0,0).doubleValue());
        assertEquals(0.0, a.getItem(0,1).doubleValue());
        assertEquals(0.707107, a.getItem(0,2).doubleValue());
        assertEquals(0.0, a.getItem(1,0).doubleValue());
        assertEquals(1.0, a.getItem(1,1).doubleValue());
        assertEquals(0.0, a.getItem(1,2).doubleValue());
        assertEquals(-0.707107, a.getItem(2,0).doubleValue());
        assertEquals(0.0, a.getItem(2,1).doubleValue());
        assertEquals(0.707107, a.getItem(2,2).doubleValue());
    }

    @Test
    void getNewInstanceOfRotationAroundZTest() throws InstantiationException {
        var c = new BodyRotation(MatrixUtils.getIdentityMatrix());
        var b = new BigDecimal("0.785398");
        var a = c.getRotationAroundZ(b);
        System.out.println(a.toString());
        assertEquals(0.707107, a.getItem(0,0).doubleValue());
        assertEquals(-0.707107, a.getItem(0,1).doubleValue());
        assertEquals(0.0, a.getItem(0,2).doubleValue());
        assertEquals(0.707107, a.getItem(1,0).doubleValue());
        assertEquals(0.707107, a.getItem(1,1).doubleValue());
        assertEquals(0.0, a.getItem(1,2).doubleValue());
        assertEquals(0.0, a.getItem(2,0).doubleValue());
        assertEquals(0.0, a.getItem(2,1).doubleValue());
        assertEquals(1.0, a.getItem(2,2).doubleValue());
    }


    @Test
    void getRotationAroundVectorTest() throws InstantiationException {
        var a = new BodyRotation(MatrixUtils.getIdentityMatrix());
        System.out.println(a.toString());
        var b = a.getRotationAroundVector(new BigDecimal("0.996047"), new BigDecimal("0.155150"), new BigDecimal("0.505820"), new BigDecimal("0.848572"));
        System.out.println(b.toString());
    }
//
//    @Test
//    void getAxisAngleTestCaseAifMatrixIsIdentity() throws InstantiationException {
//        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
//        m.set(0,0,1);
//        m.set(0,1,0);
//        m.set(0,2,0);
//        m.set(1,0,0);
//        m.set(1,1,1);
//        m.set(1,2,0);
//        m.set(2,0,0);
//        m.set(2,1,0);
//        m.set(2,2,1);
//        var a = new BodyRotation(m);
//        var b = a.getAxisAngle();
//        assertEquals(0.0, b.get(3));
//        assertEquals(0.0, b.get(0));
//        assertEquals(0.0, b.get(1));
//        assertEquals(0.0, b.get(2));
//
//    }
//
//    @Test
//    void getAxisAngleTestCaseBifTraceIsMinusOne() throws InstantiationException {
//        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
//        m.set(0,0,-0.33366);
//        m.set(0,1,0.66682);
//        m.set(0,2,0.66682);
//        m.set(1,0,0.66682);
//        m.set(1,1,-0.33366);
//        m.set(1,2,0.66682);
//        m.set(2,0,0.66682);
//        m.set(2,1,0.66682);
//        m.set(2,2,-0.33366);
//        var a = new BodyRotation(m);
//        var b = a.getAxisAngle();
//        //0.578000 0.578000 0.578000 3.14159
//        assertEquals(.578, b.get(0));
//        assertEquals(.578, b.get(1));
//        assertEquals(.578, b.get(2));
//        assertEquals(3.142, b.get(3));
//    }
//
//    @Test
//    void getAxisAngleCaseCTest() throws InstantiationException {
//        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
//        m.set(0,0,0.93435);
//        m.set(0,1,0.06565);
//        m.set(0,2,0.35000);
//        m.set(1,0,0.06565);
//        m.set(1,1,0.93435);
//        m.set(1,2,-0.35000);
//        m.set(2,0,-0.35000);
//        m.set(2,1,0.35000);
//        m.set(2,2,0.86870);
//        //axis angle 0.707 0.707 0, 0.519
//        var a = new BodyRotation(m);
//        var b = a.getAxisAngle();
//        assertEquals(.707, b.get(0));
//        assertEquals(.707, b.get(1));
//        assertEquals(0, b.get(2));
//        assertEquals(0.519, b.get(3));
//    }




}

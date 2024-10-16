package ru.robot.cartesian.spatial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.ArrayList;
import java.util.Arrays;

public class BodyRotationTest {

    private TestInfo testInfo;

    @BeforeEach
    void init(TestInfo testInfo) {
        this.testInfo = testInfo;
        System.out.println(testInfo.getDisplayName());
    }

    @Test
    void BodyRotationListContractorTest(){
        //System.out.println(testInfo.getDisplayName());
        ArrayList<Double> a  = new ArrayList<>(Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0));
        var b = new BodyRotation(a);
        System.out.println(b.toString());
        assertEquals(0.0, b.getLocalRotationMatrix().get(0,0));
        assertEquals(1.0, b.getLocalRotationMatrix().get(0,1));
        assertEquals(2.0, b.getLocalRotationMatrix().get(0,2));
        assertEquals(3.0, b.getLocalRotationMatrix().get(1,0));
        assertEquals(4.0, b.getLocalRotationMatrix().get(1,1));
        assertEquals(5.0, b.getLocalRotationMatrix().get(1,2));
        assertEquals(6.0, b.getLocalRotationMatrix().get(2,0));
        assertEquals(7.0, b.getLocalRotationMatrix().get(2,1));
        assertEquals(8.0, b.getLocalRotationMatrix().get(2,2));
    }

    @Test
    void BodyRotationNumbersContractorTest(){
        var b = new BodyRotation(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        System.out.println(b.toString());
        assertEquals(0.0, b.getLocalRotationMatrix().get(0,0));
        assertEquals(1.0, b.getLocalRotationMatrix().get(0,1));
        assertEquals(2.0, b.getLocalRotationMatrix().get(0,2));
        assertEquals(3.0, b.getLocalRotationMatrix().get(1,0));
        assertEquals(4.0, b.getLocalRotationMatrix().get(1,1));
        assertEquals(5.0, b.getLocalRotationMatrix().get(1,2));
        assertEquals(6.0, b.getLocalRotationMatrix().get(2,0));
        assertEquals(7.0, b.getLocalRotationMatrix().get(2,1));
        assertEquals(8.0, b.getLocalRotationMatrix().get(2,2));
    }

    @Test
    void BodyRotationMatrixContractorTest(){
        DoubleMatrix2D matrix = new DenseDoubleMatrix2D(3,3);;
        matrix.set(0,0,1);
        matrix.set(0,1,2);
        matrix.set(0,2,3);
        matrix.set(1,0,4);
        matrix.set(1,1,5);
        matrix.set(1,2,6);
        matrix.set(2,0,7);
        matrix.set(2,1,8);
        matrix.set(2,2,9);

        var b = new BodyRotation(matrix);
        System.out.println(b.toString());
        assertEquals(1, b.getLocalRotationMatrix().get(0,0));
        assertEquals(2, b.getLocalRotationMatrix().get(0,1));
        assertEquals(3, b.getLocalRotationMatrix().get(0,2));
        assertEquals(4, b.getLocalRotationMatrix().get(1,0));
        assertEquals(5, b.getLocalRotationMatrix().get(1,1));
        assertEquals(6, b.getLocalRotationMatrix().get(1,2));
        assertEquals(7, b.getLocalRotationMatrix().get(2,0));
        assertEquals(8, b.getLocalRotationMatrix().get(2,1));
        assertEquals(9, b.getLocalRotationMatrix().get(2,2));
    }

    @Test
    void RotationTest(){
        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
        m.set(0,0,1);
        m.set(0,1,2);
        m.set(0,2,3);
        m.set(1,0,4);
        m.set(1,1,5);
        m.set(1,2,6);
        m.set(2,0,7);
        m.set(2,1,8);
        m.set(2,2,9);

        DoubleMatrix2D n = new DenseDoubleMatrix2D(3,3);
        n.set(0,0,9);
        n.set(0,1,8);
        n.set(0,2,7);
        n.set(1,0,6);
        n.set(1,1,5);
        n.set(1,2,4);
        n.set(2,0,3);
        n.set(2,1,2);
        n.set(2,2,1);

        var a = new BodyRotation(m);
        System.out.println(a.toString());
        var b = new BodyRotation(n);
        System.out.println(b.toString());
        a.rotate(b);
        assertEquals(30, a.getLocalRotationMatrix().get(0,0));
        assertEquals(24, a.getLocalRotationMatrix().get(0,1));
        assertEquals(18, a.getLocalRotationMatrix().get(0,2));
        assertEquals(84, a.getLocalRotationMatrix().get(1,0));
        assertEquals(69, a.getLocalRotationMatrix().get(1,1));
        assertEquals(54, a.getLocalRotationMatrix().get(1,2));
        assertEquals(138, a.getLocalRotationMatrix().get(2,0));
        assertEquals(114, a.getLocalRotationMatrix().get(2,1));
        assertEquals(90, a.getLocalRotationMatrix().get(2,2));
    }

    @Test
    void getMatrixOfRotationTest(){
        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
        m.set(0,0,1);
        m.set(0,1,2);
        m.set(0,2,3);
        m.set(1,0,4);
        m.set(1,1,5);
        m.set(1,2,6);
        m.set(2,0,7);
        m.set(2,1,8);
        m.set(2,2,9);

        DoubleMatrix2D n = new DenseDoubleMatrix2D(3,3);;
        n.set(0,0,9);
        n.set(0,1,8);
        n.set(0,2,7);
        n.set(1,0,6);
        n.set(1,1,5);
        n.set(1,2,4);
        n.set(2,0,3);
        n.set(2,1,2);
        n.set(2,2,1);

        var a = new BodyRotation(m);
        System.out.println(a.toString());
        var b = new BodyRotation(n);
        System.out.println(b.toString());
        DoubleMatrix2D c = a.getMatrixOfRotation(b);
        System.out.println(a.toString());
        System.out.println(c.toString());
        assertEquals(1, a.getLocalRotationMatrix().get(0,0));
        assertEquals(2, a.getLocalRotationMatrix().get(0,1));
        assertEquals(3, a.getLocalRotationMatrix().get(0,2));
        assertEquals(4, a.getLocalRotationMatrix().get(1,0));
        assertEquals(5, a.getLocalRotationMatrix().get(1,1));
        assertEquals(6, a.getLocalRotationMatrix().get(1,2));
        assertEquals(7, a.getLocalRotationMatrix().get(2,0));
        assertEquals(8, a.getLocalRotationMatrix().get(2,1));
        assertEquals(9, a.getLocalRotationMatrix().get(2,2));
        assertEquals(30, c.get(0,0));
        assertEquals(24, c.get(0,1));
        assertEquals(18, c.get(0,2));
        assertEquals(84, c.get(1,0));
        assertEquals(69, c.get(1,1));
        assertEquals(54, c.get(1,2));
        assertEquals(138, c.get(2,0));
        assertEquals(114, c.get(2,1));
        assertEquals(90, c.get(2,2));
    }

    @Test
    void getRotationAroundVectorTest(){
        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
        m.set(0,0,1);
        m.set(0,1,1);
        m.set(0,2,1);
        m.set(1,0,1);
        m.set(1,1,1);
        m.set(1,2,1);
        m.set(2,0,1);
        m.set(2,1,1);
        m.set(2,2,1);

        var a = new BodyRotation(m);
        System.out.println(a.toString());
        var b = a.getRotationAroundVector(0.524, 0.0, 0.866, 0.5);
        System.out.println(b.toString());
    }

    @Test
    void getRotationAxisTestCaseAifMatrixIsIdentity (){
        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
        m.set(0,0,1);
        m.set(0,1,0);
        m.set(0,2,0);
        m.set(1,0,0);
        m.set(1,1,1);
        m.set(1,2,0);
        m.set(2,0,0);
        m.set(2,1,0);
        m.set(2,2,1);
        var a = new BodyRotation(m);
        a.getRotationAxis();
        assertEquals(0.0, a.getAxisAngle().get(3));
        assertEquals(0.0, a.getAxisAngle().get(0));
        assertEquals(0.0, a.getAxisAngle().get(1));
        assertEquals(0.0, a.getAxisAngle().get(2));

    }

    @Test
    void getRotationAxisTestCaseBifTraceIsMinusOne(){
        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
        m.set(0,0,-0.33366);
        m.set(0,1,0.66682);
        m.set(0,2,0.66682);
        m.set(1,0,0.66682);
        m.set(1,1,-0.33366);
        m.set(1,2,0.66682);
        m.set(2,0,0.66682);
        m.set(2,1,0.66682);
        m.set(2,2,-0.33366);
        var a = new BodyRotation(m);
        var b = a.getRotationAxis();
        //0.578000 0.578000 0.578000 3.14159
        assertEquals(.578, b.get(0));
        assertEquals(.578, b.get(1));
        assertEquals(.578, b.get(2));
        assertEquals(3.142, b.get(3));
    }

    @Test
    void getRotationAxisCaseCTest(){
        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
        m.set(0,0,0.93435);
        m.set(0,1,0.06565);
        m.set(0,2,0.35000);
        m.set(1,0,0.06565);
        m.set(1,1,0.93435);
        m.set(1,2,-0.35000);
        m.set(2,0,-0.35000);
        m.set(2,1,0.35000);
        m.set(2,2,0.86870);
        //axis angle 0.707 0.707 0, 0.519
        var a = new BodyRotation(m);
        var b = a.getRotationAxis();
        assertEquals(.707, b.get(0));
        assertEquals(.707, b.get(1));
        assertEquals(0, b.get(2));
        assertEquals(0.519, b.get(3));
    }




}

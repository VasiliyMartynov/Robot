package ru.robot.cartesian.spatial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class BodyRotationTest {

    @Test
    void firstTest() {
        DoubleMatrix2D matrix;
        matrix = new DenseDoubleMatrix2D(3,3);
        matrix.set(0,0,1);
        matrix.set(1,1,1);
        matrix.set(2,2,1);

//        CoordinateSystem cs = new CoordinateSystem(
//                "Local",
//                getGlobalCoordinateSystem(),
//                new Position(0,0,0),
//                new BodyRotation(matrix)
//        );

        assertEquals(3, matrix.rows());
        assertEquals(3, matrix.columns());
    }

    @Test
    void BodyRotationListContractorTest(){
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
    void getRotationAxisTest(){
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

        DoubleMatrix2D n = new DenseDoubleMatrix2D(3,3);;
        n.set(0,0,0.5);
        n.set(0,1,0.5);
        n.set(0,2,0.5);
        n.set(1,0,0.5);
        n.set(1,1,0.5);
        n.set(1,2,0.5);
        n.set(2,0,0.5);
        n.set(2,1,0.5);
        n.set(2,2,0.5);
        var a = new BodyRotation(m);
        var b = new BodyRotation(n);
        b.getRotationAxis(a);
        System.out.println(b.getAngleTheta());
        System.out.println(b.getVectorAxisOmega());
    }


}

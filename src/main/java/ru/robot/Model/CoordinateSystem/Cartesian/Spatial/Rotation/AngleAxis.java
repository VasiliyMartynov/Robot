package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation;

import ru.robot.Model.DataStructure.RMatrix;
import ru.robot.Model.DataStructure.RVector;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static ch.obermuhlner.math.big.BigDecimalMath.*;
import static ru.robot.Model.DataStructure.RMatrix.getIdentityMatrix;
import static ru.robot.Model.DataStructure.RVector.normOfVector;
import static ru.robot.Model.DataStructure.RVector.normaliseVector;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.nearZero;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.*;

public class AngleAxis {

    /**
     * Converts a 3-vector to an so(3) representation
     *     :param omg: A 3-vector
     *     :return: The skew symmetric representation of omg
     *
     *     Example Input:
     *         omg = np.array([1, 2, 3])
     *     Output:
     * ([[ 0, -3,  2],
     * [ 3,  0, -1],
     * [-2,  1,  0]])
     */
    public static RMatrix VecToso3(RVector omg){
        var v1 = omg.get(0);
        var v2 = omg.get(1);
        var v3 = omg.get(2);
        List<BigDecimal> items = Arrays.asList(ZERO, minus(v3), v2,v3,ZERO,minus(v1),minus(v2),v1, ZERO);
        return new RMatrix(items);
    }

    /**
     *     Converts an so(3) representation to a 3-vector
     *
     *     :param so3mat: A 3x3 skew-symmetric matrix
     *     :return: The 3-vector corresponding to so3mat
     *
     *     Example Input:
     *         so3mat = np.array([[ 0, -3,  2],
     *                            [ 3,  0, -1],
     *                            [-2,  1,  0]])
     *     Output:
     *         np.array([1, 2, 3])
     */
    public static RVector so3ToVec(RMatrix so3mat){
        var m1 = so3mat.get(2,1);
        var m2 = so3mat.get(0,2);
        var m3 = so3mat.get(1,0);
        return  new RVector(m1, m2, m3);
    }

    public static RVector AngleAxisToVec(RVector fourVector){
        var m1 = fourVector.get(0);
        var m2 = fourVector.get(1);
        var m3 = fourVector.get(2);
        var m4 = fourVector.get(3);
        var x1 = m1.multiply(m4);
        var x2 = m2.multiply(m4);
        var x3 = m3.multiply(m4);
        return  new RVector(x1, x2, x3);
    }


    /**
     *     Converts a 3-vector of exponential coordinates for rotation into
     *     axis-angle form
     *     :return omghat: A unit rotation axis
     *     :return theta: The corresponding rotation angle
     *     Example Input:
     *         expc3 = np.array([1, 2, 3])
     *     Output:
     *         (np.array([0.26726124, 0.53452248, 0.80178373]), 3.7416573867739413)
     *     return (Normalize(expc3), np.linalg.norm(expc3))
     * @param expc3  A 3-vector of exponential coordinates for rotation
     * @return result
     */
    public static RVector AxisAng3(RVector expc3){
        RVector omghat = normaliseVector(expc3);
        return new RVector(omghat.get(0), omghat.get(1),omghat.get(2) , normOfVector(expc3));
    }

    /**
     Computes the matrix exponential of a matrix in so(3) using Rodriges formula

     :param so3mat: A 3x3 skew-symmetric matrix
     :return: The matrix exponential of so3mat

     Example Input:
     so3mat = np.array([[ 0, -3,  2],
     [ 3,  0, -1],
     [-2,  1,  0]])
     Output:
     np.array([[-0.69492056,  0.71352099,  0.08929286],
     [-0.19200697, -0.30378504,  0.93319235],
     [ 0.69297817,  0.6313497 ,  0.34810748]])

     */
    public static RMatrix MatrixExp3(RMatrix so3mat){
        var omgtheta = so3ToVec(so3mat);
        var w1 = omgtheta.get(0);
        var w2 = omgtheta.get(1);
        var w3 = omgtheta.get(2);
        var theta = AxisAng3(omgtheta).get(3);;
        var omghat = so3mat.divide(theta);
        if (w1.compareTo(ONE) > 0 || w2.compareTo(ONE) > 0 || w3.compareTo(ONE) > 0 ) {
            throw  new IllegalArgumentException("Illegal input, please check input parameters");
        }

        if(nearZero(normOfVector(omgtheta)) > 0){
            return getIdentityMatrix(3);
        } else {
            var I = getIdentityMatrix(3);
            var c = cos(theta, MC6);
            var oneMinusCos = ONE.subtract(c);
            var s = sin(theta, MC6);
            var cMultOmghat = omghat.mult(s);
            var omghatPow2 = omghat.mult(omghat);
            var oneMunusCosMultOmghatPow2 = omghatPow2.mult(oneMinusCos);
            var IplusCmultOmghat = I.plus(cMultOmghat);

            return IplusCmultOmghat.plus(oneMunusCosMultOmghatPow2);
        }
    }

    /**
     * log : R ∈ SO(3) → [ωˆ]θ ∈ so(3).
     * get Angel/Axis from SO(3)
     * input: none
     * return Matrix 4,0 with w1,w2,w3, angle
     *Computes the matrix logarithm of a rotation matrix
     *
     *     :param R: A 3x3 rotation matrix
     *     :return: The matrix logarithm of R
     *
     *     Example Input:
     *         R = np.array([[0, 0, 1],
     *                       [1, 0, 0],
     *                       [0, 1, 0]])
     *     Output:
     *         np.array([[          0, -1.20919958,  1.20919958],
     *                   [ 1.20919958,           0, -1.20919958],
     *                   [-1.20919958,  1.20919958,           0]])
     */
    public static RMatrix MatrixLog3(RMatrix rotationMatrixData){
        RVector axisAngle;
        var i = RMatrix.getIdentityMatrix(3);
        var trace = RMatrix.trace(rotationMatrixData);
        System.out.println("Trace is " + trace);
        axisAngle = new RVector(4);

        if (RMatrix.equalsContent(rotationMatrixData,i))
        {
            System.out.println("Case A matrix is Identity");
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());

            return RMatrix.getZerosMatrix(3);
        }

        else if (trace.compareTo(minusONE) == 0)
        {
            System.out.println("Case B Trace is -1");
            var r13 = rotationMatrixData.get(0,2);
            var r23 = rotationMatrixData.get(1,2);
            var r33 = rotationMatrixData.get(2,2);
            var onePlusR33 = r33.add(ONE);
            var TwoMultOnePlusR33 = onePlusR33.multiply(new BigDecimal("2"));
            var sqrtFromTwoMultOnePlusR33 = TwoMultOnePlusR33.sqrt(MC6);
            var p1 = ONE.divide(sqrtFromTwoMultOnePlusR33, MC6);
            var w1 = p1.multiply(r13, MC6);
            var w2 = p1.multiply(r23, MC6);
            var w3 = p1.multiply(onePlusR33, MC6);
            return VecToso3(new RVector(w1,w2,w3));
        }
        else
        {
            System.out.println("Case C");
            var traceMinusOne  = trace.subtract(ONE);
            var traceMinusOneDivTWO = traceMinusOne.divide(TWO, MC6);
            var angle = acos(traceMinusOneDivTWO, MC6);
            var sinA = sin(angle, MC6);
            var TwoSinA = sinA.multiply(TWO);
            var OneDivTwoSinA = ONE.divide(TwoSinA, MC6);

            var r21 = rotationMatrixData.get(2,1);
            var r12 = rotationMatrixData.get(1,2);
            var r02 = rotationMatrixData.get(0,2);
            var r20 = rotationMatrixData.get(2,0);
            var r10 = rotationMatrixData.get(1,0);
            var r01 = rotationMatrixData.get(0,1);

            var r21MinusR12 = r21.subtract(r12);
            var r02MinusR20 = r02.subtract(r20);
            var r10MinusR01 = r10.subtract(r01);

            var w1 = OneDivTwoSinA.multiply(r21MinusR12).round(MC6);
            var w2 = OneDivTwoSinA.multiply(r02MinusR20).round(MC6);
            var w3 = OneDivTwoSinA.multiply(r10MinusR01).round(MC6);
            var fourVector = new RVector(w1,w2,w3,angle);
            return VecToso3(AngleAxisToVec(fourVector));
        }
    }
}

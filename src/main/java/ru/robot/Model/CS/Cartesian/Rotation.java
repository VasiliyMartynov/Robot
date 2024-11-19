package ru.robot.Model.CS.Cartesian;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ujmp.core.Matrix;

import ru.robot.Model.DS.Base.RMatrix;
import ru.robot.Model.DS.Vector3;
import ru.robot.Model.DS.Vector4;
import ru.robot.Model.DS.RotationMatrix;
import ru.robot.Model.DS.SkewSymmetricMatrix;
import ru.robot.Model.Utils.AXIS;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static ch.obermuhlner.math.big.BigDecimalMath.*;
import static ru.robot.Model.DS.Base.RMatrix.*;
import static ru.robot.Model.DS.Base.RMatrix.mult;
import static ru.robot.Model.Utils.Utils.nearZero;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.Utils.Utils.*;
import static ru.robot.Model.DS.Vector3.normOfVector;
import static ru.robot.Model.DS.Vector3.normaliseVector;

public class Rotation {

    private static final Logger LOGGER = LogManager.getLogger();

    public static RotationMatrix rotate(RotationMatrix m, RotationMatrix n) {
        var z = mult(m.getData(), n.getData());
        return new RotationMatrix(roundValuesOfRMatrix(z));
    }

    public static RotationMatrix getRotationAroundFixedAxis(RotationMatrix currentRotation, BigDecimal angleInRad, AXIS axis) {
        var c = cos(angleInRad, MC6);
        var s = sin(angleInRad, MC6);
        var minusS = s.multiply(new BigDecimal("-1"), MC6);
        List<BigDecimal> itemList = List.of();
        switch (axis) {
            case X -> {
                itemList = Arrays.asList(ONE, ZERO, ZERO, ZERO, c, minusS, ZERO, s, c);
            }
            case Y -> {
                itemList = Arrays.asList(c, ZERO, s, ZERO, ONE, ZERO, minusS, ZERO, c);
            }
            case Z -> {
                itemList = Arrays.asList(c, minusS, ZERO, s, c, ZERO, ZERO, ZERO, ONE);
            }
        }
        return new RotationMatrix(mult(currentRotation.getData(), new RMatrix(itemList)));
    }


    public static RMatrix RotInv(RMatrix R){
        LOGGER.debug("RotInv has started");
        Matrix data = R.getData();
        LOGGER.debug("data \n '{}'", data);
        Matrix invMatrix = data.inv();
        LOGGER.debug("invMatrix \n '{}'", invMatrix);
        var result = new RMatrix(invMatrix);
        LOGGER.debug("result /n '{}'", result);
        LOGGER.debug("RotInv has finished");
        return result;
    }

    /**
     *Converts a 3-vector to an so(3) representation
     *      Example Input:
     *      <br> omg = np.array([1, 2, 3])
     *      <br>Output:
     *      * <br> [ 0, -3,  2]
     *      * <br> [ 3,  0, -1]
     *      * <br> [-2,  1,  0]
     *
     * @param omg A 3-vector
     * @return The skew symmetric representation of omg
     */
    public static SkewSymmetricMatrix VecToso3(Vector3 omg){
        LOGGER.debug("VecToso3 has STARTED");
        var v1 = omg.getData().get(0);
        LOGGER.debug("v1 '{}'", v1);
        var v2 = omg.getData().get(1);
        LOGGER.debug("v2 '{}'", v2);
        var v3 = omg.getData().get(2);
        LOGGER.debug("v3 '{}'", v3);
        List<BigDecimal> items = Arrays.asList(ZERO, minus(v3), v2,v3,ZERO,minus(v1),minus(v2),v1, ZERO);
        LOGGER.debug("List<BigDecimal> items '{}'", items);
        var result = new SkewSymmetricMatrix(new RMatrix(items));
        LOGGER.debug("result \n'{}'", result.getData());
        LOGGER.debug("VecToso3 FINISHED");
        return result;
    }


    /**
     * Converts an so(3) representation to a 3-vector
     *      <P>Example Input so3mat:
     *      <br>[ 0, -3,  2],
     *      <br>[ 3,  0, -1],
     *      <br>[-2,  1,  0]
     *      <P>Output: [1, 2, 3]
     * @see SkewSymmetricMatrix
     * @param so3mat A 3x3 skew-symmetric matrix
     * @return The 3-vector corresponding to so3mat
     */
    public static Vector3 so3ToVec(SkewSymmetricMatrix so3mat){
        LOGGER.debug("so3ToVec STARTED");
        var m1 = so3mat.getData().get(2,1);
        LOGGER.debug("m1 '{}'", m1);
        var m2 = so3mat.getData().get(0,2);
        LOGGER.debug("m2 '{}'", m2);
        var m3 = so3mat.getData().get(1,0);
        LOGGER.debug("m3 '{}'", m3);
        LOGGER.debug("so3ToVec FINISHED");
        return  new Vector3(m1, m2, m3);
    }

    /**
     *     Converts a 3-vector of exponential coordinates for rotation into
     *     axis-angle form
     *
     *     <P>Example Input:
     *     <br>  expc3 = np.array([1, 2, 3])
     *     <P>Output:
     *         <br> 0.26726124, 0.53452248, 0.80178373, 3.7416573867739413
     *     <br> return (Normalize(expc3), np.linalg.norm(expc3))
     * @param expc3  A 3-vector of exponential coordinates for rotation
     * @return The corresponding rotation angle
     */
    public static Vector4 AxisAng3(Vector3 expc3){
        Vector3 omghat = normaliseVector(expc3);
        return new Vector4(omghat.getData().get(0), omghat.getData().get(1),omghat.getData().get(2) , normOfVector(expc3));
    }

    /**
     *     Converts a 4-vector of axis-angle form to
     *     exponential coordinates for rotation
     *     <P>Example Input:
     *      <br>(np.array([0.26726124, 0.53452248, 0.80178373]), 3.7416573867739413)
     *     <P> Output:
     *     <br> expc3 = np.array([1, 2, 3])

     * @param  fourVector of exponential coordinates for rotation
     * @return result (Normalize(expc3), np.linalg.norm(expc3))
     */
    public static Vector3 AngleAxisToVec3(Vector4 fourVector){
        LOGGER.debug("AngleAxisToVec3 STARTED");
        LOGGER.debug("INPUT '{}'", fourVector.getData());
        var m1 = fourVector.getData().get(0);
        var m2 = fourVector.getData().get(1);
        var m3 = fourVector.getData().get(2);
        var m4 = fourVector.getData().get(3);
        var x1 = m1.multiply(m4, MC6);
        LOGGER.debug("x1 '{}'", x1);
        var x2 = m2.multiply(m4, MC6);
        LOGGER.debug("x2 '{}'", x2);
        var x3 = m3.multiply(m4, MC6);
        LOGGER.debug("x3 '{}'", x3);
        LOGGER.debug("AngleAxisToVec3 FINISHED");
        return  new Vector3(x1, x2, x3);
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
    public static RMatrix MatrixExp3(SkewSymmetricMatrix so3mat){
        var omgtheta = so3ToVec(so3mat);
        var theta = AxisAng3(omgtheta).getData().get(3);
        var omghat = so3mat.getData().divide(theta);


        if(nearZero(normOfVector(omgtheta)) < 0){
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
            var res = IplusCmultOmghat.plus(oneMunusCosMultOmghatPow2);
            var result = roundValuesOfRMatrix(res);

            return result;
        }
    }

    /**
     *Computes the matrix logarithm of a rotation matrix
     *           <P> Example Input:
     *               <br> [[0, 0, 1],
     *               <br> [1, 0, 0],
     *               <br> [0, 1, 0]
     *           <P> Output:
     *               <br> [          0, -1.20919958,  1.20919958],
     *               <br> [ 1.20919958,           0, -1.20919958],
     *               <br> [-1.20919958,  1.20919958,           0]])
     *
     * @param rotationMatrixData R: A 3x3 rotation matrix
     * @return The matrix logarithm of R
     */
    public static SkewSymmetricMatrix MatrixLog3(RotationMatrix rotationMatrixData){
        LOGGER.info("MatrixLog3 started");
        Vector4 axisAngle = null;
        var i = getIdentityMatrix(3);
        var trace = RMatrix.trace(rotationMatrixData.getData());
        LOGGER.debug("Trace is: `{}`" , trace);


        if (RMatrix.equalsContent(rotationMatrixData.getData(),i))
        {
            LOGGER.debug("Case A matrix is Identity");
            LOGGER.debug("vectorAxisOmega is `{}`", axisAngle);

            return new SkewSymmetricMatrix(RMatrix.getZerosMatrix(3));
        }

        else if (trace.compareTo(minus(ONE)) == 0)
        {
            LOGGER.debug("Case B Trace is -1");
            var r13 = rotationMatrixData.getData().get(0,2);
            var r23 = rotationMatrixData.getData().get(1,2);
            var r33 = rotationMatrixData.getData().get(2,2);
            var onePlusR33 = r33.add(ONE);
            var TwoMultOnePlusR33 = onePlusR33.multiply(new BigDecimal("2"));
            var sqrtFromTwoMultOnePlusR33 = TwoMultOnePlusR33.sqrt(MC6);
            var p1 = ONE.divide(sqrtFromTwoMultOnePlusR33, MC6);
            var w1 = p1.multiply(r13, MC6);
            var w2 = p1.multiply(r23, MC6);
            var w3 = p1.multiply(onePlusR33, MC6);
            return VecToso3(new Vector3(w1,w2,w3));
        }
        else
        {
            LOGGER.debug("Case C");
            var traceMinusOne  = trace.subtract(ONE);
            LOGGER.debug("traceMinusOne '{}'", traceMinusOne);
            var traceMinusOneDivTWO = traceMinusOne.divide(TWO, MC6);
            LOGGER.debug("traceMinusOneDivTWO '{}'", traceMinusOneDivTWO);
            var angle = acos(traceMinusOneDivTWO, MC6);
            LOGGER.debug("angle '{}'", angle);
            var sinA = sin(angle, MC6);
            LOGGER.debug("sinA '{}'", sinA);
            var TwoSinA = sinA.multiply(TWO);
            LOGGER.debug("TwoSinA '{}'", TwoSinA);
            var OneDivTwoSinA = ONE.divide(TwoSinA, MC6);
            LOGGER.debug("OneDivTwoSinA '{}'", OneDivTwoSinA);
            var r21 = rotationMatrixData.getData().get(2,1);
            LOGGER.debug("r21 '{}'", r21);
            var r12 = rotationMatrixData.getData().get(1,2);
            LOGGER.debug("r12 '{}'", r12);
            var r02 = rotationMatrixData.getData().get(0,2);
            LOGGER.debug("r02 '{}'", r02);
            var r20 = rotationMatrixData.getData().get(2,0);
            LOGGER.debug("r20 '{}'", r20);
            var r10 = rotationMatrixData.getData().get(1,0);
            LOGGER.debug("r10 '{}'", r10);
            var r01 = rotationMatrixData.getData().get(0,1);
            LOGGER.debug("r01 '{}'", r01);

            var r21MinusR12 = r21.subtract(r12);
            LOGGER.debug("r21MinusR12 '{}'", r21MinusR12);
            var r02MinusR20 = r02.subtract(r20);
            LOGGER.debug("r02MinusR20 '{}'", r02MinusR20);
            var r10MinusR01 = r10.subtract(r01);
            LOGGER.debug("r10MinusR01 '{}'", r10MinusR01);

            var w1 = OneDivTwoSinA.multiply(r21MinusR12).round(MC6);
            LOGGER.debug("w1 '{}'", w1);
            var w2 = OneDivTwoSinA.multiply(r02MinusR20).round(MC6);
            LOGGER.debug("w2 '{}'", w2);
            var w3 = OneDivTwoSinA.multiply(r10MinusR01).round(MC6);
            LOGGER.debug("w3 '{}'", w3);
            var fourVector = new Vector4(w1,w2,w3,angle);
            LOGGER.debug("fourVector '{}'", fourVector.getData());
            return VecToso3(AngleAxisToVec3(fourVector));
        }
    }
}

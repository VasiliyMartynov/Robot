package ru.robot.Model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.robot.Model.DS.*;
import ru.robot.Model.DS.Base.RMatrix;
import ru.robot.Model.DS.Base.RVector;

import java.math.BigDecimal;

import static ch.obermuhlner.math.big.BigDecimalMath.acos;
import static ch.obermuhlner.math.big.BigDecimalMath.tan;
import static ch.obermuhlner.math.big.DefaultBigDecimalMath.cos;
import static ch.obermuhlner.math.big.DefaultBigDecimalMath.sin;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.Rotation.*;
import static ru.robot.Model.Utils.Utils.minus;
import static ru.robot.Model.Utils.Utils.nearZero;
import static ru.robot.Model.DS.Base.RMatrix.*;
import static ru.robot.Model.DS.Base.RVector.*;
import static ru.robot.Model.DS.Vector3.*;
import static ru.robot.Model.Utils.YESNO.*;

public class Motion {

    private static final Logger LOGGER = LogManager.getLogger();

    RMatrix data;

    public Motion(){
        var T = getZerosMatrix(4);
        T.setItem(ONE, 3,3);
        this.data = T;
    }

    public RMatrix getData() {
        return data;
    }

    /**
     *
     *       Converts a rotation matrix and a position vector into homogeneous transformation matrix

     *      <p>example Input:
     *       <br>R = ma
     *       <br>[1, 0,  0],
     *       <br>[0, 0, -1],
     *       <br>[0, 1,  0]
     *       <br>p = np.array([1, 2, 5])
     *       <p>Output:
     *       <br>[1, 0,  0, 1],
     *       <br>[0, 0, -1, 2],
     *       <br>[0, 1,  0, 5],
     *       <br>[0, 0,  0, 1]
     *
     * @param rotationMatrix A 3x3 rotation matrix
     * @param position A 3-vector
     * @return A homogeneous transformation matrix corresponding to the inputs
     */
    //test OK
    public static RMatrix RpToTrans(RMatrix rotationMatrix, RVector position) {
        LOGGER.info("RpToTrans has started");
        var T = new Motion();
        for (int i = 0; i < rotationMatrix.getSize(); i++) {
            for (int j = 0; j < rotationMatrix.getSize(); j++) {
                T.getData().setItem(rotationMatrix.get(i, j), i, j);
            }
        }
        for (int z = 0; z <= position.size(); z++) {
            T.getData().setItem(position.get(z), z, 3);
        }
        var result = T.getData();
//        LOGGER.debug("result /n '{}'", result);
        LOGGER.info("RpToTrans has finished");
        return result;
    }

    /**
     * Converts a homogeneous transformation matrix into a rotation matrix
     *      <P>Example Input:
     *      <br>T = np.array(
     *      <br>[[1, 0,  0, 0],
     *       <br>[0, 0, -1, 0],
     *       <br>[0, 1,  0, 3],
     *       <br>[0, 0,  0, 1]])
     *      <P> Output:
     *      <br>np.array
     *       <br>[1, 0,  0],
     *       <br>[0, 0, -1],
     *       <br>[0, 1,  0],
     *
     * @param T  A homogeneous transformation matrix
     * @return The corresponding rotation matrix,
     */
    //Test OK
    public static RMatrix TransToR(RMatrix T) {
        LOGGER.info("TransToR has started" );
//        LOGGER.debug("TransToR input T  \n'{}'", T );
        var R = new RMatrix(3);
        R.setData(T, YES, 0,0);
//        LOGGER.debug("TransToR result \n'{}' ", R.getData());
        LOGGER.info("TransToR has finished");
        return R;
    }

    /**
     * Converts a homogeneous transformation matrix into a position matrix
     *      <P>Example Input:
     *      <br>T = np.array(
     *      <br>[[1, 0,  0, 0],
     *       <br>[0, 0, -1, 0],
     *       <br>[0, 1,  0, 3],
     *       <br>[0, 0,  0, 1]])
     *      <P> Output:
     *      <br>np.array
     *       <br>[3, 0,  3],
     * @param T  A homogeneous transformation matrix
     * @return The corresponding rotation matrix,
     */
    //test OK
    public static Vector3 TransToP(RMatrix T) {
        LOGGER.debug("TransToP has STARTED");
        LOGGER.debug("INPUT Motion T '{}'", T);
        var P = new Vector3();
        for (int z = 0; z <= 2; z++) {
            P.setItem(z, T.get(z, 3));
        }
        LOGGER.debug("P '{}", P.getData());
        LOGGER.debug("TransToP has finished");
        return P;
    }

    /**
     * Inverts a homogeneous transformation matrix
     *      Uses the structure of transformation matrices to avoid taking a matrix
     *      inverse, for efficiency.
     *       <p>Example input:
     *      <br> T = np.array([[1, 0,  0, 0],
     *      <br> [0, 0, -1, 0],
     *      <br> [0, 1,  0, 3],
     *      <br> [0, 0,  0, 1]])
     *      <P>Output:
     *      <br> np.array([[1,  0, 0,  0],
     *      <br> [0,  0, 1, -3],
     *      <br> [0, -1, 0,  0],
     *      <br> [0,  0, 0,  1]])
     * @param T A homogeneous transformation matrix
     * @return The inverse of T
     */
    //test OK
    public static RMatrix TransInv(RMatrix T) {
        LOGGER.debug("TransInv has started");
        var R = TransToR(T);
        var P = TransToP(T);
        var Rinv = RotInv(R);
        var RinvMultP = mult(Rinv, P.getData());
        var minusRinvMultP = mult(RinvMultP, minus(ONE));
        LOGGER.debug("TransInv has finished");
        return RpToTrans(Rinv, minusRinvMultP);
    }

    /**
     * Converts a spatial velocity vector into a 4x4 matrix in se3
     *    <p> Example Input:
     *         <br>V = np.array([1, 2, 3, 4, 5, 6])
     *     <p>Output:
     *         <br>[ 0, -3,  2, 4]
     *         <br>[ 3,  0, -1, 5]
     *         <br>[-2,  1,  0, 6]
     *         <br>[ 0,  0,  0, 0]
     * @param V A 6-vector representing a spatial velocity
     * @return The 4x4 se3 representation of V
     */
    //testOK
    public static RMatrix VecTose3(Vector6 V){
        LOGGER.debug("VecTose3 has started" );
        var so3 = VecToso3(new Vector3(V.getItem(0), V.getItem(1), V.getItem(2)));
        var se3 = new RMatrix(4);
        se3.setData(so3.getData(), YES,0,0);
        se3.setItem(V.getItem(3), 0,3);
        se3.setItem(V.getItem(4), 1,3);
        se3.setItem(V.getItem(5), 2,3);
        se3.setItem(ONE,3,3);
        LOGGER.debug("se3 \n '{}'", se3);
        LOGGER.debug("VecTose3 has finished");
        return se3;
    }

    /**
     * Converts se3 matrix into a spatial velocity vector
     *     <P>Example Input:
     *     <br>    se3mat = np.array(<br>[ 0, -3,  2, 4],
     *     <br>                       [ 3,  0, -1, 5],
     *     <br>                       [-2,  1,  0, 6],
     *     <br>                       [ 0,  0,  0, 0]
     *     <P>Output:
     *        <br> np.array([1, 2, 3, 4, 5, 6])
     * @param se3mat A 4x4 matrix in se3
     * @return The spatial velocity 6-vector corresponding to se3mat
     */
    //Test OK
    public static Vector6 se3ToVec(RMatrix se3mat){
        return new Vector6(
                se3mat.get(2,1),
                se3mat.get(0,2),
                se3mat.get(1,0),
                se3mat.get(0,3),
                se3mat.get(1,3),
                se3mat.get(2,3));
    }

    /**
     * Computes the adjoint representation of a homogeneous transformation matrix
     *     <P>Example Input:
     *        <br> T = np.array<br>[1, 0,  0, 0],
     *            <br>           [0, 0, -1, 0],
     *                <br>       [0, 1,  0, 3],
     *                    <br>   [0, 0,  0, 1]
     *     <P>Output:
     *         <br>np.array<br>[1, 0,  0, 0, 0,  0],
     *             <br>      [0, 0, -1, 0, 0,  0],
     *                 <br>  [0, 1,  0, 0, 0,  0],
     *                   <br>[0, 0,  3, 1, 0,  0],
     *                   <br>[3, 0,  0, 0, 0, -1],
     *                   <br>[0, 0,  0, 0, 1,  0]
     * @param T A homogeneous transformation matrix
     * @return The 6x6 adjoint representation [AdT] of T
     */
    //Test OK
    public static RMatrix Adjoint(RMatrix T){
        var R = TransToR(T);
        var P = TransToP(T);
        var result = new RMatrix(6);
        result.setData(R, YES, 0,0);
        result.setData(R, YES, 3,3);
        var m = VecToso3(P).getData().mult(R);
        result.setData(m, YES, 3, 0);
        return result;
    }

    /**
     * Takes a parametric description of a screw axis and converts it to normalized screw axis
     *     Example Input:
     *         q = np.array([3, 0, 0])
     *         s = np.array([0, 0, 1])
     *         h = 2
     *     Output:
     *         np.array([0, 0, 1, 0, -3, 2])
     * @param q A point lying on the screw axis
     * @param s A unit vector in the direction of the screw axis
     * @param h The pitch of the screw axis
     * @return A normalized screw axis described by the inputs
     */
    //Test OK
    public static Vector6 ScrewToAxis(Vector3 q, Vector3 s, BigDecimal h){
        LOGGER.debug("ScrewToAxis has started" );
        var v = new RVector(6);
        v.set(0,s.getItem(0));
        v.set(1,s.getItem(1));
        v.set(2,s.getItem(2));
        var crossQS = crossProduct(q,s);
        var dotHS = times(s,h);
        var crossQSplusDotHS = plus(crossQS,dotHS);
        v.set(3,crossQSplusDotHS.getItem(0));
        v.set(4,crossQSplusDotHS.getItem(1));
        v.set(5,crossQSplusDotHS.getItem(2));
        var result = new Vector6(v);
        LOGGER.debug("ScrewToAxis has finished" );
        return result;
    }

    /**
     * Converts a 6-vector of exponential coordinates into screw axis-angle orm
     *     <p>Example Input:
     *         <br>expc6 = np.array([1, 0, 0, 1, 2, 3])
     *     <p>Output:
     *         (<br>np.array(
     *         <br>[1.0, 0.0, 0.0, 1.0, 2.0, 3.0]), 1.0)
     * @param expc6 A 6-vector of exponential coordinates for rigid-body motion S*theta
     * @return S, The corresponding normalized screw axis
     */
    public static Vector7 AxisAng6(Vector6 expc6) {
        LOGGER.debug("AxisAng6toS has started" );
        var theta = normOfVector(new Vector3(
                expc6.getItem(0),
                expc6.getItem(1),
                expc6.getItem(2)
        ));
        if (nearZero(theta) < 0 ) {
            theta = normOfVector(new Vector3(
                    expc6.getItem(3),
                    expc6.getItem(4),
                    expc6.getItem(5)
            ));
        }
        var v = Vector6.divide(expc6, theta);
        var result = new Vector7();
        result.setItem(0, v.getItem(0));
        result.setItem(1, v.getItem(1));
        result.setItem(2, v.getItem(2));
        result.setItem(3, v.getItem(3));
        result.setItem(4, v.getItem(4));
        result.setItem(5, v.getItem(5));
        result.setItem(6, theta);
        LOGGER.debug("AxisAng6toS has finished" );
        return result;
    }

    /**
     * Computes the matrix exponential of se3 representation of exponential coordinates
     *     <P>Example Input:
     *         <br>se3mat = <br>[0,          0,           0,          0],
     *                  <br>[0,          0, -1.57079632, 2.35619449],
     *                  <br>[0, 1.57079632,           0, 2.35619449],
     *                  <br>[0,          0,           0,          0]
     *     <P>Output:
     *        <br>[1.0, 0.0,  0.0, 0.0],
     *        <br>[0.0, 0.0, -1.0, 0.0],
     *        <br>[0.0, 1.0,  0.0, 3.0],
     *        <br>[  0,   0,    0,   1]
     * @param se3mat A matrix of exponential coordinates
     * @return The matrix(size 4) exponential of se3mat
     */
    public static RMatrix MatrixExp6(RMatrix se3mat){
        LOGGER.info("========================");
        LOGGER.info("MatrixExp6 has started" );
        var R = TransToR(se3mat);
        LOGGER.debug("MatrixExp6 R \n'{}'", R.getData());
        var P = TransToP(se3mat);
        LOGGER.debug("MatrixExp6 P '{}'", P.getData());
        var so3mat = new SkewSymmetricMatrix(R);
        var omgTheta = so3ToVec(so3mat);
        LOGGER.debug("MatrixExp6 omgTheta '{}'", omgTheta.getData());
        var res = new RMatrix(4);
        if (nearZero(normOfVector(omgTheta)) < 0.00001) {
            res.setData(getIdentityMatrix(3), YES,0,0);
            res.setItem(P.getItem(0), 0,3);
            res.setItem(P.getItem(1), 1,3);
            res.setItem(P.getItem(2), 2,3);
            res.setItem(ONE, 3,3);
        } else {
            var me3 = MatrixExp3(so3mat);
            LOGGER.debug("MatrixExp6 me3 '{}'", me3.getData());
            var theta = AxisAng3(omgTheta).getItem(3);
            LOGGER.debug("MatrixExp6 theta '{}'", theta);
            var omgMat = divide(so3mat.getData(), theta);
            LOGGER.debug("MatrixExp6 omgMat '{}'", omgMat);
            var p1 = getIdentityMatrix(3).mult(theta);
            LOGGER.debug("MatrixExp6 p1(getIdentityMatrix(3).mult(theta);) '{}'", p1);
            var OneMinusCosTheta = ONE.subtract(cos(theta));
            var p2 = mult(omgMat, OneMinusCosTheta);
            LOGGER.debug("MatrixExp6 p2(mult(omgMat, OneMinusCosTheta)) '{}'", p2);
            var thetaMinusSinTheta = theta.subtract(sin(theta));
            var omgMatPow2 = mult(omgMat, omgMat);
            LOGGER.debug("MatrixExp6 omgHatPow2 '{}'", omgMatPow2);
            var p3 = mult(omgMatPow2, thetaMinusSinTheta);
            LOGGER.debug("MatrixExp6 p3(mult(omgHatPow2, thetaMinusSinTheta)) '{}'", p3);
            var p1PlusP2 = p1.plus(p2);
            LOGGER.debug("MatrixExp6 p1PlusP2 '{}'", p1PlusP2);
            var p1PlusP2PlusP3 = p1PlusP2.plus(p3);
            LOGGER.debug("MatrixExp6 p1PlusP2PlusP3 '{}'", p1PlusP2PlusP3);
            LOGGER.debug("MatrixExp6 P '{}'", P.getData());
            var p4 = mult(p1PlusP2PlusP3, P.getData());

//            LOGGER.debug("MatrixExp6 p4(mult(p1PlusP2PlusP3, P.getData());) '{}'", p4);
            var p5 = divide(p4, theta);
//            LOGGER.debug("MatrixExp6 p5(divide(p4, theta)) '{}'", p5);
            var GthetaV = new Vector3(p5);
            LOGGER.debug("MatrixExp6 GthetaV '{}'", GthetaV.getData());
            res.setItem(GthetaV.getItem(0), 0,3);
            res.setItem(GthetaV.getItem(1), 1,3);
            res.setItem(GthetaV.getItem(2), 2,3);
            res.setData(me3, YES, 0,0);
            res.setItem(ONE, 3,3);
        }
        var result = roundValuesOfRMatrix(res);
        LOGGER.debug("MatrixExp6 result '{}'", result.getData());
        LOGGER.info("MatrixExp6 has finished" );
        LOGGER.info("========================");
        return result;
    }


    /**
     * Computes the matrix logarithm of a homogeneous transformation matrix
     *     <p>Example Input:
     *      <br>   [1, 0,  0, 0],
     *      <br>   [0, 0, -1, 0],
     *      <br>   [0, 1,  0, 3],
     *      <br>   [0, 0,  0, 1]
     *     <p>Output:
     *         <br>[0,          0,           0,           0]
     *         <br>[0,          0, -1.57079633,  2.35619449]
     *         <br>[0, 1.57079633,           0,  2.35619449]
     *         <br>[0,          0,           0,           0]
     * @param T A matrix in SE3
     * @return The matrix logarithm of T
     */
    public static RMatrix MatrixLog6(RMatrix T){
        LOGGER.info("========================");
        LOGGER.info("MatrixLog6 has started" );
        var R = TransToR(T);
        //var P = TransToP(T);
        var omgMat = MatrixLog3(new RotationMatrix(R));
        var result = getIdentityMatrix(4);
        var zeroMatrix = getZerosMatrix(3);
        if (equalsContent(omgMat.getData(), zeroMatrix)) {
            LOGGER.info("MatrixLog6 has equalsContent(omgMat.getData(), zeroMatrix) it TRUE" );
            LOGGER.info("CASE A" );
            result.setData(zeroMatrix, YES, 0,0);
            result.setItem(T.get(0,3), 0,3);
            result.setItem(T.get(1,3), 1,3);
            result.setItem(T.get(2,3), 2,3);
            result.setItem(ZERO,3,0);
            result.setItem(ZERO,3,1);
            result.setItem(ZERO,3,2);
            result.setItem(ZERO,3,3);
        } else {
            LOGGER.info("MatrixLog6 has equalsContent(omgMat.getData(), zeroMatrix) it FALSE" );
            LOGGER.info("CASE B" );
            var Rtrace = BigDecimal.valueOf(R.getData().trace());
            LOGGER.info("MatrixLog6 Rtrace '{}'", Rtrace );
            var RtraceMinusOne = Rtrace.subtract(ONE, MC6);
            LOGGER.info("MatrixLog6 RtraceMinusOne '{}'", RtraceMinusOne );
            var RtraceMinusOneDivide2 = RtraceMinusOne.divide(TWO, MC6);
            LOGGER.info("MatrixLog6 RtraceMinusOneDivide2 '{}'", RtraceMinusOneDivide2 );
            var theta = acos(RtraceMinusOneDivide2, MC6);
            LOGGER.info("MatrixLog6 theta '{}'", theta );
            result.setData(omgMat.getData(), YES, 0, 0);
            result.setItem(ZERO,3,0);
            result.setItem(ZERO,3,1);
            result.setItem(ZERO,3,2);
            result.setItem(ZERO,3,3);
            LOGGER.info("MatrixLog6 result \n'{}'", result.getData() );

            //np.eye(3) - omgMat / 2.0
            var omgMatDiv2 = omgMat.getData().divide(TWO);
            var eye3 = getIdentityMatrix(3);
            var p1 = substract(eye3, omgMatDiv2);
            LOGGER.info("MatrixLog6 p1 \n'{}'", p1.getData() );

            //1.0 / theta - 1.0 / np.tan(theta / 2.0) / 2
            var oneDivTheta = ONE.divide(theta, MC6);
            var thetaDiv2 = theta.divide(TWO, MC6);
            var tanThetaDiv2 = tan(thetaDiv2, MC6);
            var oneDivTanThetaDiv2 = ONE.divide(tanThetaDiv2, MC6);
            var oneDivTanThetaDiv2andDiv2 = oneDivTanThetaDiv2.divide(TWO, MC6);
            var p2 = oneDivTheta.subtract(oneDivTanThetaDiv2andDiv2);
            LOGGER.info("MatrixLog6 p2 \n'{}'", p2 );

            //np.dot(omgMat,omgMat) / theta,
            var omgMatPow2 = mult(omgMat.getData(), omgMat.getData());
            var p3 = omgMatPow2.divide(theta);
            LOGGER.info("MatrixLog6 p3 \n'{}'", p3.getData() );


            var p2multP3 = p3.mult(p2);
            var p1PlusP2multP3 = p1.plus(p2multP3);

            var p4 = new RVector(3);
            p4.set(0, T.get(0,3));
            p4.set(1, T.get(1,3));
            p4.set(2, T.get(2,3));
            LOGGER.info("MatrixLog6 p4 \n'{}'", p4.getData() );

            var p5 = mult(p1PlusP2multP3, p4);
            LOGGER.info("MatrixLog6 p5 \n'{}'", p5.getData() );

            result.setItem(p5.get(0), 0,3);
            result.setItem(p5.get(1), 1,3);
            result.setItem(p5.get(2), 2,3);

        }
        LOGGER.info("MatrixLog6 result \n'{}'", result.getData() );
        LOGGER.info("MatrixLog6 has finished" );
        LOGGER.info("========================");
        return result;
    }





}

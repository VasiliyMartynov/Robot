package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.robot.Model.DataStructure.Base.RMatrix;
import ru.robot.Model.DataStructure.Base.RVector;
import ru.robot.Model.DataStructure.Vector3;
import ru.robot.Model.DataStructure.Vector6;

import static ru.robot.Environment.Global.ONE;
import static ru.robot.Environment.Global.minusONE;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.Rotation.RotInv;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.Rotation.VecToso3;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.YESNO.NO;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.YESNO.YES;
import static ru.robot.Model.DataStructure.Base.RMatrix.*;
import static ru.robot.Model.DataStructure.Base.RVector.*;

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
     *       Converts a rotation matrix and a position vector into homogeneous
     *      transformation matrix

     *      example Input:
     *       R = np.array([[1, 0,  0],
     *       [0, 0, -1],
     *       [0, 1,  0]])
     *       p = np.array([1, 2, 5])
     *       Output:
     *       np.array([[1, 0,  0, 1],
     *       [0, 0, -1, 2],
     *         [0, 1,  0, 5],
     *       [0, 0,  0, 1]])
     *
     * @param rotationMatrix A 3x3 rotation matrix
     * @param position A 3-vector
     * @return A homogeneous transformation matrix corresponding to the inputs
     */
    //test OK
    public static RMatrix RpToTrans(RMatrix rotationMatrix, RVector position) {
        LOGGER.debug("RpToTrans has started");
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
        LOGGER.debug("result /n '{}'", result);
        LOGGER.debug("RpToTrans has finished");
        return result;
    }

    /**
     * * Converts a homogeneous transformation matrix into a rotation matrix
     *      * Example Input:
     *      * T = np.array([[1, 0,  0, 0],
     *      * [0, 0, -1, 0],
     *      * [0, 1,  0, 3],
     *      * [0, 0,  0, 1]])
     *      * Output:
     *      * (np.array([[1, 0,  0],
     *      * [0, 0, -1],
     *      * [0, 1,  0]]),
     *      * np.array([0, 0, 3]))
     * @param T  A homogeneous transformation matrix
     * @return The corresponding rotation matrix,
     */
    //Test OK
    public static RMatrix TransToR(RMatrix T) {
        LOGGER.debug("TransToR has started" );
        LOGGER.debug("TransToR input T '{}' \n", T );
        var R = new RMatrix(3);
        R.setData(T, YES, 0,0);
        LOGGER.debug("TransToR result '{}' \n", R.getData());
        LOGGER.debug("TransToR has finished");
        return R;
    }

    /**
     *  Converts a homogeneous transformation matrix into position vector
     *      * Example Input:
     *      * T = np.array([[1, 0,  0, 0],
     *      * [0, 0, -1, 0],
     *      * [0, 1,  0, 3],
     *      * [0, 0,  0, 1]])
     *      * Output:
     *      * (np.array([[1, 0,  0],
     *      * [0, 0, -1],
     *      * [0, 1,  0]]),
     *      * np.array([0, 0, 3]))
     * @param T A homogeneous transformation matrix
     * @return The corresponding position vector.
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
     *      * Uses the structure of transformation matrices to avoid taking a matrix
     *      * inverse, for efficiency.
     *      * <p>
     *      * Example input:
     *      * T = np.array([[1, 0,  0, 0],
     *      * [0, 0, -1, 0],
     *      * [0, 1,  0, 3],
     *      * [0, 0,  0, 1]])
     *      * Output:
     *      * np.array([[1,  0, 0,  0],
     *      * [0,  0, 1, -3],
     *      * [0, -1, 0,  0],
     *      * [0,  0, 0,  1]])
     *      * """
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
        var munusRinvMultP = mult(RinvMultP, minusONE);
        LOGGER.debug("TransInv has finished");
        return RpToTrans(Rinv, munusRinvMultP);
    }

    /**
     * Converts a spatial velocity vector into a 4x4 matrix in se3
     *     Example Input:
     *         V = np.array([1, 2, 3, 4, 5, 6])
     *     Output:
     *         <p>[ 0, -3,  2, 4]
     *         <p>[ 3,  0, -1, 5]
     *         <p>[-2,  1,  0, 6]
     *         <p>[ 0,  0,  0, 0]
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
        LOGGER.debug("VecTose3 has shinished");
        return se3;
    }

    /**
     * Converts an se3 matrix into a spatial velocity vector
     *     Example Input:
     *         se3mat = np.array([[ 0, -3,  2, 4],
     *                            [ 3,  0, -1, 5],
     *                            [-2,  1,  0, 6],
     *                            [ 0,  0,  0, 0]])
     *     Output:
     *         np.array([1, 2, 3, 4, 5, 6])
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
     *     Example Input:
     *         T = np.array([[1, 0,  0, 0],
     *                       [0, 0, -1, 0],
     *                       [0, 1,  0, 3],
     *                       [0, 0,  0, 1]])
     *     Output:
     *         np.array([[1, 0,  0, 0, 0,  0],
     *                   [0, 0, -1, 0, 0,  0],
     *                   [0, 1,  0, 0, 0,  0],
     *                   [0, 0,  3, 1, 0,  0],
     *                   [3, 0,  0, 0, 0, -1],
     *                   [0, 0,  0, 0, 1,  0]]
     * @param T A homogeneous transformation matrix
     * @return The 6x6 adjoint representation [AdT] of T
     */
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

}

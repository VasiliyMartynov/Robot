package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.robot.Model.DataStructure.Base.RMatrix;
import ru.robot.Model.DataStructure.Base.RVector;
import ru.robot.Model.DataStructure.Vector3;
import ru.robot.Model.DataStructure.Vector6;

import static ru.robot.Environment.Global.ONE;
import static ru.robot.Environment.Global.minusONE;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.Rotation.MatrixExp3;
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
        T.set(ONE, 3,3);
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
        var T = new Motion();
        for (int i = 0; i < rotationMatrix.getSize(); i++) {
            for (int j = 0; j < rotationMatrix.getSize(); j++) {
                T.getData().set(rotationMatrix.get(i, j), i, j);
            }
        }
        for (int z = 0; z <= position.size(); z++) {
            T.getData().set(position.get(z), z, 3);
        }
        return T.getData();
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
        return setMatrixValues(T, NO, 3);
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
        LOGGER.debug("TransToP START");
        LOGGER.debug("INPUT Motion T '{}'", T);
        var P = new Vector3();
        for (int z = 0; z <= 2; z++) {
            LOGGER.debug("T.get '{}'", T.get(z, 3));
            P.setItem(z, T.get(z, 3));
        }
        LOGGER.debug("P '{}", P.getData());
        LOGGER.debug("TransToP END");
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
        System.out.println("T " + T);
        var R = TransToR(T);
        System.out.println("R " + R);
        var P = TransToP(T);
        System.out.println("P " + P);
        var Rinv = RotInv(R);
        var RinvMultP = mult(Rinv, P.getData());
        var munusRinvMultP = mult(RinvMultP, minusONE);
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
        var so3 = VecToso3(new Vector3(V.getItem(0), V.getItem(1), V.getItem(2)));
        var se3 = setMatrixValues(so3.getData(), YES, 4);
        se3.set(V.getItem(3), 0,3);
        se3.set(V.getItem(4), 1,3);
        se3.set(V.getItem(5), 2,3);
        se3.set(ONE,3,3);
        return se3;
    }

}

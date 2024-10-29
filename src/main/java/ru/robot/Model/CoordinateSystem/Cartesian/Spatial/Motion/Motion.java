package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion;

import ru.robot.Model.CoordinateSystem.Cartesian.Utils.YESNO;
import ru.robot.Model.DataStructure.RMatrix;
import ru.robot.Model.DataStructure.RVector;

import static ru.robot.Model.DataStructure.RMatrix.*;

public class Motion {

    /**
     * Converts a rotation matrix and a position vector into homogeneous
     *     transformation matrix
     *
     *     :param R: A 3x3 rotation matrix
     *     :param p: A 3-vector
     *     :return: A homogeneous transformation matrix corresponding to the inputs
     *
     *     Example Input:
     *         R = np.array([[1, 0,  0],
     *                       [0, 0, -1],
     *                       [0, 1,  0]])
     *         p = np.array([1, 2, 5])
     *     Output:
     *         np.array([[1, 0,  0, 1],
     *                   [0, 0, -1, 2],
     *                   [0, 1,  0, 5],
     *                   [0, 0,  0, 1]])
     */
    public static RMatrix RpToTrans(RMatrix rotationMatrix, RVector position){
        var T = getEmptyTransformationMatrix();

        for(int i = 0; i<rotationMatrix.getSize(); i++){
            for(int j = 0; j < rotationMatrix.getSize(); j++){
                T.set(rotationMatrix.get(i,j), i,j);
            }
        }

        for(int z = 0; z <= position.size(); z++){

            T.set(position.get(z),z,3);
        }
        return T;
    }

    /**
     *  Converts a homogeneous transformation matrix into a rotation matrix
     *     and position vector
     *
     *     :param T: A homogeneous transformation matrix
     *     :return R: The corresponding rotation matrix,
     *     :return p: The corresponding position vector.
     *
     *     Example Input:
     *         T = np.array([[1, 0,  0, 0],
     *                       [0, 0, -1, 0],
     *                       [0, 1,  0, 3],
     *                       [0, 0,  0, 1]])
     *     Output:
     *         (np.array([[1, 0,  0],
     *                    [0, 0, -1],
     *                    [0, 1,  0]]),
     *          np.array([0, 0, 3]))
     */
    public static RMatrix TrToRotationMatrix(RMatrix T){
        var R = setValues(T, YESNO.NO, 3);
        return R;
    }

    public static RVector TrToPositionVector(RMatrix T){
        var P = new RVector(3);
        for(int z = 0; z <= P.size(); z++){

            T.set(P.get(z),z,3);
        }
        return P;
    }

    /**
     * """Inverts a homogeneous transformation matrix
     *
     *     :param T: A homogeneous transformation matrix
     *     :return: The inverse of T
     *     Uses the structure of transformation matrices to avoid taking a matrix
     *     inverse, for efficiency.
     *
     *     Example input:
     *         T = np.array([[1, 0,  0, 0],
     *                       [0, 0, -1, 0],
     *                       [0, 1,  0, 3],
     *                       [0, 0,  0, 1]])
     *     Output:
     *         np.array([[1,  0, 0,  0],
     *                   [0,  0, 1, -3],
     *                   [0, -1, 0,  0],
     *                   [0,  0, 0,  1]])
     *     """
     */
    public static RMatrix TransInv(RMatrix T){

    }

}

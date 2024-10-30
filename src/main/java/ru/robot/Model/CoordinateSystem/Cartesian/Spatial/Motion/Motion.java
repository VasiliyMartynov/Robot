//package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion;
//
//import ru.robot.Model.CoordinateSystem.Cartesian.Utils.YESNO;
//import ru.robot.Model.DataStructure.Base.RMatrix;
//import ru.robot.Model.DataStructure.Base.RVector;
//import static ru.robot.Environment.Global.ONE;
//import static ru.robot.Environment.Global.minusONE;
//import static ru.robot.Model.DataStructure.Base.RMatrix.*;
//import static ru.robot.Model.DataStructure.Base.RVector.*;
//
//public class Motion {
//
//    public static RMatrix getEmptyTransformationMatrix(){
//        var T = getZerosMatrix(4);
//        T.set(ONE,3,3);
//        return T;
//    }
//
//    /**
//     * Converts a rotation matrix and a position vector into homogeneous
//     *     transformation matrix
//     *
//     *     :param R: A 3x3 rotation matrix
//     *     :param p: A 3-vector
//     *     :return: A homogeneous transformation matrix corresponding to the inputs
//     *
//     *     Example Input:
//     *         R = np.array([[1, 0,  0],
//     *                       [0, 0, -1],
//     *                       [0, 1,  0]])
//     *         p = np.array([1, 2, 5])
//     *     Output:
//     *         np.array([[1, 0,  0, 1],
//     *                   [0, 0, -1, 2],
//     *                   [0, 1,  0, 5],
//     *                   [0, 0,  0, 1]])
//     */
//
//    public static RMatrix RpToTrans(RMatrix rotationMatrix, RVector position){
//
//        var T = getEmptyTransformationMatrix();
//
//        for(int i = 0; i<rotationMatrix.getSize(); i++){
//            for(int j = 0; j < rotationMatrix.getSize(); j++){
//                T.set(rotationMatrix.get(i,j), i,j);
//            }
//        }
//        for(int z = 0; z <= position.size(); z++){
//            T.set(position.get(z),z,3);
//
//        }
//
//        return T;
//    }
//
//    /**
//     *  Converts a homogeneous transformation matrix into a rotation matrix
//     *
//     *     :param T: A homogeneous transformation matrix
//     *     :return R: The corresponding rotation matrix,
//     *
//     *     Example Input:
//     *         T = np.array([[1, 0,  0, 0],
//     *                       [0, 0, -1, 0],
//     *                       [0, 1,  0, 3],
//     *                       [0, 0,  0, 1]])
//     *     Output:
//     *         (np.array([[1, 0,  0],
//     *                    [0, 0, -1],
//     *                    [0, 1,  0]]),
//     *          np.array([0, 0, 3]))
//     */
//
//    public static RMatrix TransToR(RMatrix T){
//        return setValues(T, YESNO.NO, 3);
//    }
//
//    /**
//     *  Converts a homogeneous transformation matrix into position vector
//     *
//     *     :param T: A homogeneous transformation matrix
//     *     :return p: The corresponding position vector.
//     *
//     *     Example Input:
//     *         T = np.array([[1, 0,  0, 0],
//     *                       [0, 0, -1, 0],
//     *                       [0, 1,  0, 3],
//     *                       [0, 0,  0, 1]])
//     *     Output:
//     *         (np.array([[1, 0,  0],
//     *                    [0, 0, -1],
//     *                    [0, 1,  0]]),
//     *          np.array([0, 0, 3]))
//     */
//
//    public static RVector TransToP(RMatrix T){
//        //System.out.println("T " + T);
//        var P = new RVector(3);
//        //System.out.println("empty P\n" + P);
//        for(int z = 0; z <= P.size(); z++){
//            P.set(z, T.get(z,3));
//        }
//        //System.out.println("P "+ P);
//        return P;
//    }
//
//    /**
//     * """Inverts a homogeneous transformation matrix
//     *
//     *     :param T: A homogeneous transformation matrix
//     *     :return: The inverse of T
//     *     Uses the structure of transformation matrices to avoid taking a matrix
//     *     inverse, for efficiency.
//     *
//     *     Example input:
//     *         T = np.array([[1, 0,  0, 0],
//     *                       [0, 0, -1, 0],
//     *                       [0, 1,  0, 3],
//     *                       [0, 0,  0, 1]])
//     *     Output:
//     *         np.array([[1,  0, 0,  0],
//     *                   [0,  0, 1, -3],
//     *                   [0, -1, 0,  0],
//     *                   [0,  0, 0,  1]])
//     *     """
//     */
//
//    public static RMatrix TransInv(RMatrix T){
//        System.out.println("T " + T);
//        var R = TransToR(T);
//        System.out.println("R " + R);
//        var P = TransToP(T);
//        System.out.println("P " + P);
//        var Rinv = RotInv(R);
//        var RinvMultP = mult(Rinv, P);
//        var munusRinvMultP = mult(RinvMultP, minusONE);
//        return RpToTrans(Rinv, munusRinvMultP);
//    }
//
//}

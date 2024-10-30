//package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion;
//
//import org.junit.jupiter.api.Test;
//import ru.robot.Model.DataStructure.Base.RMatrix;
//import ru.robot.Model.DataStructure.Base.RVector;
//import java.util.Arrays;
//import static ru.robot.Environment.Global.*;
////import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion.Motion.*;
//
//public class MotionTest {
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
//    @Test
//    public void RpToTransTest(){
//        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minusONE, ZERO,ONE, ZERO));
//        var P = new RVector(ONE, TWO, FIVE);
//        var T = RpToTrans(R,P);
//        System.out.println(T);
//    }
//
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
//    @Test
//    void TransToPTest(){
//        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minusONE, ZERO,ONE, ZERO));
//        var P = new RVector(ZERO, ZERO, THREE);
//        var tr = RpToTrans(R,P);
//        var extractP = TransToP(tr);
//        System.out.println(extractP);
//
//
//    }
//
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
//    @Test
//    public void TransInvTest(){
//        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minusONE, ZERO,ONE, ZERO));
//        var P = new RVector(ZERO, ZERO, THREE);
//        var tr = RpToTrans(R,P);
//        var trInv = TransInv(tr);
//        System.out.println(trInv);
//
//    }
//
//}

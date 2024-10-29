package ru.robot.cartesian.spatial.Motion;

import org.junit.jupiter.api.Test;
import ru.robot.Model.DataStructure.RMatrix;
import ru.robot.Model.DataStructure.RVector;
import java.util.Arrays;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion.Motion.RpToTrans;

public class MotionTest {

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
    @Test
    public void RpToTransTest(){
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minusONE, ZERO,ONE, ZERO));
        var P = new RVector(ONE, TWO, FIVE);
        var T = RpToTrans(R,P);
        System.out.println(T);
    }

}

package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.robot.Model.DataStructure.Base.RMatrix;
import ru.robot.Model.DataStructure.Base.RVector;
import ru.robot.Model.DataStructure.Vector3;
import ru.robot.Model.DataStructure.Vector6;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Motion.Motion.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.minus;


public class MotionTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @BeforeEach
    void init(TestInfo testInfo) {
        LOGGER.info(testInfo.getDisplayName());
    }

    /**
     * Converts a rotation matrix and a position vector into homogeneous
     *     transformation matrix
     *     :param R: A 3x3 rotation matrix
     *     :param p: A 3-vector
     *     :return: A homogeneous transformation matrix corresponding to the inputs
     *    Input:
     *        <P>[1, 0,  0]</P>
     *                       <P>[0, 0, -1]</P>
     *                       <P>[0, 1,  0]</P>
     *        <P>Vector3 =[1, 2, 5])</P>
     *     <p>return:</p>
     *                   <p>[1, 0,  0, 1],
     *                   <p>[0, 0, -1, 2]
     *                   <p>[0, 1,  0, 5]
     *                   <p>[0, 0,  0, 1]
     */
    @Test
    public void RpToTransTest(){
        LOGGER.debug("RpToTransTest");
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minusONE, ZERO,ONE, ZERO));
        LOGGER.debug("R '{}", R);
        var P = new RVector(ONE, TWO, FIVE);
        LOGGER.debug("P '{}", P);
        var actual = RpToTrans(R,P);
        LOGGER.debug("actual '{}", actual);
        var expected = new RMatrix(Arrays.asList(
                ONE, ZERO,ZERO,ONE,
                ZERO, ZERO,minusONE,TWO,
                ZERO,ONE,ZERO,FIVE,
                ZERO,ZERO,ZERO,ONE));
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }
    }


    /**
     *  Converts a homogeneous transformation matrix into position vector
     *     :param T: A homogeneous transformation matrix
     *     :return p: The corresponding position vector.
     *     Example Input:
     *         T = np.array([[1, 0,  0, 0],
     *                       [0, 0, -1, 0],
     *                       [0, 1,  0, 3],
     *                       [0, 0,  0, 1]])<p>
     * return [0, 0, 3])
     */
    @Test
    void TransToPTest(){
        LOGGER.debug("TransToPTest");
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minusONE, ZERO,ONE, ZERO));
        var P = new RVector(ZERO, ZERO, THREE);
        var tr = RpToTrans(R,P);
        LOGGER.debug("test Motion '{}", tr);
        var actual = TransToP(tr);
        LOGGER.debug("actual '{}", actual.getData());
        var expected = new Vector3(ZERO,ZERO,THREE);
        assertEquals(expected.getData().get(0), actual.getItem(0));
        assertEquals(expected.getData().get(1), actual.getItem(1));
        assertEquals(expected.getData().get(2), actual.getItem(2));


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
     *     homogeneous transformation matrix
     *  The corresponding rotation matrix,
     */
    @Test
    void TransToRTest(){
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minusONE, ZERO,ONE, ZERO));
        var expected = R;
        var P = new RVector(ZERO, ZERO, THREE);
        var tr = RpToTrans(R,P);
        LOGGER.debug("test Motion '{}", tr);
        var actual = TransToR(tr);
        LOGGER.debug("actual \n'{}", actual.getData());
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }

    }


    /**
     * """Inverts a homogeneous transformation matrix
     *     :param T: A homogeneous transformation matrix
     *     :return: The inverse of T
     *     Uses the structure of transformation matrices to avoid taking a matrix
     *     inverse, for efficiency.
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
     */
    @Test
    public void TransInvTest(){
        var P = new RVector(ZERO, ZERO, THREE);
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minusONE, ZERO,ONE, ZERO));
        var tr = RpToTrans(R,P);
        LOGGER.debug("input test data \n'{}", tr);
        var trInv = TransInv(tr);
    }

    /**
     * Converts a spatial velocity vector into a 4x4 matrix in se3
     *     <P> Example Input:
     *        <P> [1, 2, 3, 4, 5, 6]
     *     <p>Output:
     *        <P> [ 0, -3,  2, 4]
     *         <p>[ 3,  0, -1, 5]
     *         <p>[-2,  1,  0, 6]
     *         <p>[ 0,  0,  0, 0]
     */
    @Test
    public void VecTose3Test(){
        var v6 = new Vector6(ONE, TWO, THREE, FOUR,FIVE,SIX);
        LOGGER.debug("input v6 '{}", v6.getData());
        var actual = VecTose3(v6);
        LOGGER.debug("actual '{}", actual);
        var expected = new RMatrix(Arrays.asList(
                ZERO, minus(THREE),TWO,FOUR,
                THREE, ZERO,minus(ONE),FIVE,
                minus(TWO),ONE,ZERO,SIX,
                ZERO,ZERO,ZERO,ONE));
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }
    }

    /**
     * Converts an se3 matrix into a spatial velocity vector
     *     :param se3mat: A 4x4 matrix in se3
     *     :return: The spatial velocity 6-vector corresponding to se3mat
     *     Example Input:
     *         se3mat = np.array([[ 0, -3,  2, 4],
     *                            [ 3,  0, -1, 5],
     *                            [-2,  1,  0, 6],
     *                            [ 0,  0,  0, 0]])
     *     Output:
     *         np.array([1, 2, 3, 4, 5, 6])
     */
    @Test
    public void se3ToVecTest(){
        LOGGER.debug("VecTose3Test");
        var se3mat = new RMatrix(Arrays.asList(
                ZERO, minus(THREE),TWO,FOUR,
                THREE, ZERO,minus(ONE),FIVE,
                minus(TWO),ONE,ZERO,SIX,
                ZERO,ZERO,ZERO,ZERO));
        LOGGER.debug("input se3mat \n'{}", se3mat.getData());
        var actual = se3ToVec(se3mat);
        LOGGER.debug("actual '{}", actual.getData());
        var expected = new Vector6(ONE, TWO, THREE, FOUR,FIVE,SIX);
        for(int i = 0; i < actual.getSize(); i++){
                assertEquals(expected.getItem(i), actual.getItem(i));

        }
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
     */
    @Test
    public void AdjointTest(){
        LOGGER.debug("AdjointTest");
        var T = new RMatrix(Arrays.asList(
                ONE, ZERO,ZERO,ZERO,
                ZERO, ZERO,minus(ONE),ZERO,
                ZERO,ONE,ZERO,THREE,
                ZERO,ZERO,ZERO,ONE));
        LOGGER.debug("Input T: '{}'", T);
        var actual = Adjoint(T);
        LOGGER.debug("actual '{}", actual);
        var expected = new RMatrix(Arrays.asList(
                ONE, ZERO,ZERO,ZERO,ZERO,ZERO,
                ZERO, ZERO,minus(ONE),ZERO,ZERO,ZERO,
                ZERO,ONE,ZERO,ZERO,ZERO,ZERO,
                ZERO,ZERO,ZERO,THREE,ONE,ZERO,ZERO,
                THREE, ZERO,ZERO,ZERO,ZERO,minus(ONE),
                ZERO,ZERO,ZERO,ZERO,ONE,ZERO));
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }
    }

}

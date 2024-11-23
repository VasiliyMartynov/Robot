package ru.robot.Model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.robot.Model.DS.Base.RMatrix;

import ru.robot.Model.DS.Vector3;
import ru.robot.Model.DS.Vector6;
import ru.robot.Model.DS.Vector7;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.Motion.*;
import static ru.robot.Model.Utils.Utils.minus;


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
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minus(ONE), ZERO,ONE, ZERO));
        LOGGER.debug("RpToTransTest R '{}", R);
        var P = new Vector3(ONE, TWO, FIVE);
        LOGGER.debug("RpToTransTest P '{}", P);
        var actual = RpToTrans(R,P.getData());
        LOGGER.debug("RpToTransTest actual '{}", actual);
        var expected = new RMatrix(Arrays.asList(
                ONE, ZERO,ZERO,ONE,
                ZERO, ZERO,minus(ONE),TWO,
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
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minus(ONE), ZERO,ONE, ZERO));
        var P = new Vector3(ZERO, ZERO, THREE);
        var tr = RpToTrans(R,P.getData());
        LOGGER.debug("TransToPTest input '{}", tr);
        var actual = TransToP(tr);
        LOGGER.debug("TransToPTest actual '{}", actual.getData());
        var expected = new Vector3(ZERO,ZERO,THREE);
        assertEquals(expected.getData().get(0), actual.getItem(0));
        assertEquals(expected.getData().get(1), actual.getItem(1));
        assertEquals(expected.getData().get(2), actual.getItem(2));


    }

    /**
     * Converts a homogeneous transformation matrix into a rotation matrix
     *   Example Input:
     *      [1, 0,  0, 0],
     *      [0, 0, -1, 0],
     *      [0, 1,  0, 3],
     *      [0, 0,  0, 1]
     * Output:
     *   [1, 0,  0],
     *   [0, 0, -1],
     *   [0, 1,  0]]),
     *   [0, 0, 3]))
     *     homogeneous transformation matrix
     *  The corresponding rotation matrix,
     */
    @Test
    void TransToRTest(){
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minus(ONE), ZERO,ONE, ZERO));
        var P = new Vector3(ZERO, ZERO, THREE);
        var tr = RpToTrans(R,P.getData());
        LOGGER.debug("TransToRTest input '{}", tr);
        var actual = TransToR(tr);
        LOGGER.debug("TransToRTest actual \n'{}", actual.getData());
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(R.getDouble(i,j), actual.getDouble(i,j));
            }
        }

    }


    /**
     * Inverts a homogeneous transformation matrix
     * Uses the structure of transformation matrices to avoid taking a matrix inverse, for efficiency.
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
        var P = new Vector3(ZERO, ZERO, THREE);
        var R = new RMatrix(Arrays.asList(ONE, ZERO, ZERO, ZERO,ZERO,minus(ONE), ZERO,ONE, ZERO));
        var tr = RpToTrans(R,P.getData());
        LOGGER.debug("TransInvTest input test data \n'{}", tr);
        var expected = new RMatrix(Arrays.asList(
                ONE, ZERO, ZERO, ZERO,
                ZERO, ZERO, ONE, minus(THREE),
                ZERO, minus(ONE), ZERO, ZERO,
                ZERO,ZERO,ZERO, ONE));
        LOGGER.debug("TransInvTest expected \n'{}", expected);
        var actual = TransInv(tr);
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }

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
        LOGGER.debug("VecTose3Test input v6 '{}", v6.getData());
        var actual = VecTose3(v6);
        LOGGER.debug("VecTose3Test actual '{}", actual);
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
        var se3mat = new RMatrix(Arrays.asList(
                ZERO, minus(THREE),TWO,FOUR,
                THREE, ZERO,minus(ONE),FIVE,
                minus(TWO),ONE,ZERO,SIX,
                ZERO,ZERO,ZERO,ZERO));
        LOGGER.debug("se3ToVecTest input se3mat \n'{}", se3mat.getData());
        var actual = se3ToVec(se3mat);
        LOGGER.debug("se3ToVecTest actual '{}", actual.getData());
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
        var T = new RMatrix(Arrays.asList(
                ONE, ZERO,ZERO,ZERO,
                ZERO, ZERO,minus(ONE),ZERO,
                ZERO,ONE,ZERO,THREE,
                ZERO,ZERO,ZERO,ONE));
        LOGGER.debug("AdjointTest Input T: '{}'", T);
        var actual = Adjoint(T);
        LOGGER.debug("AdjointTest actual '{}", actual.getData());
        var expected = new RMatrix(Arrays.asList(
                ONE, ZERO,ZERO,ZERO,ZERO,ZERO,
                ZERO, ZERO,minus(ONE),ZERO,ZERO,ZERO,
                ZERO,ONE,ZERO,ZERO,ZERO,ZERO,
                ZERO,ZERO,THREE,ONE,ZERO,ZERO,
                THREE, ZERO,ZERO,ZERO,ZERO,minus(ONE),
                ZERO,ZERO,ZERO,ZERO,ONE,ZERO));
        LOGGER.debug("AdjointTest expected '{}", expected.getData());
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }
    }

    /**
     * Takes a parametric description of a screw axis and converts it to normalized screw axis
     *     Example Input:
     *         q = np.array([3, 0, 0])
     *         s = np.array([0, 0, 1])
     *         h = 2
     *     Output:
     *         np.array([0, 0, 1, 0, -3, 2])
     */

    @Test
    public void ScrewToAxisTest(){
        var q = new Vector3(THREE, ZERO, ZERO);
        LOGGER.debug("ScrewToAxisTest input q\n'{}", q.getData());
        var s = new Vector3(ZERO, ZERO, ONE);
        LOGGER.debug("ScrewToAxisTest input s\n'{}", s.getData());
        var h = TWO;
        LOGGER.debug("ScrewToAxisTest input h\n'{}", h);
        var expected = new Vector6(ZERO, ZERO,ONE,ZERO,minus(THREE), TWO);
        LOGGER.debug("ScrewToAxisTest expected \n'{}", expected.getData());
        var actual = ScrewToAxis(q,s,h);
        LOGGER.debug("ScrewToAxisTest actual \n'{}", actual.getData());
        for(int i = 0; i < actual.getSize(); i++){
            assertEquals(expected.getItem(i).doubleValue(), actual.getItem(i).doubleValue());
        }
    }

    /**
     * Converts a 6-vector of exponential coordinates into screw axis-angle
     *     form
     *     Example Input:
     *         expc6 = np.array([1, 0, 0, 1, 2, 3])
     *     Output:
     *         (np.array([1.0, 0.0, 0.0, 1.0, 2.0, 3.0]), 1.0)
     */
    @Test
    public void AxisAng6Test(){
        var expc6 = new Vector6(ONE, ZERO, ZERO, ONE,TWO,THREE);
        LOGGER.debug("AxisAng6 input \n'{}", expc6.getData());
        var expected = new Vector7(ONE, ZERO, ZERO, ONE,TWO,THREE, ONE);
        LOGGER.debug("AxisAng6 expected \n'{}", expected.getData());
        var actual = AxisAng6(expc6);
        LOGGER.debug("AxisAng6Test actual \n'{}", actual.getData());
        for(int i = 0; i < actual.getSize(); i++){
            assertEquals(expected.getItem(i), actual.getItem(i));
        }
    }

    /**
     * Computes the matrix exponential of an se3 representation of exponential coordinates
     *     Example Input:
     *         se3mat = [0,          0,           0,          0],
     *                  [0,          0, -1.57079632, 2.35619449],
     *                  [0, 1.57079632,           0, 2.35619449],
     *                  [0,          0,           0,          0]
     *     Output:
     *        [1.0, 0.0,  0.0, 0.0],
     *        [0.0, 0.0, -1.0, 0.0],
     *        [0.0, 1.0,  0.0, 3.0],
     *        [  0,   0,    0,   1]
     */
    @Test
    public void MatrixExp6Test(){
        var input = new RMatrix(Arrays.asList(
                ZERO, ZERO,ZERO,ZERO,
                ZERO, ZERO,minus(new BigDecimal("1.57079632")),new BigDecimal("2.35619449").round(MC6),
                ZERO, new BigDecimal("1.57079632").round(MC6),ZERO,new BigDecimal("2.35619449").round(MC6),
                ZERO,ZERO,ZERO,ZERO));
        LOGGER.debug("MatrixExp6Test input\n'{}", input.getData());
        var expected = new RMatrix(Arrays.asList(
                ONE, ZERO,ZERO,ZERO,
                ZERO, ZERO,minus(ONE),ZERO,
                ZERO,ONE,ZERO,THREE,
                ZERO,ZERO,ZERO,ONE));

        var actual = MatrixExp6(input);
        LOGGER.debug("MatrixExp6Test actual \n'{}", actual.getData());
        LOGGER.debug("MatrixExp6Test expected \n'{}", expected.getData());
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }
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
     */
    @Test
    public void MatrixLog6Test(){
        var input = new RMatrix(Arrays.asList(
                ONE, ZERO,ZERO,ZERO,
                ZERO, ZERO,minus(ONE),ZERO,
                ZERO,ONE,ZERO,THREE,
                ZERO,ZERO,ZERO,ONE));
        var expected = new RMatrix(Arrays.asList(
                ZERO, ZERO,ZERO,ZERO,
                ZERO, ZERO,minus(new BigDecimal("1.57079632")),new BigDecimal("2.35619449").round(MC6),
                ZERO, new BigDecimal("1.57079632").round(MC6),ZERO,new BigDecimal("2.35619449").round(MC6),
                ZERO,ZERO,ZERO,ZERO));
        LOGGER.debug("MatrixLog6Test input\n'{}", input.getData());
        var actual = MatrixLog6(input);
        LOGGER.debug("MatrixLog6Test actual \n'{}", actual.getData());
        LOGGER.debug("MatrixLog6Test expected \n'{}", expected.getData());
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }
    }
}

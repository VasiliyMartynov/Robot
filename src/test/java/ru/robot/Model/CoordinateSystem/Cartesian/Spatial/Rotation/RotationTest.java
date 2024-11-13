package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.robot.Model.DataStructure.Base.RMatrix;
import ru.robot.Model.DataStructure.RotationMatrix;
import ru.robot.Model.DataStructure.Vector3;
import ru.robot.Model.DataStructure.Vector4;
import ru.robot.Model.DataStructure.SkewSymmetricMatrix;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.Rotation.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.minus;
import static ru.robot.Model.DataStructure.Base.RMatrix.getIdentityMatrix;

public class RotationTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @BeforeEach
    void init(TestInfo testInfo) {
        LOGGER.info(testInfo.getDisplayName());
    }

    private RotationMatrix getGoodRotationMatrix(){
        LOGGER.debug("getGoodRotationMatrix");
        var r = new RMatrix(Arrays.asList(
                new BigDecimal("0.804738"),
                new BigDecimal("-0.310617"),
                new BigDecimal("0.505879"),
                new BigDecimal("0.505879"),
                new BigDecimal("0.804738"),
                new BigDecimal("-0.310617"),
                new BigDecimal("-0.310617"),
                new BigDecimal("0.505879"),
                new BigDecimal("0.804738")));
        return new RotationMatrix(r);
    }

    private RMatrix init(){
        LOGGER.debug("init");
        return getIdentityMatrix(3);
    }

    private RMatrix getBadRotationMatrix(){
        LOGGER.debug("getBadRotationMatrix");
        return new RMatrix(Arrays.asList(
                new BigDecimal("0.804738"),
                new BigDecimal("-0.310617"),
                new BigDecimal("0.505879"),
                new BigDecimal("0.505879"),
                new BigDecimal("0.804738"),
                new BigDecimal("-0.310617"),
                new BigDecimal("-0.310617"),
                new BigDecimal("0.505879"),
                new BigDecimal("0.804738")));
    }


    /**
     * Converts a 3-vector to an so(3) representation
     *     :param omg: A 3-vector
     *     :return: The skew symmetric representation of omg
     *
     *     Example Input:
     *         omg = np.array([1, 2, 3])
     *     Output:
     * ([[ 0, -3,  2],
     * [ 3,  0, -1],
     * [-2,  1,  0]])
     */
    @Test
    void VecToso3Test(){
        var omg = new Vector3(ONE, TWO, THREE);
        LOGGER.debug("omg \n'{}'",omg.getData());
        var actual = VecToso3(omg);
        LOGGER.debug("actual SkewSymmetricMatrix \n`{}`", actual.getData());

        var expected = new SkewSymmetricMatrix(new RMatrix(Arrays.asList(
                ZERO, minus(THREE), TWO,
                THREE, ZERO, minus(ONE),
                minus(TWO), ONE, ZERO)));

        for(int i = 0; i < actual.getData().getSize(); i++){
            for(int j = 0; j < actual.getData().getSize(); j++){
                assertEquals(expected.getData().getDouble(i,j), actual.getData().getDouble(i,j));
            }
        }
    }

    /**
     *     Converts an so(3) representation to a 3-vector
     *
     *     :param so3mat: A 3x3 skew-symmetric matrix
     *     :return: The 3-vector corresponding to so3mat
     *
     *     Example Input:
     *         so3mat = np.array([[ 0, -3,  2],
     *                            [ 3,  0, -1],
     *                            [-2,  1,  0]])
     *     Output:
     *         np.array([1, 2, 3])
     */
    @Test
    void so3ToVecTest(){
        var skewSymmetricMatrix = new SkewSymmetricMatrix(new RMatrix(Arrays.asList(
                ZERO, minus(THREE), TWO,
                THREE, ZERO, minus(ONE),
                minus(TWO), ONE, ZERO)));
        var expected = new Vector3(ONE, TWO, THREE);

        var actual = so3ToVec(skewSymmetricMatrix);
        LOGGER.debug("actual vector3 `{}`", actual.getData());

        for(int j = 0; j < actual.getSize(); j++){
            assertEquals(expected.getData().get(j),actual.getData().get(j));
        }


    }

    /**
     *     Converts a 3-vector of exponential coordinates for rotation into
     *     axis-angle form
     *     :return omghat: A unit rotation axis
     *     :return theta: The corresponding rotation angle
     *     Example Input:
     *         expc3 = np.array([1, 2, 3])
     *     Output:
     *         (np.array([0.26726124, 0.53452248, 0.80178373]), 3.7416573867739413)
     *     return (Normalize(expc3), np.linalg.norm(expc3))
     */
    @Test
    void AxisAng3Test(){
        var v3 = new Vector3(ONE, TWO, THREE);
        var expected = new Vector4(
                new BigDecimal("0.267261"),
                new BigDecimal("0.534522"),
                new BigDecimal("0.801783"),
                new BigDecimal("3.74166"));

        var actual = AxisAng3(v3);

        LOGGER.debug("actual vector4 `{}`", actual.getData());

        for(int j = 0; j < actual.getSize(); j++){
            assertEquals(expected.getData().get(j),actual.getData().get(j));
        }
    }

    /**
     *     Converts a 4-vector of axis-angle form to
     *     exponential coordinates for rotation
     *     Example Input:
     *      (np.array([0.26726124, 0.53452248, 0.80178373]), 3.7416573867739413)
     *     Output:  expc3 = np.array([1, 2, 3])

     *     return (Normalize(expc3), np.linalg.norm(expc3))
     */
    @Test
    void AngleAxisToVec3Test(){
        var v4 = new Vector4(
                new BigDecimal("0.267261"),
                new BigDecimal("0.534522"),
                new BigDecimal("0.801783"),
                new BigDecimal("3.74166"));
        var actual = AngleAxisToVec3(v4);
        var expected = new Vector3(ONE, TWO, THREE);

        LOGGER.debug("actual vector3 `{}`", actual.getData());

        for(int j = 0; j < actual.getSize(); j++){
            assertEquals(expected.getData().get(j),actual.getData().get(j));
        }
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
     np.array(  [[-0.69492056,  0.71352099,  0.08929286],
                 [-0.19200697, -0.30378504,  0.93319235],
                 [ 0.69297817,  0.6313497 ,  0.34810748]])

     */
    @Test
    void MatrixExp3Test(){
        var skewSymmetricMatrix = new SkewSymmetricMatrix(new RMatrix(Arrays.asList(
                ZERO, minus(THREE), TWO,
                THREE, ZERO, minus(ONE),
                minus(TWO), ONE, ZERO)));
        var expected = new RMatrix(Arrays.asList(
                new BigDecimal("-0.694920"),
                new BigDecimal("0.713520"),
                new BigDecimal("0.089292"),
                new BigDecimal("-0.192006"),
                new BigDecimal("-0.303785"),
                new BigDecimal("0.933192"),
                new BigDecimal("0.692978"),
                new BigDecimal("0.631349"),
                new BigDecimal("0.348107")
        ));

        var actual = Rotation.MatrixExp3(skewSymmetricMatrix);

        LOGGER.debug("actual matrix `{}`\n", actual.getData());
        for(int i = 0; i < actual.getSize(); i++){
            for(int j = 0; j < actual.getSize(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }
    }

    /**
     *Computes the matrix logarithm of a rotation matrix
     *     :param R: A 3x3 rotation matrix
     *     :return: The matrix logarithm of R
     *     Example Input:
     *         R = np.array([[0, 0, 1],
     *                       [1, 0, 0],
     *                       [0, 1, 0]])
     *     Output:
     *         np.array([[          0, -1.20919958,  1.20919958],
     *                   [ 1.20919958,           0, -1.20919958],
     *                   [-1.20919958,  1.20919958,           0]])
     */
    @Test
    void MatrixLog3Test(){
        var rotationMatrix = new RotationMatrix(
                new RMatrix(Arrays.asList(
                        ZERO,ZERO,ONE,
                        ONE,ZERO,ZERO,
                        ZERO,ONE,ZERO
                )));
        LOGGER.info("rotationMatrix\n'{}",rotationMatrix.getData());
        var expected = new RMatrix(Arrays.asList(
                ZERO,
                new BigDecimal("-1.20921"),
                new BigDecimal("1.20921"),
                new BigDecimal("1.20921"),
                ZERO,
                new BigDecimal("-1.20921"),
                new BigDecimal("-1.20921"),
                new BigDecimal("1.20921"),
                ZERO
        ));
        LOGGER.info("expected matrix \n`{}`", expected.getData());
        var actual = MatrixLog3(rotationMatrix);
        LOGGER.info("actual matrix \n`{}`", actual.getData());
        for(int i = 0; i < actual.getData().getSize(); i++){
            for(int j = 0; j < actual.getData().getSize(); j++){
                assertEquals(expected.getDouble(i,j), actual.getData().getDouble(i,j));
            }
        }



    }

    @Test
    void rotateTest(){
        var m = new RotationMatrix(init());
        LOGGER.debug("M `{}`", m.getData());
        LOGGER.debug("M size`{}`", m.getData().getSize());
        var n = getGoodRotationMatrix();
        LOGGER.debug("N `{}`", n.getData());
        var r = rotate(m, n);
        LOGGER.debug("R `{}`", r.getData());

        for(int i = 0; i < m.getData().getSize(); i++){
            for(int j = 0; j < m.getData().getSize(); j++){
                assertEquals(r.getData().getDouble(i,j), n.getData().getDouble(i,j));
            }
        }
    }
}

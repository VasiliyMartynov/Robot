package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.robot.Model.DataStructure.Base.RMatrix;
import ru.robot.Model.DataStructure.Vector3;
import ru.robot.Model.DataStructure.Vector4;
import ru.robot.Model.DataStructure.SkewSymmetricMatrix;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.AngleAxis.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.minus;

public class AngleAxisTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @BeforeEach
    void init(TestInfo testInfo) {
        LOGGER.info(testInfo.getDisplayName());
    }



//    @Test
//    void MatrixLog3Test() throws InstantiationException {
//        var SO3 = new RotationMatrix(new RMatrix(Arrays.asList(ZERO,ZERO,ONE, ONE,ZERO,ZERO,ZERO,ONE,ZERO)));
//        var n = new BigDecimal("1.20919958");
//        var minusN = minus(n);
//        var so3 = new RMatrix(Arrays.asList(ZERO, minusN,n, n,ZERO,minusN,minusN,n,ZERO));
//        System.out.println(SO3.toString());
//        System.out.println(so3.toString());
//        System.out.println(MatrixLog3(SO3).toString());
//
//    }


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
        var actual = VecToso3(omg);
        LOGGER.debug("actual SkewSymmetricMatrix `{}`", actual.getData());

        var expected = new SkewSymmetricMatrix(new RMatrix(Arrays.asList(
                ZERO, minus(THREE), TWO,
                THREE, ZERO, minusONE,
                minus(TWO), ONE, ZERO)));

        for(int i = 0; i < actual.getData().getSize(); i++){
            for(int j = 0; j < actual.getData().getSize(); j++){
                assertEquals(actual.getData().getDouble(i,j), expected.getData().getDouble(i,j));
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
                THREE, ZERO, minusONE,
                minus(TWO), ONE, ZERO)));
        var expected = new Vector3(ONE, TWO, THREE);

        var actual = so3ToVec(skewSymmetricMatrix);
        LOGGER.debug("actual vector3 `{}`", actual.getData());

        for(int j = 0; j < actual.getSize(); j++){
            assertEquals(actual.getData().get(j), expected.getData().get(j));
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
            assertEquals(actual.getData().get(j), expected.getData().get(j));
        }
    }

    /**
     *     Converts a 4-vector of axis-angle form to
     *     exponential coordinates for rotation
     *     Example Input:
     *      (np.array([0.26726124, 0.53452248, 0.80178373]), 3.7416573867739413)
     *     Output:  expc3 = np.array([1, 2, 3])
     *
     *     return (Normalize(expc3), np.linalg.norm(expc3))
     */
    @Test
    void AngleAxisToVec3Test(){
        var v4 = new Vector4(
                new BigDecimal("0.267261"),
                new BigDecimal("0.534522"),
                new BigDecimal("0.801783"),
                new BigDecimal("3.74166"));

        var expected = new Vector3(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"));
        var actual = AngleAxisToVec3(v4);

        LOGGER.debug("actual vector3 `{}`", actual.getData());

        for(int j = 0; j < actual.getSize(); j++){
            LOGGER.debug(actual.getData().get(j));
            LOGGER.debug(expected.getData().get(j));
            assertEquals(actual.getData().get(j), expected.getData().get(j));
        }
    }
}

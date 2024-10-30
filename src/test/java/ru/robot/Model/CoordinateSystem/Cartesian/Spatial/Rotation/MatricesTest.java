package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.robot.Model.DataStructure.Base.RMatrix;
import ru.robot.Model.DataStructure.RotationMatrix;
import ru.robot.Model.DataStructure.SkewSymmetricMatrix;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.Matrices.*;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.minus;
import static ru.robot.Model.DataStructure.Base.RMatrix.getIdentityMatrix;

public class MatricesTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @BeforeEach
    void init(TestInfo testInfo) {
        LOGGER.debug(testInfo.getDisplayName());
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

    /**
     """Computes the matrix exponential of a matrix in so(3)

     :param so3mat: A 3x3 skew-symmetric matrix
     :return: The matrix exponential of so3mat

     Example Input:
     [ 0, -3,  2],
     [ 3,  0, -1],
     [-2,  1,  0]
     Output:
     [-0.69492056,  0.71352099,  0.08929286],
     [-0.19200697, -0.30378504,  0.93319235],
     [ 0.69297817,  0.6313497 ,  0.34810748]
     """
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

        var actual = Matrices.MatrixExp3(skewSymmetricMatrix);

        LOGGER.debug("actual matrix `{}`\n", actual.getData());
        for(int i = 0; i < actual.getSize(); i++){
            for(int j = 0; j < actual.getSize(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }
    }
}

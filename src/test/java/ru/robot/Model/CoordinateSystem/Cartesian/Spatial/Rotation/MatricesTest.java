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
        System.out.println(testInfo.getDisplayName());
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
        LOGGER.debug("set so3");
        var so3 = new SkewSymmetricMatrix(new RMatrix(Arrays.asList(
                ZERO,
                minus(THREE),
                TWO,
                THREE,
                ZERO,
                minusONE,
                minus(TWO),
                ONE,
                ZERO)));
        LOGGER.debug("so3 `{}`", so3.getData());
        var actual = MatrixExp3(so3);
        LOGGER.debug("actual `{}`", actual.getData());
        var expected = new RMatrix(Arrays.asList(
                new BigDecimal("-0.69492056"),
                new BigDecimal("0.71352099"),
                new BigDecimal("0.08929286"),
                new BigDecimal("-0.19200697"),
                new BigDecimal("-0.30378504"),
                new BigDecimal("0.93319235"),
                new BigDecimal("0.69297817"),
                new BigDecimal("0.6313497"),
                new BigDecimal("0.34810748")
        )
        );

        for(int i = 0; i < actual.getData().getSize(); i++){
            for(int j = 0; j < actual.getData().getSize(); j++){
                assertEquals(actual.getData().getDouble(i,j), expected.getDouble(i,j));
            }
        }

    }
}

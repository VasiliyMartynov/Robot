package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.robot.Model.CoordinateSystem.Cartesian.Utils.AXIS;
import ru.robot.Model.DataStructure.Base.RMatrix;
import ru.robot.Model.DataStructure.RotationMatrix;
import ru.robot.Model.DataStructure.SkewSymmetricMatrix;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static ch.obermuhlner.math.big.BigDecimalMath.cos;
import static ch.obermuhlner.math.big.BigDecimalMath.sin;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.AngleAxis.AxisAng3;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.AngleAxis.so3ToVec;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.DataStructure.Base.RMatrix.*;
import static ru.robot.Model.DataStructure.Base.RVector.normOfVector;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.nearZero;

public class Matrices {
    private static final Logger LOGGER = LogManager.getLogger();

    public static RotationMatrix rotate(RotationMatrix m, RotationMatrix n) {
        var z = mult(m.getData(), n.getData());
        return new RotationMatrix(roundValuesOfRMatrix(z));
    }

    public static RotationMatrix getRotationAroundFixedAxis(RotationMatrix currentRotation, BigDecimal angleInRad, AXIS axis) {
        var c = cos(angleInRad, MC6);
        var s = sin(angleInRad, MC6);
        var minusS = s.multiply(new BigDecimal("-1"), MC6);
        List<BigDecimal> itemList = List.of();
        switch (axis) {
            case X -> {
                itemList = Arrays.asList(ONE, ZERO, ZERO, ZERO, c, minusS, ZERO, s, c);
            }
            case Y -> {
                itemList = Arrays.asList(c, ZERO, s, ZERO, ONE, ZERO, minusS, ZERO, c);
            }
            case Z -> {
                itemList = Arrays.asList(c, minusS, ZERO, s, c, ZERO, ZERO, ZERO, ONE);
            }
        }
        return new RotationMatrix(mult(currentRotation.getData(), new RMatrix(itemList)));
    }


    /**
     """Computes the matrix exponential of a matrix in so(3)

     :param so3mat: A 3x3 skew-symmetric matrix
     :return: The matrix exponential of so3mat

     Example Input:
     so3mat = np.array([[ 0, -3,  2],
     [ 3,  0, -1],
     [-2,  1,  0]])
     Output:
     np.array([[-0.69492056,  0.71352099,  0.08929286],
     [-0.19200697, -0.30378504,  0.93319235],
     [ 0.69297817,  0.6313497 ,  0.34810748]])
     """
     */
    public static RotationMatrix MatrixExp3(SkewSymmetricMatrix so3mat) {
        LOGGER.debug("MatrixExp3 has started");
        var omgtheta = so3ToVec(so3mat);
        LOGGER.debug("omgtheta: `{}`", omgtheta.getData());
        var w1 = omgtheta.getData().get(0);
        var w2 = omgtheta.getData().get(1);
        var w3 = omgtheta.getData().get(2);
        var theta = AxisAng3(omgtheta).getData().get(3);

//        if (w1.compareTo(ONE) > 0 || w2.compareTo(ONE) > 0 || w3.compareTo(ONE) > 0 ) {
//            IllegalArgumentException e = new IllegalArgumentException("Illegal input, please check input parameters");
//            LOGGER.debug("MatrixExp3 has finished with exception", e);
//            throw  e;
//        }

        if(nearZero(normOfVector(omgtheta)) > 0){
            LOGGER.debug("nearZero(normOfVector(omgtheta)) > 0 return Identity");
            return new RotationMatrix(getIdentityMatrix(3));
        }

        var c = cos(theta,MC6);
        var oneMinusCos = ONE.subtract(c);
        var s = sin(theta, MC6);
        var w1pow2 = w1.pow(2);
        var w2pow2 = w2.pow(2);
        var w3pow2 = w3.pow(2);
        var w1w2 = w1.multiply(w2);
        var w1w3 = w1.multiply(w3);
        var w2w3 = w2.multiply(w3);
        var w1s = w1.multiply(s);
        var w2s = w2.multiply(s);
        var w3s = w3.multiply(s);

        var m00_p = w1pow2.multiply(oneMinusCos);
        var m00 = c.add(m00_p);

        var m01_p = w1w2.multiply(oneMinusCos);
        var m01 = m01_p.subtract(w3s);

        var m02_p = w1w3.multiply(oneMinusCos);
        var m02 = m02_p.add(w2s);

        var m1_p = w1w2.multiply(oneMinusCos);
        var m10 = m1_p.add(w3s);

        var m11_p = w2pow2.multiply(oneMinusCos);
        var m11 = c.add(m11_p);

        var m12_p = w2w3.multiply(oneMinusCos);
        var m12 = m12_p.subtract(w1s);

        var m20_p = w1w3.multiply(oneMinusCos);
        var m20 = m20_p.subtract(w2s);

        var m21_p = w2w3.multiply(oneMinusCos);
        var m21 = m21_p.add(w1s);

        var m22_p = w3pow2.multiply(oneMinusCos);
        var m22 = c.add(m22_p);

        var itemList = Arrays.asList(m00, m01, m02, m10, m11, m12, m20, m21, m22);
        LOGGER.debug("MatrixExp3 has finished");
        return new RotationMatrix(new RMatrix(itemList));
    }
}

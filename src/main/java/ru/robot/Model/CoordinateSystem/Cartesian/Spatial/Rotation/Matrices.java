package ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation;

import ru.robot.Model.CoordinateSystem.Cartesian.AXIS;
import ru.robot.Model.DataStructure.RMatrix;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static ch.obermuhlner.math.big.BigDecimalMath.cos;
import static ch.obermuhlner.math.big.BigDecimalMath.sin;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.AngleAxis.AxisAng3;
import static ru.robot.Model.CoordinateSystem.Cartesian.Spatial.Rotation.AngleAxis.so3ToVec;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.DataStructure.RMatrix.*;
import static ru.robot.Model.DataStructure.RVector.normOfVector;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.nearZero;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.YESNO.YES;

public class Matrices {

    public static RMatrix rotate(RMatrix currentRotation, RMatrix rotationMatrixData) {
        var m = RMatrix.mult(currentRotation, rotationMatrixData);
        return roundValuesOfRMatrix(m);
    }

    public static RMatrix getRotationAroundFixedAxis(RMatrix currentRotation, BigDecimal angleInRad, AXIS axis) throws InstantiationException {
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
        return mult(currentRotation, new RMatrix(itemList));
    }

    /**
     * soMat to SO(3) using matrix multimpliactionl
     * @param so3mat
     * @return
     * @throws InstantiationException
     */

    public static RMatrix MatrixExp3(RMatrix so3mat) throws InstantiationException {
        var omgtheta = so3ToVec(so3mat);
        var w1 = omgtheta.get(0);
        var w2 = omgtheta.get(1);
        var w3 = omgtheta.get(2);
        var theta = AxisAng3(omgtheta).get(3);

        if (w1.compareTo(ONE) > 0 || w2.compareTo(ONE) > 0 || w3.compareTo(ONE) > 0 ) {
            throw  new IllegalArgumentException("Illegal input, please check input parameters");
        }

        if(nearZero(normOfVector(omgtheta)) > 0){
            return getIdentityMatrix(3);
        } else {

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
        return new RMatrix(itemList);
    }
}

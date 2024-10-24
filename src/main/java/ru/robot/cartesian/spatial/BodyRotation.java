package ru.robot.cartesian.spatial;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static ch.obermuhlner.math.big.BigDecimalMath.*;
import static ru.robot.cartesian.spatial.RMatrix.*;
import static ru.robot.cartesian.spatial.RVector.*;
import static ru.robot.cartesian.spatial.YESNO.*;
import static ru.robot.cartesian.utils.GVARS.*;
import static ru.robot.cartesian.utils.Utils.*;

public class BodyRotation {

    RMatrix rotationMatrixData;

    //Constructors section start

    public BodyRotation(RMatrix m) throws InstantiationException {
        var det = getDeterminantOfx3(m);
        System.out.println("Determinant of input Matrix is " + det );
        var diff = ONE.subtract(det, MC6);
        System.out.println("Diff is "+ diff.doubleValue());
        if (nearZero(diff) < 0) {
            System.out.println("Body rotation instance were created");
            this.rotationMatrixData = m;
        } else {
            throw new InstantiationException(
                "Body rotation cannot be instantiated, " +
                "because Matrix isn't R ∈ SO(3), " +
                "det R not equals 1, please check input values"
                );}
    }
    //Constructors section end

    public RMatrix getLocalRotationMatrix() {
        return this.rotationMatrixData;
    }

    public BigDecimal getItem(int row, int column) {
        return this.rotationMatrixData.get(row,column);
    }

    public double getItemAsDouble(int row, int column) {
        return this.rotationMatrixData.get(row,column).doubleValue();
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Rotation matrix:\n");
        for(int i = 0; i < this.rotationMatrixData.getRowCount(); i++) {
            for(int j = 0; j < this.rotationMatrixData.getColumnCount(); j++) {
                s.append(this.rotationMatrixData.get(i, j).doubleValue());
                s.append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

//    MATH
    public void rotate(BodyRotation r) {
        var m = RMatrix.mult(r.getLocalRotationMatrix(), this.rotationMatrixData);
        this.rotationMatrixData = roundValuesOfMatrix(m);
    }

    public BodyRotation getMatrixOfRotation(BodyRotation r) throws InstantiationException {
        var m = mult(this.rotationMatrixData, r.getLocalRotationMatrix());
        var result = roundValuesOfMatrix(m);
        return new BodyRotation(result);
    }

    public BodyRotation getRotationAroundFixedAxis(BigDecimal angleInRad, AXIS axis) throws InstantiationException {
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
        return new BodyRotation(mult(this.rotationMatrixData, setValues3x3(itemList, YES)));
    }

//    exp : [ωˆ]θ ∈ so(3) → R ∈ SO(3),
//    Rot(ωˆ, θ) = e[ωˆ]θ = I + sin θ [ωˆ] + (1 − cos θ)[ωˆ]2 ∈ SO(3).
//    exponential coordinates of a rotation matrix R

    /**
     * get SO3 from Angle/Axis
     * return new BodyRotation
     */
    public static BodyRotation getRotationFromAngleAxis(BigDecimal angleRadians, BigDecimal w1, BigDecimal w2, BigDecimal w3) throws InstantiationException {
        if (w1.compareTo(ONE) > 0 || w2.compareTo(ONE) > 0 || w3.compareTo(ONE) > 0 ) {
            throw  new IllegalArgumentException("Illegal input, please check input parameters");
        }
        var c = cos(angleRadians,MC6);
        var oneMinusCos = ONE.subtract(c);
        var s = sin(angleRadians, MC6);
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
        return new BodyRotation(setValues3x3(itemList, YES));
    }

        //    log : R ∈ SO(3) → [ωˆ]θ ∈ so(3).

    /**
     * get Angel/Axis from SO(3)
     * input: none
     * return Matrix 4,0 with w1,w2,w3, angle
     *
     */
    public RVector getAngleAxisFromLocalSO3(){
        RVector axisAngle;
        var i = RMatrix.getIdentityMatrix();
        var r = rotationMatrixData;
        var trace = RMatrix.trace(r);
        System.out.println("Trace is " + trace);
        axisAngle = new RVector(4);

        if (RMatrix.equalsContent(r,i))
        {
            System.out.println("Case A matrix is Identity");
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());
            return axisAngle;
        }

        else if (trace.compareTo(minusONE) == 0)
        {
            System.out.println("Case B Trace is -1");
            var r13 = r.get(0,2);
            var r23 = r.get(1,2);
            var r33 = r.get(2,2);
            var onePlusR33 = r33.add(ONE);
            var TwoMultOnePlusR33 = onePlusR33.multiply(new BigDecimal("2"));
            var sqrtFromTwoMultOnePlusR33 = TwoMultOnePlusR33.sqrt(MC6);
            var p1 = ONE.divide(sqrtFromTwoMultOnePlusR33, MC6);
            var w1 = p1.multiply(r13, MC6);
            var w2 = p1.multiply(r23, MC6);
            var w3 = p1.multiply(onePlusR33, MC6);
            var angle = BigDecimal.valueOf(Math.PI).round(MC6);
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());
            return new RVector(w1,w2,w3,angle);
        }
        else
        {
            System.out.println("Case C");
            var traceMinusOne  = trace.subtract(ONE);
            var traceMinusOneDivTWO = traceMinusOne.divide(TWO, MC6);
            var angle = acos(traceMinusOneDivTWO, MC6);

            var sinA = sin(angle, MC6);
            var TwoSinA = sinA.multiply(TWO);
            var OneDivTwoSinA = ONE.divide(TwoSinA, MC6);

            var r21 = r.get(2,1);
            var r12 = r.get(1,2);
            var r02 = r.get(0,2);
            var r20 = r.get(2,0);
            var r10 = r.get(1,0);
            var r01 = r.get(0,1);

            var r21MinusR12 = r21.subtract(r12);
            var r02MinusR20 = r02.subtract(r20);
            var r10MinusR01 = r10.subtract(r01);

            var w1 = OneDivTwoSinA.multiply(r21MinusR12).round(MC6);
            var w2 = OneDivTwoSinA.multiply(r02MinusR20).round(MC6);
            var w3 = OneDivTwoSinA.multiply(r10MinusR01).round(MC6);

            return new RVector(w1,w2,w3,angle);
        }
    }

    /**
     * Converts a 3-vector to an so(3) representation
     *     :param omg: A 3-vector
     *     :return: The skew symmetric representation of omg
     *
     *     Example Input:
     *         omg = np.array([1, 2, 3])
     *     Output:
     *         np.array([[ 0, -3,  2],
     *                   [ 3,  0, -1],
     *                   [-2,  1,  0]])
     */
    public static RMatrix VecToso3(RVector omg){
        var v1 = omg.get(0);
        var v2 = omg.get(1);
        var v3 = omg.get(2);
        List<BigDecimal> items = Arrays.asList(ZERO, minusOne(v3), v2,v3,ZERO,minusOne(v1),minusOne(v2),v1, ZERO);
        return setValues3x3(items, NO);
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
    public static RVector so3ToVec(RMatrix so3mat){
        var m1 = so3mat.get(2,1);
        var m2 = so3mat.get(0,2);
        var m3 = so3mat.get(1,0);
        return  new RVector(m1, m2, m3);
    }

    /**
     *     Converts a 3-vector of exponential coordinates for rotation into
     *     axis-angle form
     *
     *     :param
     *     :return omghat: A unit rotation axis
     *     :return theta: The corresponding rotation angle
     *
     *     Example Input:
     *         expc3 = np.array([1, 2, 3])
     *     Output:
     *         (np.array([0.26726124, 0.53452248, 0.80178373]), 3.7416573867739413)
     *     """
     *     return (Normalize(expc3), np.linalg.norm(expc3))
     * @param expc3  A 3-vector of exponential coordinates for rotation
     * @return result
     */
    public static RVector AxisAng3(RVector expc3){
        RVector normalised = normaliseVector(expc3);
        return new RVector(normalised.get(0), normalised.get(1),normalised.get(2) , normOfVector(expc3));
    }
}


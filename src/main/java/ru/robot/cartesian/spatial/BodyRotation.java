package ru.robot.cartesian.spatial;

import org.ujmp.core.Matrix;
import org.ujmp.core.bigdecimalmatrix.impl.DefaultDenseBigDecimalMatrix2D;
import ru.robot.cartesian.utils.GVARS;
import ru.robot.cartesian.utils.MatrixUtils;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static ch.obermuhlner.math.big.BigDecimalMath.cos;
import static ch.obermuhlner.math.big.BigDecimalMath.sin;

public class BodyRotation {

    Matrix rotationMatrixData;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal ZERO = BigDecimal.ZERO;

    
    //Constructors section start

    public BodyRotation(Matrix m) throws InstantiationException {
        var det = MatrixUtils.getDeterminantOfx3(m);
        System.out.println("Determinant of input Matrix is " + det );
        var diff = ONE.subtract(det, GVARS.MC6);
        System.out.println("Diff is "+ diff.doubleValue());
        if (diff.compareTo(new BigDecimal("0.000005")) < 0) {
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

    public Matrix getLocalRotationMatrix() {
        return this.rotationMatrixData;
    }

    public BigDecimal getItem(int row, int column) {
        return this.rotationMatrixData.getAsBigDecimal(row,column);
    }

    public double getItemAsDouble(int row, int column) {
        return this.rotationMatrixData.getAsBigDecimal(row,column).doubleValue();
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Rotation matrix:\n");
        for(int i = 0; i < this.rotationMatrixData.getRowCount(); i++) {
            for(int j = 0; j < this.rotationMatrixData.getColumnCount(); j++) {
                s.append(this.rotationMatrixData.getAsBigDecimal(i, j).doubleValue());
                s.append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

//    MATH
    public void rotate(BodyRotation r) {
        var m = this.rotationMatrixData.mtimes(r.rotationMatrixData);
        this.rotationMatrixData = MatrixUtils.roundValuesOfMatrix(m);
    }

    public BodyRotation getMatrixOfRotation(BodyRotation r) throws InstantiationException {
        var m = this.rotationMatrixData.mtimes(r.rotationMatrixData);
        var result = MatrixUtils.roundValuesOfMatrix(m);
        return new BodyRotation(result);
    }

    public BodyRotation getRotationAroundFixedAxis(BigDecimal angleInRad, AXIS axis) throws InstantiationException {
        var c = cos(angleInRad, GVARS.MC6);
        var s = sin(angleInRad, GVARS.MC6);
        var minusS = s.multiply(new BigDecimal("-1"), GVARS.MC6);
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
        return new BodyRotation(this.rotationMatrixData.mtimes(MatrixUtils.setValues(itemList, 1)));
    }

//    exp : [ωˆ]θ ∈ so(3) → R ∈ SO(3),
//    Rot(ωˆ, θ) = e[ωˆ]θ = I + sin θ [ωˆ] + (1 − cos θ)[ωˆ]2 ∈ SO(3).
//    exponential coordinates of a rotation matrix R

    public static BodyRotation getRotationOfAngleAxis(BigDecimal angleRadians, BigDecimal w1, BigDecimal w2, BigDecimal w3) throws InstantiationException {
        if (w1.compareTo(ONE) > 0 || w2.compareTo(ONE) > 0 || w3.compareTo(ONE) > 0 ) {
            throw  new IllegalArgumentException("Illegal input, please check input parameters");
        }

        var c = cos(angleRadians, GVARS.MC6);
        var oneMinusCos = ONE.subtract(c);
        var s = sin(angleRadians, GVARS.MC6);
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
        return new BodyRotation(MatrixUtils.setValues(itemList, 1));
    }

        //    log : R ∈ SO(3) → [ωˆ]θ ∈ so(3).

    private Matrix calcAxisAngle(){
        Matrix axisAngle;
        var i = MatrixUtils.getIdentityMatrix();
        var r = rotationMatrixData;

        var trace = BigDecimal.valueOf(r.trace());
        var traceRounded = trace.round(GVARS.MC6);
        System.out.println("Trace is " + trace);
        System.out.println("Trace rounded is " + traceRounded);
        if (r.equals(i))
        {
            System.out.println("Case A matrix is Identity");
            axisAngle = new DefaultDenseBigDecimalMatrix2D(3,1);
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());
            return axisAngle;
        }
        else if (traceRounded.compareTo(new BigDecimal("-1")) < 0)
        {
            System.out.println("Case B Trace is -1");
            var r13 = r.get(0,2);
            var r23 = r.get(1,2);
            var r33 = r.get(2,2);
            var o1 = (1/sqrt(2 * (1 + r33))) * r13;
            var o2 = (1/sqrt(2 * (1 + r33))) * r23;
            var o3 = (1/sqrt(2 * (1 + r33))) * r23;
            var o4 = Math.PI;
            var r1 = roundValue(o1);
            var r2 = roundValue(o2);
            var r3 = roundValue(o3);
            var r4 = roundValue(o4);
            axisAngle = new DenseDoubleMatrix1D(new double[]{r1,r2,r3,r4});
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());
            return axisAngle;
        }
        else
        {
            System.out.println("Case C");
            var a = acos((algebra.trace(r) - 1.0) / 2.0);
            var w1 = (1 / (2.0 * sin(a))) * (r.get(2,1) - r.get(1,2));
            var w2 = (1 / (2.0 * sin(a))) * (r.get(0,2) - r.get(2,0));
            var w3 = (1 / (2.0 * sin(a))) * (r.get(1,0) - r.get(0,1));
            double r1 = roundValue(w1);
            double r2 = roundValue(w2);
            double r3 = roundValue(w3);
            double angle = roundValue(a);
            axisAngle = new DenseDoubleMatrix1D(new double[]{r1,r2,r3,angle});
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());
            return axisAngle;
        }
    }

//    public BodyRotation getSkewSymmetricFromVector(Double x1, Double x2, Double x3) throws InstantiationException {
//        Matrix rotationMatrix = new DenseMatrix(3,3);
//        rotationMatrix.set(0,0,0);
//        rotationMatrix.set(0,1,-x3);
//        rotationMatrix.set(0,2,x2);
//
//        rotationMatrix.set(1,0,x3);
//        rotationMatrix.set(1,1,0);
//        rotationMatrix.set(1,2,-x1);
//
//        rotationMatrix.set(2,0,-x2);
//        rotationMatrix.set(2,1,x1);
//        rotationMatrix.set(3,2,0);
//
//        return new BodyRotation(rotationMatrix);
//    }
//
//    public DoubleMatrix1D getAxisAngle(){
//        return  this.calcAxisAngle();
//
//    }


}


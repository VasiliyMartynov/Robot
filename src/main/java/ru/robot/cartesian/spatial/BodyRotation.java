package ru.robot.cartesian.spatial;

import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import cern.colt.matrix.tdouble.algo.DoubleFormatter;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;

public class BodyRotation {

    DenseDoubleAlgebra algebra = new DenseDoubleAlgebra();
    DoubleMatrix2D rotationMatrixData;
    double angleTheta;
    DoubleMatrix1D vectorAxisOmega;

    //Constructors section start
    //**************************
    public <T> BodyRotation(ArrayList<T> a) {
        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);
        int k = 0;
        for(int i = 0; i < m.rows(); i++) {
            for(int j = 0; j < m.columns(); j++){
                m.set(i,j, (Double) a.get(k));
                k++;
            }
        }
        this.rotationMatrixData = m;
    }

    public BodyRotation(Double i1,Double i2,Double i3,Double i4,Double i5,Double i6, Double i7,Double i8,Double i9){
        this(new ArrayList<>(Arrays.asList(i1,i2,i3,i4,i5,i6,i7,i8,i9)));
    }

    public BodyRotation(DoubleMatrix2D m) {
        this.rotationMatrixData = m;
    }
    //Constructors section end
    //**************************

    @Override
    public String toString() {
        //String title = "Rotation table";
        //String columnAxisName = "Column";
        //String rowAxisName = "Rows";
        String[] columnNames = {"i1", "i2", "i3"};
        String[] rowNames = { "j1", "j2", "j3"};
        String format = "%1.4G";
        return new DoubleFormatter(format).toTitleString(this.rotationMatrixData, rowNames,columnNames,null,null,null, null);
    }

    //MATH
    public void rotate(BodyRotation r) {
        this.rotationMatrixData = algebra.mult(this.rotationMatrixData, r.rotationMatrixData);
    }

    public DoubleMatrix2D getLocalRotationMatrix() {
        return this.rotationMatrixData;
    }

    public DoubleMatrix2D getMatrixOfRotation(BodyRotation r) {
        DoubleMatrix2D m = algebra.mult(this.rotationMatrixData, r.rotationMatrixData);
        return new BodyRotation(m).getLocalRotationMatrix();
    }

    public BodyRotation getRotationAroundX(double angle) {
        DoubleMatrix2D rotationMatrix = new DenseDoubleMatrix2D(3,3);
        var c = cos(angle);
        var s = sin(angle);
        rotationMatrix.set(0,0,1);
        rotationMatrix.set(0,1,0);
        rotationMatrix.set(0,2,0);

        rotationMatrix.set(1,0,0);
        rotationMatrix.set(1,1,c);
        rotationMatrix.set(1,2,-1 * s);

        rotationMatrix.set(2,0,0);
        rotationMatrix.set(2,1,s);
        rotationMatrix.set(3,2,c);
        return new BodyRotation(algebra.mult(this.rotationMatrixData, rotationMatrix));
    }

    public BodyRotation getRotationAroundY(double angle) {
        DoubleMatrix2D rotationMatrix = new DenseDoubleMatrix2D(3,3);
        var c = cos(angle);
        var s = sin(angle);
        rotationMatrix.set(0,0,c);
        rotationMatrix.set(0,1,0);
        rotationMatrix.set(0,2,s);

        rotationMatrix.set(1,0,0);
        rotationMatrix.set(1,1,1);
        rotationMatrix.set(1,2,0);

        rotationMatrix.set(2,0,-1 * s);
        rotationMatrix.set(2,1,0);
        rotationMatrix.set(3,2,c);

        return new BodyRotation(algebra.mult(this.rotationMatrixData, rotationMatrix));
    }

    public BodyRotation getRotationAroundZ(double angle) {
        DoubleMatrix2D rotationMatrix = new DenseDoubleMatrix2D(3,3);
        var c = cos(angle);
        var s = sin(angle);
        rotationMatrix.set(0,0,c);
        rotationMatrix.set(0,1,-1 * s);
        rotationMatrix.set(0,2,0);

        rotationMatrix.set(1,0,s);
        rotationMatrix.set(1,1,c);
        rotationMatrix.set(1,2,0);

        rotationMatrix.set(2,0,0);
        rotationMatrix.set(2,1,0);
        rotationMatrix.set(3,2,1);

        return new BodyRotation(algebra.mult(this.rotationMatrixData, rotationMatrix));
    }

//    exp : [ωˆ]θ ∈ so(3) → R ∈ SO(3),
//    Rot(ωˆ, θ) = e[ωˆ]θ = I + sin θ [ωˆ] + (1 − cos θ)[ωˆ]2 ∈ SO(3).
//    exponential coordinates of a rotation matrix R

    public BodyRotation getRotationAroundVector(double angleRadiadns, double w1, double w2, double w3){
        var c = cos(angleRadiadns);
        var s = sin(angleRadiadns);
        var m00 = c + Math.pow(w1, 2) * (1 - c);
        var m01 = (w1 * w2) * (1 - c) - (w3 * s);
        var m02 = (w1 * w3) * (1 - c) + (w2 * s);

        var m10 = (w1 * w2) * (1 - c) + (w3 * s);
        var m11 = c + Math.pow(w2, 2) * (1 - c);
        var m12 = (w2 * w3) * (1 - c) - (w1 * s);

        var m20 = (w1 * w3) * (1 - c) - (w2 * s);
        var m21 = (w2 * w2) * (1 - c) + (w3 * s);
        var m22 = c + Math.pow(w3, 2) * (1 - c);

        DoubleMatrix2D rotationMatrix = new DenseDoubleMatrix2D(3,3);

        rotationMatrix.set(0,0,m00);
        rotationMatrix.set(0,1,m01);
        rotationMatrix.set(0,2,m02);

        rotationMatrix.set(1,0,m10);
        rotationMatrix.set(1,1,m11);
        rotationMatrix.set(1,2,m12);

        rotationMatrix.set(2,0,m20);
        rotationMatrix.set(2,1,m21);
        rotationMatrix.set(2,2,m22);

        return new BodyRotation(rotationMatrix);
    }

//    log : R ∈ SO(3) → [ωˆ]θ ∈ so(3).
    public BodyRotation getRotationAxis(BodyRotation rotation){
        var I = DoubleFactory2D.dense.identity(3);
        var r = rotation.getLocalRotationMatrix();
        if (r.equals(I)) {
            this.angleTheta = 0.0;
            this.vectorAxisOmega = new DenseDoubleMatrix1D(new double[]{0.0,0.0,0.0});
        } else if (algebra.trace(r) == -1) {
            var r13 = r.get(0,2);
            var r23 = r.get(1,2);
            var r33 = r.get(2,2);
            var o0 = (1/sqrt(2 * (1 + r33))) * r13;
            var o1 = (1/sqrt(2 * (1 + r33))) * r23;
            var o2 = (1/sqrt(2 * (1 + r33))) * (1 + r23);
            this.vectorAxisOmega = new DenseDoubleMatrix1D(new double[]{o0,o1,o2});
            this.angleTheta = PI;
        } else {
            double v = cos(0.5 * (algebra.trace(r) - 1));
            this.angleTheta = Math.pow(v, -1);
            DoubleMatrix2D rT = algebra.transpose(r);
            var w2 = (1.0 / 2.0 * cos(this.angleTheta)) * (r.get(0,2) - rT.get(0,2));
            var w3 = (1.0 / 2.0 * cos(this.angleTheta)) * (r.get(1,0) - rT.get(1,0));
            var w1 = (1.0 / 2.0 * cos(this.angleTheta)) * (r.get(2,1) - rT.get(2,1));
            this.vectorAxisOmega = new DenseDoubleMatrix1D(new double[]{w1,w2,w3});
        }
        return this;
    }



    public BodyRotation getSkewSymmetricFromVector(Double x1, Double x2, Double x3) {
        DoubleMatrix2D rotationMatrix = new DenseDoubleMatrix2D(3,3);
        rotationMatrix.set(0,0,0);
        rotationMatrix.set(0,1,-x3);
        rotationMatrix.set(0,2,x2);

        rotationMatrix.set(1,0,x3);
        rotationMatrix.set(1,1,0);
        rotationMatrix.set(1,2,-x1);

        rotationMatrix.set(2,0,-x2);
        rotationMatrix.set(2,1,x1);
        rotationMatrix.set(3,2,0);

        return new BodyRotation(rotationMatrix);
    }

    public double getAngleTheta(){
        return this.angleTheta;
    }
    public DoubleMatrix1D getVectorAxisOmega(){
        return this.vectorAxisOmega;
    }



}


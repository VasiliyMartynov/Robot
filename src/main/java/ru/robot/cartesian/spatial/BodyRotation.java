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
import java.util.List;

import static java.lang.StrictMath.*;


public class BodyRotation {

    DenseDoubleAlgebra algebra = new DenseDoubleAlgebra();
    DoubleMatrix2D rotationMatrixData;
    DoubleMatrix1D axisAngle;

    //Constructors section start
    //**************************
    public BodyRotation(Double i1,Double i2,Double i3,Double i4,Double i5,Double i6, Double i7,Double i8,Double i9){
        this(new ArrayList<>(Arrays.asList(i1,i2,i3,i4,i5,i6,i7,i8,i9)));
    }

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
    public DoubleMatrix1D getRotationAxis(){
        var i = DoubleFactory2D.dense.identity(3);
        var r = rotationMatrixData;
        double scale = Math.pow(10, 3);
        var trace = algebra.trace(r);
        var traceRounded = ceil(trace * scale) / scale;
        System.out.println("Trace is " + trace);
        System.out.println("Trace rounded is " + traceRounded);
        if (r.equals(i))
        {
            System.out.println("Case A matrix is Identity");
            this.axisAngle = new DenseDoubleMatrix1D(new double[]{0.0, 0.0, 0.0, 0.0});
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());
            return  axisAngle;
        }
        else if (traceRounded == -1)
        {
            System.out.println("Case B Trace is -1");
            var r13 = r.get(0,2);
            var r23 = r.get(1,2);
            var r33 = r.get(2,2);
            var o1 = (1/sqrt(2 * (1 + r33))) * r13;
            var o2 = (1/sqrt(2 * (1 + r33))) * r23;
            var o3 = (1/sqrt(2 * (1 + r33))) * r23;
            var o4 = Math.PI;
            var r1 = ceil(o1 * scale) / scale;
            var r2 = ceil(o2 * scale) / scale;
            var r3 = ceil(o3 * scale) / scale;
            var r4 = ceil(o4 * scale) / scale;
            this.axisAngle = new DenseDoubleMatrix1D(new double[]{r1,r2,r3,r4});
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());
            return  axisAngle;
        }
        else
        {
            System.out.println("Case C");
            var a = acos((algebra.trace(r) - 1.0) / 2.0);
            var w1 = (1 / (2.0 * sin(a))) * (r.get(2,1) - r.get(1,2));
            var w2 = (1 / (2.0 * sin(a))) * (r.get(0,2) - r.get(2,0));
            var w3 = (1 / (2.0 * sin(a))) * (r.get(1,0) - r.get(0,1));
            double r1 = ceil(w1 * scale) / scale;
            double r2 = ceil(w2 * scale) / scale;
            double r3 = ceil(w3 * scale) / scale;
            double angle = ceil(a * scale) / scale;
            this.axisAngle = new DenseDoubleMatrix1D(new double[]{r1,r2,r3,angle});
            System.out.println("vectorAxisOmega is "+ axisAngle.toString());
            return  axisAngle;
        }
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

    public DoubleMatrix1D getAxisAngle() {
        return this.axisAngle;
    }
}


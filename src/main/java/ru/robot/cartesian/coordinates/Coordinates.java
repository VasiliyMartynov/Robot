package ru.robot.cartesian.coordinates;
/**
 * Base coordinates
 */

import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;

public class Coordinates {
    private double x;
    private double y;
    private double z;

    public Coordinates(double _x, double _y, double _z)  {
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public double getZ(){
        return this.z;
    }

    public Coordinates(DoubleMatrix1D matrix) {
        if (matrix.toArray().length > 3 || matrix.toArray().length <3 ) {
            throw new RuntimeException("Wrong size, ");
        }
        this.x = matrix.get(0);
        this.y = matrix.get(1);
        this.z = matrix.get(2);

    }

    public DoubleMatrix2D getCoordinatesMatrix() {
        DoubleMatrix2D coordinates;
        double[][] c = new double[3][1];
        c[0][0] = this.getX();
        c[1][0] = this.getY();
        c[2][0] = this.getZ();
        DoubleFactory2D factory = DoubleFactory2D.dense;
        return coordinates = factory.make(c);
    }


}

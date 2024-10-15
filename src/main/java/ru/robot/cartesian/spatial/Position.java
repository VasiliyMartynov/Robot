package ru.robot.cartesian.spatial;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import ru.robot.cartesian.coordinates.Coordinates;

public class Position {

    private Coordinates coordinates;

    public Position(double x, double y, double z) {
        this(new Coordinates(x,y,z));
    }

    public Position(DoubleMatrix1D m) {
        this(new Coordinates(m));
    }

    public Position(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Position() {

    }

    @Override
    public String toString(){
        return ("x " + String.valueOf(this.coordinates.getX())
                + ", " +
                "y " + String.valueOf(this.coordinates.getY())
                + ", " +
                "z " + String.valueOf(this.coordinates.getZ())) + ";";
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }
}

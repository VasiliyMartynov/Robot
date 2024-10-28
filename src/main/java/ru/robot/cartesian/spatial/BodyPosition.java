package ru.robot.cartesian.spatial;

import org.ujmp.core.DenseMatrix2D;
import ru.robot.cartesian.coordinates.Coordinates;

import java.math.BigDecimal;

public class BodyPosition {

    private Coordinates coordinates;

    public BodyPosition(BigDecimal x, BigDecimal y, BigDecimal z) {
        this(new Coordinates(x,y,z));
    }

    public BodyPosition(DenseMatrix2D m) {
        this(new Coordinates(m));
    }

    public BodyPosition(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public BodyPosition() {

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

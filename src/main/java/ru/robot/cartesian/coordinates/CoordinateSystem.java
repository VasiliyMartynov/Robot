package ru.robot.cartesian.coordinates;

import ru.robot.cartesian.spatial.BodyRotation;
import ru.robot.cartesian.spatial.Position;

public class CoordinateSystem {
    private String name;
    private CoordinateSystem referenceCoordinateSystem;
    private Position zeroPosition;
    private BodyRotation bodyRotation;

    public CoordinateSystem(String _name, CoordinateSystem _referenceCoordinateSystem, Position _zeroPosition, BodyRotation _Body_rotation) {
        this.name = _name;
        this.referenceCoordinateSystem = _referenceCoordinateSystem;
        this.zeroPosition = _zeroPosition;
        this.bodyRotation = _Body_rotation;
    }

    public CoordinateSystem(String _name, Position _zeroPosition, BodyRotation _Body_rotation) {
        this.name = _name;
        this.zeroPosition = _zeroPosition;
        this.bodyRotation = _Body_rotation;
    }

    public CoordinateSystem(String _name){
        this.name = _name;
    }

    public void setZeroPosition(double x, double y, double z) {
        this.zeroPosition.setCoordinates(new Coordinates(x,y,z));
    }
    @Override
    public String toString() {
        String name = "Coordinate system name is \""
                + this.getName()
                + "\""
                + "\n";
        String position = "Coordinate system \""
                + this.getName()
                + "\" "
                + "position is "
                + this.getZeroPosition()
                + "\n";
        String orientation = "Coordinate system \""
                + this.getName()
                + "\" "
                +  "orientation is \n"
                +  this.getOrientation()
                + "\n";
        String referenceCoordinateSystem = "";

        if (this.getReferenceCoordinateSystem() != null) {
            referenceCoordinateSystem =
                    "Reference coordinate system of \""
                            + this.getName()
                            + "\" "
                            + "is coordinate system: "
                            + "\""
                            + this.getReferenceCoordinateSystem().getName()
                            + "\" coordinate system." ;
        }
    return name + position + orientation + referenceCoordinateSystem;
    }

    private CoordinateSystem getReferenceCoordinateSystem() {
        return this.referenceCoordinateSystem;
    }

    private BodyRotation getOrientation() {
        return this.bodyRotation;
    }

    private Position getZeroPosition() {
        return this.zeroPosition;
    }

    private String getName() {
        return this.name;
    }

    public String getInfo(){
        return this.toString();
    }

}
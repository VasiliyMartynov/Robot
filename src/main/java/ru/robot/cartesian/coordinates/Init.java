package ru.robot.cartesian.coordinates;



import cern.colt.matrix.tdouble.DoubleFactory2D;
import javafx.geometry.Orientation;
import ru.robot.cartesian.spatial.BodyRotation;
import ru.robot.cartesian.spatial.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Init {
    static public CoordinateSystem getGlobalCoordinateSystem() {
        String name = "Global";
        Position zeroPosition = new Position();
        var I = DoubleFactory2D.dense.identity(3);
        BodyRotation orientation = new BodyRotation(I);
        CoordinateSystem globalCoordinateSystem = new CoordinateSystem(name, zeroPosition, orientation);
        globalCoordinateSystem.setZeroPosition(0,0,0);
        return  globalCoordinateSystem;
    }
}

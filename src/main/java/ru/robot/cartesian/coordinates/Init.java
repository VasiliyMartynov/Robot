package ru.robot.cartesian.coordinates;

import ru.robot.cartesian.spatial.BodyRotation;
import ru.robot.cartesian.spatial.Position;
import ru.robot.cartesian.spatial.RMatrix;

import java.math.BigDecimal;

public class Init {
    static public CoordinateSystem getGlobalCoordinateSystem() throws InstantiationException {
        String name = "Global";
        Position zeroPosition = new Position();
        var I = RMatrix.getIdentityMatrix();
        BodyRotation orientation = new BodyRotation(I);
        CoordinateSystem globalCoordinateSystem = new CoordinateSystem(name, zeroPosition, orientation);
        var x = BigDecimal.ZERO;
        var y = BigDecimal.ZERO;
        var z = BigDecimal.ZERO;
        globalCoordinateSystem.setZeroPosition(x,y,z);
        return  globalCoordinateSystem;
    }
}

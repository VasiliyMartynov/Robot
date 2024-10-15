package ru.robot.cartesian.spatial;


import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import ru.robot.cartesian.coordinates.CoordinateSystem;

public class RigidBodyMotion {
    DoubleFactory2D factory = DoubleFactory2D.dense;
    DoubleMatrix2D rigidBodyMotionMatrix;
    CoordinateSystem frameCoordinateSystem;

//    public RigidBodyMotion(Position p, BodyRotation o) {
//        DoubleMatrix2D[][] f = new DoubleMatrix2D[1][4];
//        f[0][0] = o.getXVector();
//        f[0][1] = o.getYVector();
//        f[0][2] = o.getZVector();
//        f[0][3] = p.getCoordinates().getCoordinatesMatrix();
//        rigidBodyMotionMatrix = factory.compose(f);
//    }

    public RigidBodyMotion() {
    }
}

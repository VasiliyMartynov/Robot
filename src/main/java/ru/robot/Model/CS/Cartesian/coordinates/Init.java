//package ru.robot.Model.CS.Cartesian.coordinates;
//
//import ru.robot.Model.Rigid.BodyRotation;
//import ru.robot.Model.Rigid.BodyPosition;
//import ru.robot.Model.DS.Base.RMatrix;
//
//import java.math.BigDecimal;
//
//public class Init {
//    static public CoordinateSystem getGlobalCoordinateSystem(){
//        String name = "Global";
//        BodyPosition zeroPosition = new BodyPosition();
//        var I = RMatrix.getIdentityMatrix(3);
//        BodyRotation orientation = new BodyRotation(I);
//        CoordinateSystem globalCoordinateSystem = new CoordinateSystem(name, zeroPosition, orientation);
//        var x = BigDecimal.ZERO;
//        var y = BigDecimal.ZERO;
//        var z = BigDecimal.ZERO;
//        globalCoordinateSystem.setZeroPosition(x,y,z);
//        return  globalCoordinateSystem;
//    }
//}

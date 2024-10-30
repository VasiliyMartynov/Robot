//package ru.robot.Model.Rigid;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInfo;
//import ru.robot.Model.DataStructure.Base.RMatrix;
//
//import java.math.BigDecimal;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class BodyRotationTest {
//
//    @BeforeEach
//    void init(TestInfo testInfo) {
//        System.out.println(testInfo.getDisplayName());
//    }
//
//    private RMatrix getGoodRotationMatrix(){
//        RMatrix a = new RMatrix(3);
//        a.set(new BigDecimal("0.804738"), 0, 0);
//        a.set(new BigDecimal("-0.310617"), 0, 1);
//        a.set(new BigDecimal("0.505879"), 0, 2);
//        a.set(new BigDecimal("0.505879"), 1, 0);
//        a.set(new BigDecimal("0.804738"), 1, 1);
//        a.set(new BigDecimal("-0.310617"), 1, 2);
//        a.set(new BigDecimal("-0.310617"), 2, 0);
//        a.set(new BigDecimal("0.505879"), 2, 1);
//        a.set(new BigDecimal("0.804738"), 2, 2);
//        return a;
//    }
//
//    private RMatrix getBadRotationMatrix(){
//        RMatrix a = new RMatrix(3);
//        a.set(new BigDecimal("0.004738"), 0, 0);
//        a.set(new BigDecimal("-0.310617"), 0, 1);
//        a.set( new BigDecimal("0.505879"), 0, 2);
//        a.set(new BigDecimal("0.505879"), 1, 0);
//        a.set(new BigDecimal("0.804738"), 1, 1);
//        a.set(new BigDecimal("-0.310617"), 1, 2);
//        a.set(new BigDecimal("-0.310617"), 2, 0);
//        a.set(new BigDecimal("0.505879"), 2, 1);
//        a.set(new BigDecimal("0.804738"), 2, 2);
//        return a;
//    }
//
//    @Test
//    void BodyRotationListConstructorTestIsOK() throws InstantiationException {
//        var m = getGoodRotationMatrix();
//        BodyRotation b = new BodyRotation(m);
//        for(int i = 0; i < b.rotationMatrixData.getRowCount(); i++) {
//            for(int j = 0; j < b.rotationMatrixData.getColumnCount(); j++) {
//                assertEquals(m.get(i,j), b.getItem(i, j));
//            }
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////
////    @Test
////    void calcAxisAngleCaseCaseAifMatrixIsIdentity() throws InstantiationException {
////        var a = new BodyRotation(getIdentityMatrix());
////        var b = a.getAngleAxisFromLocalSO3();
////        assertEquals(0.0, b.getAsDouble(0,0));
////        assertEquals(0.0, b.getAsDouble(1,0));
////        assertEquals(0.0, b.getAsDouble(2,0));
////        assertEquals(0.0, b.getAsDouble(3,0));
////
////    }
//////
////    @Test
////    void calcAxisAngleCaseCifTraceIsMinusOne() throws InstantiationException {
////        var a = new BodyRotation(getGoodRotationMatrix());
////        var b = a.getAngleAxisFromLocalSO3();
////        //0.577350 0.577350 0.577350 0.785398
////        assertEquals(0.577350, b.getAsDouble(0,0));
////        assertEquals(0.577350, b.getAsDouble(1,0));
////        assertEquals(0.577350, b.getAsDouble(2,0));
////        assertEquals(0.785398, b.getAsDouble(3,0));
////    }
//
////    @Test
////    void getAxisAngleCaseBTest() throws InstantiationException {
////        DoubleMatrix2D m = new DenseDoubleMatrix2D(3,3);;
////
////        //axis angle 0.707 0.707 0, 0.519
////        var a = new BodyRotation(m);
////        var b = a.getAxisAngle();
////        assertEquals(.707, b.get(0));
////        assertEquals(.707, b.get(1));
////        assertEquals(0, b.get(2));
////        assertEquals(0.519, b.get(3));
////    }
//
//}

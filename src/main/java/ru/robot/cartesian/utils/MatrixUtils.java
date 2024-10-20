package ru.robot.cartesian.utils;

import org.ujmp.core.Matrix;
import org.ujmp.core.bigdecimalmatrix.impl.DefaultDenseBigDecimalMatrix2D;
import java.math.BigDecimal;

import java.util.List;

public class MatrixUtils {

    public static Matrix getIdentityMatrix(){
        Matrix I = new DefaultDenseBigDecimalMatrix2D(3, 3);
        I.setAsBigDecimal(BigDecimal.ONE, 0, 0);
        I.setAsBigDecimal(BigDecimal.ZERO, 0, 1);
        I.setAsBigDecimal(BigDecimal.ZERO, 0, 2);
        I.setAsBigDecimal(BigDecimal.ZERO, 1, 0);
        I.setAsBigDecimal(BigDecimal.ONE, 1, 1);
        I.setAsBigDecimal(BigDecimal.ZERO, 1, 2);
        I.setAsBigDecimal(BigDecimal.ZERO, 2, 0);
        I.setAsBigDecimal(BigDecimal.ZERO, 2, 1);
        I.setAsBigDecimal(BigDecimal.ONE, 2, 2);
        return I;
    }

    public static BigDecimal getDeterminantOfx3(Matrix m){
        var a = m.getAsBigDecimal(0, 0);
        var b = m.getAsBigDecimal(0, 1);
        var c = m.getAsBigDecimal(0, 2);

        var d = m.getAsBigDecimal(1, 0);
        var e = m.getAsBigDecimal(1, 1);
        var f = m.getAsBigDecimal(1, 2);

        var g = m.getAsBigDecimal(2, 0);
        var h = m.getAsBigDecimal(2, 1);
        var i = m.getAsBigDecimal(2, 2);

        //|A| = a(ei − fh) − b(di − fg) + c(dh − eg)

        // part 1 a(ei − fh)
        //e*i
        var ei = e.multiply(i);
        //f*h
        var fh = f.multiply(h );
        //ei − fh
        var eiMinusFh = ei.subtract(fh);
        // a(ei − fh)
        BigDecimal p1 = a.multiply(eiMinusFh);

        // part 2 b(di − fg)
        //e*i
        var di = d.multiply(i);
        //f*h
        var fg = f.multiply(g);
        //di − fg
        var diMinusFg = di.subtract(fg);
        // b(di − fg)
        var p2 = b.multiply(diMinusFg);

        // part 3 c(dh − eg)
        //dh
        var dh = d.multiply(h);
        //eg
        var eg = e.multiply(g);
        //dh − eg
        var dhMinusEg = dh.subtract(eg);
        // c(dh − eg)
        var p3 = c.multiply(dhMinusEg);

        //result
        var r1 = p1.subtract(p2);
        return r1.add(p3).round(GVARS.MC6);
    }

    public static Matrix roundValuesOfMatrix(Matrix m){
        Matrix rounded = new DefaultDenseBigDecimalMatrix2D(3,3);
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j = 0; j < m.getColumnCount(); j++) {
//                System.out.println(m.getAsDouble(i,j));
                rounded.setAsBigDecimal(m.getAsBigDecimal(i,j).round(GVARS.MC6), i,j);
//                System.out.println(rounded.getAsDouble(i,j));
            }
        }
        return  rounded;
    }

    public static Matrix setValues(List<BigDecimal> arrayList){
        Matrix m = new DefaultDenseBigDecimalMatrix2D(3,3);
        var index = 0;
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j = 0; j < m.getColumnCount(); j++) {
//                System.out.println(m.getAsDouble(i,j));
                m.setAsBigDecimal(arrayList.get(index), i,j);
                index++;
//                System.out.println(rounded.getAsDouble(i,j));
            }
        }
        return m;
    }
}

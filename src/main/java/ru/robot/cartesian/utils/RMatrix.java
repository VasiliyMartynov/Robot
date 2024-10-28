package ru.robot.cartesian.utils;

import org.ujmp.core.Matrix;
import org.ujmp.core.bigdecimalmatrix.impl.DefaultDenseBigDecimalMatrix2D;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static ru.robot.cartesian.utils.YESNO.NO;
import static ru.robot.cartesian.utils.GVARS.*;

public class RMatrix {
    Matrix data;

    RMatrix(Matrix m){
        this.data = m;
    }

    public RMatrix(int m){
        this.data = new DefaultDenseBigDecimalMatrix2D(m,m);
    }

    public BigDecimal get(long... coordinates){
        return this.data.getAsBigDecimal(coordinates);
    }

    public double getDouble(long... coordinates){
        return this.data.getAsBigDecimal(coordinates).doubleValue();
    }


    public static RMatrix getIdentityMatrix(){
        List<BigDecimal> list = Arrays.asList(ONE,ZERO,ZERO,ZERO,ONE,ZERO,ZERO,ZERO,ONE);
        return setValues3x3(list, NO);
    }

    public static RMatrix getZerosMatrix(){
        List<BigDecimal> list = Arrays.asList(ZERO,ZERO,ZERO,ZERO,ZERO,ZERO,ZERO,ZERO,ZERO);
        return setValues3x3(list, NO);
    }

    public static RMatrix setValues3x3(List<BigDecimal> arrayList, YESNO needTobeRound){
        Matrix m = new DefaultDenseBigDecimalMatrix2D(3,3);
        var index = 0;
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j = 0; j < m.getColumnCount(); j++) {
                switch (needTobeRound) {
                    case NO -> {
                        m.setAsBigDecimal(arrayList.get(index), i,j);
                    }
                    case YES -> {
                        m.setAsBigDecimal(arrayList.get(index).round(MC6), i,j);
                    }
                }
                index++;
            }
        }
        return new RMatrix(m);
    }

    public void set(BigDecimal value, long... coordinates){
        this.data.setAsBigDecimal(value, coordinates);
    }

    public static RMatrix roundValuesOfMatrix(RMatrix m){
        Matrix rounded = new DefaultDenseBigDecimalMatrix2D(3,3);
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j = 0; j < m.getColumnCount(); j++) {
                rounded.setAsBigDecimal(m.get(i,j).round(MC6), i,j);
            }
        }
        return  new RMatrix(rounded);
    }

    public Matrix getData() {
        return data;
    }

    public static BigDecimal getDeterminantOfx3(RMatrix m){
        var a = m.get(0, 0);
        var b = m.get(0, 1);
        var c = m.get(0, 2);

        var d = m.get(1, 0);
        var e = m.get(1, 1);
        var f = m.get(1, 2);

        var g = m.get(2, 0);
        var h = m.get(2, 1);
        var i = m.get(2, 2);

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

    public long getRowCount() {
        return this.data.getRowCount();
    }

    public long getColumnCount() {
        return this.data.getColumnCount();
    }

    public static RMatrix mult(RMatrix m, RMatrix n) {
        return new RMatrix(m.data.mtimes(n.data));
    }

    public RMatrix mult(BigDecimal scalar) {
        return new RMatrix(this.data.times(scalar.doubleValue()));
    }

    public RMatrix mult(RMatrix m) {
        return new RMatrix(this.data.mtimes(m.data));
    }

    public RMatrix divide(BigDecimal scalar) {
        return new RMatrix(this.data.divide(scalar.doubleValue()));
    }

    public RMatrix plus(RMatrix m){
        return new RMatrix(this.data.plus(m.data));
    }

    public static RMatrix divide(RMatrix m, RMatrix n) {
        return new RMatrix(m.data.divide(n.data));
    }

    public static BigDecimal trace(RMatrix m) {
        return BigDecimal.valueOf(m.data.trace());
    }

    public static boolean equalsContent(RMatrix m, RMatrix n) {
        return m.data.equalsContent(n.data);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Rotation matrix:\n");
        for(int i = 0; i < this.data.getRowCount(); i++) {
            for(int j = 0; j < this.data.getColumnCount(); j++) {
                s.append(this.data.getAsDouble(i, j));
                s.append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}

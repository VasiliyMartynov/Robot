package ru.robot.Model.DataStructure.Base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ujmp.core.Matrix;
import org.ujmp.core.bigdecimalmatrix.impl.DefaultDenseBigDecimalMatrix2D;
import ru.robot.Model.CoordinateSystem.Cartesian.Utils.YESNO;
import java.math.BigDecimal;
import java.util.List;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.nearZero;
import static ru.robot.Environment.Global.*;

public class RMatrix {

    private static final Logger LOGGER = LogManager.getLogger();
    Matrix data;
    int size;

    public RMatrix(int size){
        this.data = new DefaultDenseBigDecimalMatrix2D(size, size);
        this.size = size;
    }

    public RMatrix(List<BigDecimal> list) {
        Double s = Math.sqrt(list.size());
        this.data = setMatrixValues(list,s);
        this.size = s.intValue();
    }

    public RMatrix(Matrix R) throws IllegalArgumentException {
        this.data = R;
    }

    public int getSize(){
        return this.size;
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
        return r1.add(p3).round(MC6);
    }




    public static RMatrix RotInv(RMatrix R){
        var invR = R.getData().inv();
        return new RMatrix(invR);
    }


    private static Matrix setMatrixValues(List<BigDecimal> arrayList, Double size){
        Matrix m = new DefaultDenseBigDecimalMatrix2D(size.intValue(),size.intValue());
        var index = 0;
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j = 0; j < m.getColumnCount(); j++) {
                m.setAsBigDecimal(arrayList.get(index), i,j);
                index++;
            }
        }
        return m;
    }

//    public static RMatrix setValues(List<BigDecimal> arrayList, YESNO needToBeRounded, int size){
//        RMatrix m = new RMatrix(size);
//        var index = 0;
//        for(int i = 0; i < m.getRowCount(); i++) {
//            for(int j = 0; j < m.getColumnCount(); j++) {
//                switch (needToBeRounded){
//                    case NO -> {
//                        m.set(arrayList.get(index), i,j);
//                    }
//                    case YES -> {
//                        m.set(arrayList.get(index).round(MC6), i,j);
//                    }
//                }
//                index++;
//            }
//        }
//        return m;
//    }

    public static RMatrix setMatrixValues(RMatrix matrix, YESNO needToBeRounded, int size){
        RMatrix m = new RMatrix(size);
        var index = 0;
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j = 0; j < m.getColumnCount(); j++) {
                switch (needToBeRounded){
                    case NO -> {
                        m.set(matrix.get(i,j), i,j);
                    }
                    case YES -> {
                        m.set(matrix.get(i,j).round(MC6), i,j);
                    }
                }
                index++;
            }
        }
        return m;
    }

    public BigDecimal get(long... coordinates){
        return this.data.getAsBigDecimal(coordinates);
    }

    public double getDouble(long... coordinates){
        return this.data.getAsBigDecimal(coordinates).doubleValue();
    }


    public static RMatrix getIdentityMatrix(int size){
        var R = new RMatrix(size);
        int row, col;
            for (row = 0; row < size; row++)
            {
                for (col = 0; col < size; col++)
                {
                    // Checking if row is equal to column
                    if (row == col){
                        R.set(ONE, row,col);
                    }
                    else
                        R.set(ZERO, row,col);
                }
            }
        return R;
    }

    public static RMatrix getZerosMatrix(int size){
        var R = new RMatrix(size);
        int row, col;
        for (row = 0; row < size; row++)
        {
            for (col = 0; col < size; col++)
            {
                R.set(ZERO, row,col);
            }
        }
        return R;
    }

    public void set(BigDecimal value, long... coordinates){
        this.data.setAsBigDecimal(value, coordinates);
    }

    public static RMatrix roundValuesOfRMatrix(RMatrix m){
        Matrix rounded = new DefaultDenseBigDecimalMatrix2D(3,3);
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j = 0; j < m.getColumnCount(); j++) {
                rounded.setAsBigDecimal(m.get(i,j).round(MC6), i,j);
            }
        }
        return  new RMatrix(rounded);
    }

    public static Matrix roundValuesOfMatrix(Matrix m){
        Matrix rounded = new DefaultDenseBigDecimalMatrix2D(3,3);
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j = 0; j < m.getColumnCount(); j++) {
                if(nearZero(m.getAsBigDecimal(i,j)) == -1) {
                    rounded.setAsBigDecimal(BigDecimal.ZERO, i,j);
                } else {
                    rounded.setAsBigDecimal(m.getAsBigDecimal(i,j).round(MC6), i,j);
                }
            }
        }
        return rounded;
    }

    public Matrix getData() {
        return data;
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
        s.append("Matrix:\n");
        for(int i = 0; i < this.data.getRowCount(); i++) {
            for(int j = 0; j < this.data.getColumnCount(); j++) {
                s.append(this.data.getAsDouble(i, j));
                s.append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static boolean compareMatrices(Matrix n, Matrix m){
        boolean result = false;
        BigDecimal x, y;
        var roundedN = roundValuesOfMatrix(n);
        var roundedM = roundValuesOfMatrix(m);
        for(long i = 0; i < n.getColumnCount(); i++) {
            for(long j = 0; j < n.getRowCount(); j++) {
                x = roundedN.getAsBigDecimal(i,j);
                y = roundedM.getAsBigDecimal(i,j);
                if (x.compareTo(y) == 0){
                    result = true;
                } else {
                    return false;
                }
            }
        }
        return result;
    }

    public static  boolean haveSize(Matrix R){
        var rows = R.getRowCount();
        var columns = R.getColumnCount();
        if (rows == columns || rows == 3){
            return true;
        } else {
            return false;
        }
    }

}


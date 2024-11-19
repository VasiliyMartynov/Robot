package ru.robot.Model.DS.Base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ujmp.core.Matrix;
import org.ujmp.core.bigdecimalmatrix.impl.DefaultDenseBigDecimalMatrix2D;

import java.math.BigDecimal;

public class RVector {
    private static final Logger LOGGER = LogManager.getLogger();
    Matrix data;

    public RVector(int size){
        this.data = new DefaultDenseBigDecimalMatrix2D(size,1);
    }

    public Matrix getData(){
        return this.data;
    }

//    public RVector(BigDecimal x1, BigDecimal x2, BigDecimal x3){
//        this.data = new DefaultDenseBigDecimalMatrix2D(3,1);
//        this.data.setAsBigDecimal(x1, 0,0);
//        this.data.setAsBigDecimal(x2, 1,0);
//        this.data.setAsBigDecimal(x3, 2,0);
//    }
//
//    public RVector(BigDecimal x1, BigDecimal x2, BigDecimal x3, BigDecimal x4){
//        this.data = new DefaultDenseBigDecimalMatrix2D(4,1);
//        this.data.setAsBigDecimal(x1, 0,0);
//        this.data.setAsBigDecimal(x2, 1,0);
//        this.data.setAsBigDecimal(x3, 2,0);
//        this.data.setAsBigDecimal(x4, 3,0);
//    }

    private RVector(Matrix m) {
        //LOGGER.info("RVector constructor input matrix \n'{}'", m );
        //System.out.println("input matrix\n" + m.toString());
        if(m.getColumnCount() > 1) {
            throw new IllegalArgumentException("Input matrix isn't vector");
        }
        this.data = m;

    }

    public BigDecimal get(int item){
        return this.data.getAsBigDecimal(item,0);
    }

    public  void set(int item, BigDecimal value){
        this.data.setAsBigDecimal(value, item,0);
    }

    public int size(){
        return this.data.getSize().length;
    }



    public static RVector mult(RMatrix m, RVector n) {
        return new RVector(m.data.mtimes(n.data));
    }

    public static RVector mult(RVector m, RVector n) {
        return new RVector(m.data.mtimes(n.data));
    }

    public static RVector mult(RVector v, BigDecimal scalar) {
        return new RVector(v.data.times(scalar.doubleValue()));
    }

    public static RVector divide(RVector m, BigDecimal n) {
        return new RVector(m.data.divide(n.doubleValue()));
    }


    /**
     * calc length of a vector
     *     :param V: A vector
     *     :return: A unit vector pointing in the same direction as z
     *     Example Input:
     *         V = matrix 0.26726124, 0.53452248, 0.80178373
     *     Output:
     *         3.74166
     */

//    public double getAsDouble(long... coordinates) {
//        return this.data.getAsDouble(coordinates);
//    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Vector:\n");
        for(int i = 0; i < this.data.getRowCount(); i++) {

                s.append(this.data.getAsDouble(i, 0));
                s.append(" ");

            s.append("\n");
        }
        return s.toString();
    }
}

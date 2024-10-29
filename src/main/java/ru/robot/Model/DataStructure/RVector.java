package ru.robot.Model.DataStructure;

import org.ujmp.core.Matrix;
import org.ujmp.core.bigdecimalmatrix.impl.DefaultDenseBigDecimalMatrix2D;

import java.math.BigDecimal;

import static ru.robot.Environment.Global.MC6;

public class RVector {
    Matrix data;

    public RVector(int size){
        this.data = new DefaultDenseBigDecimalMatrix2D(size,1);
    }

    public RVector(BigDecimal x1, BigDecimal x2, BigDecimal x3){
        this.data = new DefaultDenseBigDecimalMatrix2D(3,1);
        this.data.setAsBigDecimal(x1, 0,0);
        this.data.setAsBigDecimal(x2, 1,0);
        this.data.setAsBigDecimal(x3, 2,0);
    }

    public RVector(BigDecimal x1, BigDecimal x2, BigDecimal x3, BigDecimal x4){
        this.data = new DefaultDenseBigDecimalMatrix2D(4,1);
        this.data.setAsBigDecimal(x1, 0,0);
        this.data.setAsBigDecimal(x2, 1,0);
        this.data.setAsBigDecimal(x3, 2,0);
        this.data.setAsBigDecimal(x4, 3,0);
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

    public static BigDecimal normOfVector(RVector V3) {

        var v1 = V3.get(0);
        var v2 = V3.get(1);
        var v3 = V3.get(2);
        var v1pow2 = v1.pow(2);
        var v2pow2 = v2.pow(2);
        var v3pow2 = v3.pow(2);
        var v1powPlusV2pow2 = v1pow2.add(v2pow2);
        var v1powPlusV2pow2PlusV3 = v1powPlusV2pow2.add(v3pow2);
        return v1powPlusV2pow2PlusV3.sqrt(MC6);
    }

    /**
     * Normalizes a vector
     *
     *     :param V: A vector
     *     :return: A unit vector pointing in the same direction as z
     *
     *     Example Input:
     *         V = np.array([1, 2, 3])
     *     Output:
     *         np.array([0.26726124, 0.53452248, 0.80178373])
     */
    public static RVector normaliseVector(RVector V) {
        var m1 = V.get(0).divide(normOfVector(V),MC6);
        var m2 = V.get(1).divide(normOfVector(V),MC6);
        var m3 = V.get(2).divide(normOfVector(V),MC6);
        return new RVector(m1, m2, m3);
    }

    /**
     * calc length of a vector
     *
     *     :param V: A vector
     *     :return: A unit vector pointing in the same direction as z
     *
     *     Example Input:
     *         V = matrix 0.26726124, 0.53452248, 0.80178373
     *     Output:
     *         3.74166
     */
    public static BigDecimal normOfVector(RMatrix V3) {

        var v1 = V3.get(0,0);
        var v2 = V3.get(1,0);
        var v3 = V3.get(2,0);
        var v1pow2 = v1.pow(2);
        var v2pow2 = v2.pow(2);
        var v3pow2 = v3.pow(2);
        var v1powPlusV2pow2 = v1pow2.add(v2pow2);
        var v1powPlusV2pow2PlusV3 = v1powPlusV2pow2.add(v3pow2);
        return v1powPlusV2pow2PlusV3.sqrt(MC6);
    }

    public double getAsDouble(long... coordinates) {
        return this.data.getAsDouble(coordinates);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Rotation matrix:\n");
        for(int i = 0; i < this.data.getRowCount(); i++) {

                s.append(this.data.getAsDouble(i, 0));
                s.append(" ");

            s.append("\n");
        }
        return s.toString();
    }
}

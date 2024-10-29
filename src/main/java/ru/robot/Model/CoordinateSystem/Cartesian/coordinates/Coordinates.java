package ru.robot.Model.CoordinateSystem.Cartesian.coordinates;


import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import java.math.BigDecimal;

public class Coordinates {
    private final BigDecimal x;
    private final BigDecimal y;
    private final BigDecimal z;

    public Coordinates(BigDecimal _x, BigDecimal _y, BigDecimal _z)  {
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }

    public BigDecimal getX(){
        return this.x;
    }

    public BigDecimal getY(){
        return this.y;
    }

    public BigDecimal getZ(){
        return this.z;
    }

    public Coordinates(Matrix matrix) {
        if (matrix.getRowCount() > 3 || matrix.getColumnCount() <3 ) {
            throw new RuntimeException("Wrong size, ");
        }
        this.x = new BigDecimal(matrix.getAsString(0,0));
        this.y = new BigDecimal(matrix.getAsString(0,0));
        this.z = new BigDecimal(matrix.getAsString(0,0));
    }

    public Matrix  getCoordinatesMatrix() {
        BigDecimal[] c = {this.getX(), this.getY(), this.getZ()};
        return DenseMatrix.Factory.importFromArray(c);
    }


}

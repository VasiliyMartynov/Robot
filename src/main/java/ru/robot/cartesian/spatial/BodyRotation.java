package ru.robot.cartesian.spatial;

import ru.robot.cartesian.utils.RMatrix;

import java.math.BigDecimal;
import static ru.robot.cartesian.utils.RMatrix.*;
import static ru.robot.cartesian.utils.GVARS.*;
import static ru.robot.cartesian.utils.Utils.*;

public class BodyRotation {

    RMatrix rotationMatrixData;

    //Constructors section start

    public BodyRotation(RMatrix m) throws InstantiationException {
        var det = getDeterminantOfx3(m);
        System.out.println("Determinant of input Matrix is " + det );
        var diff = ONE.subtract(det, MC6);
        System.out.println("Diff is "+ diff.doubleValue());
        if (nearZero(diff) < 0) {
            System.out.println("Body rotation instance were created");
            this.rotationMatrixData = m;
        } else {
            throw new InstantiationException(
                "Body rotation cannot be instantiated, " +
                "because Matrix isn't R ∈ SO(3), " +
                "det R not equals 1, please check input values"
                );}
    }
    //Constructors section end

    public RMatrix getLocalRotationMatrix() {
        return this.rotationMatrixData;
    }

    public BigDecimal getItem(int row, int column) {
        return this.rotationMatrixData.get(row,column);
    }

    public double getItemAsDouble(int row, int column) {
        return this.rotationMatrixData.get(row,column).doubleValue();
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Rotation matrix:\n");
        for(int i = 0; i < this.rotationMatrixData.getRowCount(); i++) {
            for(int j = 0; j < this.rotationMatrixData.getColumnCount(); j++) {
                s.append(this.rotationMatrixData.get(i, j).doubleValue());
                s.append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

//    MATH











}


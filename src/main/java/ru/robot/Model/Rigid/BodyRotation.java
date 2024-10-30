package ru.robot.Model.Rigid;

import ru.robot.Model.DataStructure.Base.RMatrix;
import java.math.BigDecimal;


public class BodyRotation {

    public RMatrix rotationMatrixData;

    //Constructors section start

    public BodyRotation(RMatrix R){
        this.rotationMatrixData = R;
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

}


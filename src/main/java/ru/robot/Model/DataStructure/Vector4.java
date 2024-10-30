package ru.robot.Model.DataStructure;

import ru.robot.Model.DataStructure.Base.RVector;

import java.math.BigDecimal;

public class Vector4 {
    int size = 4;
    RVector data = new RVector(size);

    public Vector4(BigDecimal i1, BigDecimal i2, BigDecimal i3, BigDecimal i4){
        this.data.set(0, i1);
        this.data.set(1, i2);
        this.data.set(2, i3);
        this.data.set(3, i4);
    }

    public RVector getData(){
        return this.data;
    }

    public int getSize() {
        return size;
    }
}

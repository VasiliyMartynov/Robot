package ru.robot.Model.DataStructure;

import ru.robot.Model.DataStructure.Base.RVector;

import java.math.BigDecimal;

public class Vector3 {
    int size = 3;
    RVector data = new RVector(size);

    public Vector3(BigDecimal i1,BigDecimal i2,BigDecimal i3 ){
        this.data.set(0, i1);
        this.data.set(1, i2);
        this.data.set(2, i3);
    }

    public RVector getData(){
        return this.data;
    }

    public int getSize() {
        return size;
    }
}

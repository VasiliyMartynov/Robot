package ru.robot.Model.DataStructure;

import ru.robot.Model.DataStructure.Base.RVector;

import java.math.BigDecimal;

public class Vector6 {
    int size = 6;
    RVector data = new RVector(size);

    public Vector6(BigDecimal i1, BigDecimal i2, BigDecimal i3, BigDecimal i4, BigDecimal i5 , BigDecimal i6){
        this.data.set(0, i1);
        this.data.set(1, i2);
        this.data.set(2, i3);
        this.data.set(3, i4);
        this.data.set(4, i5);
        this.data.set(5, i6);
    }

    public RVector getData(){
        return this.data;
    }

    public BigDecimal getItem(int i){
        return this.data.get(i);
    }

    public int getSize() {
        return size;
    }
}

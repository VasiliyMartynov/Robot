package ru.robot.Model.DataStructure;

import ru.robot.Logger;
import ru.robot.Model.DataStructure.Base.RVector;

import java.math.BigDecimal;

public class Vector7 {
    int size = 7;
    RVector data = new RVector(size);

    public Vector7(BigDecimal i1, BigDecimal i2, BigDecimal i3, BigDecimal i4, BigDecimal i5 , BigDecimal i6, BigDecimal i7){
        this.data.set(0, i1);
        this.data.set(1, i2);
        this.data.set(2, i3);
        this.data.set(3, i4);
        this.data.set(4, i5);
        this.data.set(5, i6);
        this.data.set(6, i7);
    }

    public Vector7(){

    }

    public RVector getData(){
        return this.data;
    }

    public BigDecimal getItem(int i){
        try{
            return this.data.get(i);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    public int getSize() {
        return size;
    }

    public void setItem(int i, BigDecimal value){
        this.data.set(i, value);
    }
}

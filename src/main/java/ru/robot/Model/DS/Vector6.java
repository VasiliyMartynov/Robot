package ru.robot.Model.DS;

import ru.robot.Model.DS.Base.RVector;

import java.math.BigDecimal;

import static ru.robot.Environment.Global.MC6;

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

    public Vector6(){

    }

    public Vector6(RVector r){
        if (r.size() <=this.size) {
            this.data = r;
        }
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

    public void setItem(int i, BigDecimal n){
        this.data.set(i, n);
    }

    public static Vector6 divide(Vector6 v, BigDecimal n){
        var result = new Vector6();
        var max = v.getSize() - 1;
        for(int i = 0; i <= max; i++){
            result.setItem(i, v.getItem(i).divide(n, MC6));
        }
        return result;
    }

    public static Vector6 mult(Vector6 v, BigDecimal n){
        var result = new Vector6();
        var max = v.getSize() - 1;
        for(int i = 0; i <= max; i++){
            result.setItem(i, v.getItem(i).divide(n, MC6));
        }
        return result;
    }
}

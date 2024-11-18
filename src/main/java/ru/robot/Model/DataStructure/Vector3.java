package ru.robot.Model.DataStructure;

import ru.robot.Model.DataStructure.Base.RVector;

import java.math.BigDecimal;

public class Vector3 {
    int size = 3;
    RVector data = new RVector(size);

    public Vector3(){

    }

    public Vector3(RVector r){
        if (r.size() <=this.size) {
            this.data = r;
        }
    }

    public Vector3(BigDecimal i1,BigDecimal i2,BigDecimal i3){
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

    public void setItem(int i, BigDecimal n){
        this.data.set(i, n);
    }

    public BigDecimal getItem(int i){
        return this.data.get(i);
    }

    public static Vector3 crossProduct(Vector3 a, Vector3 b){
        var result = new Vector3();
        //cross_P[0] = vect_A[1] * vect_B[2] - vect_A[2] * vect_B[1];
        var A1multiplyB2 = a.getItem(1).multiply(b.getItem(2));
        var A2multiplyB1 = a.getItem(2).multiply(b.getItem(1));
        result.setItem(0, A1multiplyB2.subtract(A2multiplyB1));

        //cross_P[1] = vect_A[2] * vect_B[0] - vect_A[0] * vect_B[2];
        var A2multiplyB0 = a.getItem(2).multiply(b.getItem(0));
        var A0multiplyB2 = a.getItem(0).multiply(b.getItem(2));
        result.setItem(1, A2multiplyB0.subtract(A0multiplyB2));

        //ross_P[2] = vect_A[0] * vect_B[1] - vect_A[1] * vect_B[0];
        var A0multiplyB1 = a.getItem(0).multiply(b.getItem(1));
        var A1multiplyB0 = a.getItem(1).multiply(b.getItem(0));
        result.setItem(2, A0multiplyB1.subtract(A1multiplyB0));
        return result;
    }

    public static Vector3 times(Vector3 v, BigDecimal h){
        var result = new Vector3();
        for(int i = 0; i <= 2; i++){
            result.setItem(i, v.getItem(i).multiply(h));
        }
        return result;
    }

    public static Vector3 plus(Vector3 a, Vector3 b){
        var result = new Vector3();
        for(int i = 0; i <= 2; i++){
            result.setItem(i, a.getItem(i).add(b.getItem(i)));
        }
        return result;
    }
}

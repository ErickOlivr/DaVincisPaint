package com.paintvetorial.model;

public class NoPonto {
    public int x, y;
    public NoPonto proximo;
    
    public NoPonto(int x, int y){
        this.x = x;
        this.y = y;
        this.proximo = null;
    }
}

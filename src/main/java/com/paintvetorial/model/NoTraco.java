package com.paintvetorial.model;
import java.awt.Color;

public class NoTraco {
    public Color cor;
    public int espessura;
    public NoPonto noCabeca;
    public NoTraco proximoTraco;
    public NoTraco tracoAnterior;

    public NoTraco (Color cor, int espessura){
        this.cor = cor;
        this.espessura = espessura;
        this.noCabeca = null;
        this.proximoTraco = null;
        this.tracoAnterior = null;
    }

    public void addPonto(int x, int y) {
            NoPonto novoPonto = new NoPonto(x, y);
    
            if (noCabeca == null) {
                noCabeca = novoPonto;
            } else {
                NoPonto atual = noCabeca;
                
                while (atual.proximo != null) {
                    atual = atual.proximo;
                }
                atual.proximo = novoPonto;
            }
        }
}
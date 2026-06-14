package com.paintvetorial.model;
import java.awt.Color;

public class ListaEncadeadaCustom {
    public NoTraco inicio;
    public NoTraco fim;
    
    public ListaEncadeadaCustom (){
    this.inicio = null;
    this.fim = null;
    }
    
    //Quando o evento MousePressed for ativado
    public void iniciarNovoTraco(Color cor, int espessura) {
            NoTraco novoTraco = new NoTraco(cor, espessura);
            if (inicio == null) {
                inicio = novoTraco;
                fim = novoTraco;
            } else {
                fim.proximoTraco = novoTraco;
                novoTraco.tracoAnterior = fim;
                fim = novoTraco;
            }
    }
    
    //Quando o evento MouseDragged é ativado ou seja o usuario arrasta o mouse
    public void adicionarPontoAoTracoAtual(int x, int y) {
            if (fim != null) {
                fim.addPonto(x, y); 
            }
        }
        
     public void desfazer() {
             if (fim == null) {
                 return;
             }
  
             if (inicio == fim) {
                 inicio = null;
                 fim = null;
             } else {
                 fim = fim.tracoAnterior;
                 fim.proximoTraco = null;
             }
         }
}

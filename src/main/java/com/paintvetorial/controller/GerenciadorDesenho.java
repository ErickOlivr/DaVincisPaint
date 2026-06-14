package com.paintvetorial.controller;

import com.paintvetorial.model.ListaEncadeadaCustom;
import com.paintvetorial.view.PainelDesenho;
import java.awt.Color;

public class GerenciadorDesenho {

    private final ListaEncadeadaCustom listaTracos;
    private final PainelDesenho painelDesenho;

    private Color corAtual;
    private int espessuraAtual;

    public GerenciadorDesenho(ListaEncadeadaCustom lista, PainelDesenho painel) {
        this.listaTracos = lista;
        this.painelDesenho = painel;

        // Padrão inicial: Branco com espessura 5
        this.corAtual = Color.WHITE;
        this.espessuraAtual = 5;
    }

    public Color getCorAtual() { return corAtual; }
    public void setCorAtual(Color novaCor) { this.corAtual = novaCor; }

    public int getEspessuraAtual() { return espessuraAtual; }
    public void setEspessuraAtual(int novaEspessura) { this.espessuraAtual = novaEspessura; }

    /**
     * Aciona o seu método O(1) de desfazer e limpa o Canvas
     */
    public void acionarDesfazer() {
        listaTracos.desfazer();
        painelDesenho.repaint();
    }

    /**
     * Limpa a tela reinicializando os ponteiros da lista
     */
    public void acionarLimpar() {
        listaTracos.inicio = null;
        listaTracos.fim = null;
        painelDesenho.repaint();
    }
}
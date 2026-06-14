package com.paintvetorial.controller;

import com.paintvetorial.model.ListaEncadeadaCustom;
import com.paintvetorial.view.PainelDesenho;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GerenciadorMouse extends MouseAdapter {

    private final ListaEncadeadaCustom listaTracos;
    private final PainelDesenho painelDesenho;
    private final GerenciadorDesenho gerenciadorDesenho;

    // Variável de controle para sabermos se o botão correto está pressionado
    private boolean desenhando = false;

    public GerenciadorMouse(ListaEncadeadaCustom lista, PainelDesenho painel, GerenciadorDesenho gerenciador) {
        this.listaTracos = lista;
        this.painelDesenho = painel;
        this.gerenciadorDesenho = gerenciador;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Aceita apenas o botão esquerdo do mouse
        if (e.getButton() != MouseEvent.BUTTON1) return;

        desenhando = true;

        // 1. Chama o seu método para instanciar e encadear o novo traço na lista
        listaTracos.iniciarNovoTraco(
                gerenciadorDesenho.getCorAtual(),
                gerenciadorDesenho.getEspessuraAtual()
        );

        // 2. Adiciona o primeiro ponto onde o clique inicial aconteceu
        listaTracos.adicionarPontoAoTracoAtual(e.getX(), e.getY());

        // 3. Renderiza o Canvas
        painelDesenho.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (desenhando) {
            // Adiciona os pontos continuamente ao traço que está no "fim" da lista
            listaTracos.adicionarPontoAoTracoAtual(e.getX(), e.getY());

            // Força a atualização do frame em tempo real
            painelDesenho.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            desenhando = false;
        }
    }
}
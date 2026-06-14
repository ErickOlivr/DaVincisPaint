package com.paintvetorial.view;

import com.paintvetorial.model.ListaEncadeadaCustom;
import com.paintvetorial.model.NoPonto;
import com.paintvetorial.model.NoTraco;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PainelDesenho extends JPanel{

    private final ListaEncadeadaCustom listaTracos;


    public PainelDesenho(ListaEncadeadaCustom lista){
        this.listaTracos = lista;

        setBackground(new Color(30,30,30));
    }

    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        NoTraco tracoAtual = listaTracos.inicio;

        while(tracoAtual != null){

            g2d.setColor(tracoAtual.cor);
            g2d.setStroke(new BasicStroke(
                    tracoAtual.espessura,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));


            NoPonto pontoAtual = tracoAtual.noCabeca;

            while(pontoAtual != null && pontoAtual.proximo != null){

                g2d.drawLine(
                        pontoAtual.x,
                        pontoAtual.y,
                        pontoAtual.proximo.x,
                        pontoAtual.proximo.y
                );

                pontoAtual = pontoAtual.proximo;

            }

            tracoAtual = tracoAtual.proximoTraco;
        }
    }
}

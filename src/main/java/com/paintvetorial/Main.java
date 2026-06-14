package com.paintvetorial;

import com.formdev.flatlaf.FlatDarkLaf;
import com.paintvetorial.view.JanelaPrincipal;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Ativa o Look and Feel moderno (FlatLaf Dark) antes de carregar a interface
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Aviso: Não foi possível carregar o visual FlatLaf. O sistema usará o Swing padrão.");
            e.printStackTrace();
        }

        // 2. Garante que a interface gráfica seja criada na Thread correta do Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            // Inicializa a janela principal do Paint
            JanelaPrincipal janela = new JanelaPrincipal();

            // Torna a janela visível na tela do usuário
            janela.setVisible(true);
        });
    }
}
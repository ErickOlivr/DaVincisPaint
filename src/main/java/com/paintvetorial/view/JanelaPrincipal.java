package com.paintvetorial.view;
import com.paintvetorial.controller.GerenciadorDesenho;
import com.paintvetorial.controller.GerenciadorMouse;
import com.paintvetorial.model.ListaEncadeadaCustom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;


public class JanelaPrincipal extends JFrame{

    private JLabel labelStatus, labelCorPreview;
    private JButton desfazer, limpar, cor;
    private JSlider espessuraPincel;

    private ListaEncadeadaCustom listaTracos;
    private PainelDesenho painelDesenho;
    private GerenciadorDesenho gerenciadorDesenho;
    private GerenciadorMouse gerenciadorMouse;

    public JanelaPrincipal(){

        setTitle("Da Vincis Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());      //configura o layout principal

        this.listaTracos = new ListaEncadeadaCustom();
        this.painelDesenho = new PainelDesenho(listaTracos);

        this.gerenciadorDesenho = new GerenciadorDesenho(listaTracos, painelDesenho);
        this.gerenciadorMouse = new GerenciadorMouse(listaTracos, painelDesenho, gerenciadorDesenho);

        painelDesenho.addMouseListener(gerenciadorMouse);
        painelDesenho.addMouseMotionListener(gerenciadorMouse);
        
        barraFerramentasSuperior();    //cria a barra de ferramentas superior
        barraStatusInferior();     //cria a barra de status inferior
        areaCentral();      //cria a área onde terão os desenhos
        
        configurarAtalhos();    //configura atalhos de teclado

        gerenciadorDesenho.setCorAtual(Color.WHITE);
    }

    private void barraFerramentasSuperior(){

        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        barra.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        //botão desfazer
        desfazer = new JButton("↩️ Desfazer");
        desfazer.setToolTipText("Desfazer último traço (Ctrl+Z)");
        desfazer.addActionListener(e -> {
            gerenciadorDesenho.acionarDesfazer();
            labelStatus.setText("✅ Desfazer executado (simulação)");
            System.out.println("[SIMULAÇÃO] Desfazer: removendo último traço");
        });
        
        //botão limpar tudo
        limpar = new JButton("🗑️ Limpar Tudo");
        limpar.setToolTipText("Remove todos os traços da tela");
        limpar.addActionListener(e -> {
            gerenciadorDesenho.acionarLimpar();
            labelStatus.setText("✅ Tela limpa (simulação)");
            System.out.println("[SIMULAÇÃO] Limpar tela: removendo todos os traços");
        });
        
        //adicionando espaços entre os botões para que não fiquem muito juntos
        barra.add(desfazer);
        barra.add(Box.createHorizontalStrut(10));
        barra.add(limpar);
        barra.add(Box.createHorizontalStrut(20));
        
        //permite o usuário escolher qual a espessura desejada do
        barra.add(new JLabel("✏️ Espessura: "));
        
        espessuraPincel = new JSlider(JSlider.HORIZONTAL, 1, 20, 3);
        espessuraPincel.setMajorTickSpacing(5);
        espessuraPincel.setMinorTickSpacing(1);
        espessuraPincel.setPaintTicks(true);
        espessuraPincel.setPaintLabels(true);
        espessuraPincel.setPreferredSize(new Dimension(150, 40));
        espessuraPincel.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int espessura = source.getValue();
                gerenciadorDesenho.setEspessuraAtual(espessura);
                labelStatus.setText("✏️ Espessura alterada para: " + espessura + "px");
                System.out.println("[SIMULAÇÃO] Espessura atual: " + espessura);
            }
        });
        
        barra.add(espessuraPincel);
        barra.add(Box.createHorizontalStrut(20));
        
        //permite o usuário selecionar a cor
        barra.add(new JLabel("🎨 Cor: "));
        
        cor = new JButton("Escolher Cor");
        cor.setToolTipText("Clique para escolher a cor do pincel");
        cor.addActionListener(e -> {
            Color novaCor = JColorChooser.showDialog(this, "Escolha a cor do pincel", 
                                                     labelCorPreview.getBackground());
            if (novaCor != null) {
                labelCorPreview.setBackground(novaCor);
                gerenciadorDesenho.setCorAtual(novaCor);
                labelStatus.setText("🎨 Cor alterada para: RGB(" + novaCor.getRed() + 
                                   ", " + novaCor.getGreen() + ", " + novaCor.getBlue() + ")");
                System.out.println("[SIMULAÇÃO] Nova cor selecionada: " + novaCor);
            }
        });
        
        //preview da cor atual
        labelCorPreview = new JLabel("    ");
        labelCorPreview.setOpaque(true);
        labelCorPreview.setBackground(Color.WHITE);
        labelCorPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        labelCorPreview.setPreferredSize(new Dimension(30, 25));
        
        barra.add(cor);
        barra.add(Box.createHorizontalStrut(5));
        barra.add(labelCorPreview);
        
        //espaço flexível no final
        barra.add(Box.createHorizontalGlue());
        
        //botão de ajuda/informação
        JButton informacao = new JButton("ℹ️");
        informacao.setToolTipText("Sobre o Editor");
        informacao.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Editor Gráfico Vetorial\n" +
                "Versão: 1.0\n\n" +
                "Funcionalidades disponíveis:\n" +
                "• Desfazer último traço\n" +
                "• Limpar toda a tela\n" +
                "• Ajustar espessura do pincel\n" +
                "• Escolher cor personalizada\n\n" +
                "Atalhos:\n" +
                "• Ctrl+Z: Desfazer",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE);
        });
        barra.add(informacao);
        
        add(barra, BorderLayout.NORTH);
    }

    private void barraStatusInferior(){

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        labelStatus = new JLabel("✅ Pronto para desenhar");
        labelStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel labelInfo = new JLabel("Editor Vetorial | FlatLaf Dark Mode");
        labelInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelInfo.setForeground(Color.GRAY);
        
        statusPanel.add(labelStatus, BorderLayout.WEST);
        statusPanel.add(labelInfo, BorderLayout.EAST);
        
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void areaCentral(){
        
        add(painelDesenho, BorderLayout.CENTER);
    }

    private void configurarAtalhos() {

        // Ctrl+Z para desfazer
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
        getRootPane().getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desfazer.doClick();
            }
        });
    }
}

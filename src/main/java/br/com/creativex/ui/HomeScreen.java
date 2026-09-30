// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de tela inicial da aplicação ERP-PDVex.
 * 
 * Exibe a página inicial com logo, título e subtítulo do sistema.
 * Implementa um painel visualmente agradável com layout centralizado
 * e estilo compatível com o tema visual geral da aplicação.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public class HomeScreen extends JPanel {

    /**
     * Construtor que inicializa o painel de tela inicial.
     * 
     * Configura o layout, cores, fontes e carrega a logo e textos
     * informativos sobre o sistema.
     */
    public HomeScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Painel central
        JPanel centro = new JPanel();
        centro.setBackground(new Color(245, 245, 245));
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource("/img/LogoPDVex.png"));
        } catch (Exception e) {
            System.out.println("Logo não encontrada!");
        }

        JLabel lblLogo = new JLabel(icon);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // TÍTULO
        JLabel lblTitulo = new JLabel("PDVex-vs1");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(60, 60, 60));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // SUBTÍTULO
        JLabel lblSub = new JLabel("Sistema de Gestão Comercial");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSub.setForeground(new Color(90, 90, 90));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(lblLogo);
        centro.add(Box.createRigidArea(new Dimension(0, 20)));
        centro.add(lblTitulo);
        centro.add(Box.createRigidArea(new Dimension(0, 10)));
        centro.add(lblSub);

        add(centro, BorderLayout.CENTER);
    }
}

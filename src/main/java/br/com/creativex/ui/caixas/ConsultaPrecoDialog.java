// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.ui.caixas;

import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.presentation.controller.ProdutoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

public class ConsultaPrecoDialog extends JDialog {

    private JTextField txtBusca;
    private JLabel lblDescricao, lblPreco, lblEstoque;
    private final ProdutoController controller;
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(java.util.Locale.of("pt", "BR"));

    public ConsultaPrecoDialog(Frame parent, ProdutoController controller) {
        super(parent, "Consulta de Preço (F6)", true);
        this.controller = controller;

        setLayout(new BorderLayout(15, 15));
        setSize(500, 350);
        setLocationRelativeTo(parent);

        initComponents();
    }

    private void initComponents() {
        JPanel pnlInput = new JPanel(new BorderLayout(5, 5));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtBusca = new JTextField();
        txtBusca.setFont(new Font("SansSerif", Font.BOLD, 20));
        pnlInput.add(new JLabel("Bipe o código ou digite o ID:"), BorderLayout.NORTH);
        pnlInput.add(txtBusca, BorderLayout.CENTER);

        JPanel pnlResultado = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlResultado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 15, 15, 15),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY)
        ));
        pnlResultado.setBackground(Color.WHITE);

        lblDescricao = new JLabel("AGUARDANDO PRODUTO...");
        lblDescricao.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblDescricao.setHorizontalAlignment(SwingConstants.CENTER);

        lblPreco = new JLabel("R$ 0,00");
        lblPreco.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblPreco.setForeground(new Color(0, 102, 0));
        lblPreco.setHorizontalAlignment(SwingConstants.CENTER);

        lblEstoque = new JLabel("Estoque: -");
        lblEstoque.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblEstoque.setHorizontalAlignment(SwingConstants.CENTER);

        pnlResultado.add(lblDescricao);
        pnlResultado.add(lblPreco);
        pnlResultado.add(lblEstoque);

        add(pnlInput, BorderLayout.NORTH);
        add(pnlResultado, BorderLayout.CENTER);

        // Eventos
        txtBusca.addActionListener(e -> consultar());

        // Atalho ESC para fechar
        getRootPane().registerKeyboardAction(e -> dispose(), 
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), 
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Atalho F6 também fecha se já estiver aberto (alternância)
        getRootPane().registerKeyboardAction(e -> dispose(), 
                KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), 
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void consultar() {
        String filtro = txtBusca.getText().trim();
        if (filtro.isEmpty()) return;

        try {
            Produto p = controller.buscarPorCodigoOuId(filtro);
            if (p != null) {
                lblDescricao.setText(p.getDescricao().toUpperCase());
                lblPreco.setText(nf.format(p.getPrecoVenda()));
                lblEstoque.setText("Estoque disponível: " + p.getQuantidadeEstoque());
                
                txtBusca.setText("");
                txtBusca.requestFocus();
            } else {
                lblDescricao.setText("PRODUTO NÃO ENCONTRADO");
                lblPreco.setText("R$ 0,00");
                lblEstoque.setText("Estoque: -");
                txtBusca.selectAll();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar: " + e.getMessage());
        }
    }
}

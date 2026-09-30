// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.caixas;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class FinalizarVendaDialog extends JDialog {

    private JComboBox<String> cbPagamento;
    private JTextField txtValorPago;
    private JLabel lblTotal, lblTroco;

    private JButton btnConfirmar, btnCancelar, btnVoltarAoCarrinho;

    private boolean vendaConfirmada = false;
    private String metodoSelecionado;
    private BigDecimal valorPago = BigDecimal.ZERO;
    private BigDecimal troco = BigDecimal.ZERO;

    private final BigDecimal totalVenda;
    private final NumberFormat nf =
            NumberFormat.getCurrencyInstance(java.util.Locale.of("pt", "BR"));

    public FinalizarVendaDialog(Frame parent, BigDecimal totalVenda) {
        super(parent, "Finalizar Venda", true);
        this.totalVenda = totalVenda;

        setLayout(new BorderLayout(15, 15));
        setSize(380, 300);
        setLocationRelativeTo(parent);

        JPanel pnlInfo = new JPanel(new GridLayout(8, 1, 5, 5));
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // TOTAL
        lblTotal = new JLabel(nf.format(totalVenda));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 30));
        lblTotal.setHorizontalAlignment(JLabel.CENTER);
        lblTotal.setForeground(new Color(0, 102, 0));

        // PAGAMENTO
        cbPagamento = new JComboBox<>(
                new String[]{"Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX"}
        );
        cbPagamento.setFont(new Font("Arial", Font.PLAIN, 16));

        // VALOR PAGO
        txtValorPago = new JTextField();
        txtValorPago.setFont(new Font("Arial", Font.BOLD, 18));

        // TROCO
        lblTroco = new JLabel(nf.format(BigDecimal.ZERO));
        lblTroco.setFont(new Font("Arial", Font.BOLD, 20));
        lblTroco.setForeground(Color.BLUE);

        txtValorPago.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                calcularTroco();
            }
        });

        pnlInfo.add(new JLabel("TOTAL A PAGAR:"));
        pnlInfo.add(lblTotal);
        pnlInfo.add(new JLabel("FORMA DE PAGAMENTO:"));
        pnlInfo.add(cbPagamento);
        pnlInfo.add(new JLabel("VALOR PAGO:"));
        pnlInfo.add(txtValorPago);
        pnlInfo.add(new JLabel("TROCO:"));
        pnlInfo.add(lblTroco);

        // BOTÕES
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");
        btnVoltarAoCarrinho = new JButton("Voltar ao Carrinho");

        btnConfirmar.addActionListener(e -> confirmarVenda());
        btnCancelar.addActionListener(e -> dispose());
        btnVoltarAoCarrinho.addActionListener(e -> dispose());

        pnlBotoes.add(btnVoltarAoCarrinho);
        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnConfirmar);

        add(pnlInfo, BorderLayout.CENTER);
        add(pnlBotoes, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnConfirmar);


    }

    // =========================
    // LÓGICA
    // =========================
    private void calcularTroco() {
        try {
            String texto = txtValorPago.getText()
                    .replace("R$", "")
                    .replace(".", "")
                    .replace(",", ".");

            valorPago = new BigDecimal(texto);

            if (valorPago.compareTo(totalVenda) >= 0) {
                troco = valorPago.subtract(totalVenda);
            } else {
                troco = BigDecimal.ZERO;
            }

            lblTroco.setText(nf.format(troco));

        } catch (Exception e) {
            lblTroco.setText(nf.format(BigDecimal.ZERO));
            troco = BigDecimal.ZERO;
        }
    }

    private void confirmarVenda() {
        try {
            String texto = txtValorPago.getText()
                    .replace("R$", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();

            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o valor pago!");
                return;
            }

            valorPago = new BigDecimal(texto);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Valor pago inválido!");
            return;
        }

        if (valorPago.compareTo(totalVenda) < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Valor pago é menor que o total!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        metodoSelecionado = (String) cbPagamento.getSelectedItem();
        vendaConfirmada = true;
        dispose();
    }

    // =========================
    // GETTERS
    // =========================
    public boolean isVendaConfirmada() {
        return vendaConfirmada;
    }

    public String getMetodoSelecionado() {
        return metodoSelecionado;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public BigDecimal getTroco() {
        return troco;
    }
}

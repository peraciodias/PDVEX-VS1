// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.ui.caixas;

import br.com.creativex.config.AppFactory;
import br.com.creativex.domain.entity.venda.ItemVenda;
import br.com.creativex.domain.entity.venda.Venda;
import br.com.creativex.util.Formatador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VendaDetalheDialog extends JDialog {

    private JTable tabelaItens;
    private DefaultTableModel modelItens;

    private JLabel lblId, lblCliente, lblOperador, lblTotal;

    public VendaDetalheDialog(Frame parent, long idVenda) {
        super(parent, "Detalhe da Venda", true);

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        carregarDados(idVenda);
    }

    private void initComponents() {

        // 🔝 TOPO
        JPanel topo = new JPanel(new GridLayout(2, 2, 10, 10));

        lblId = new JLabel();
        lblCliente = new JLabel();
        lblOperador = new JLabel();
        lblTotal = new JLabel();

        topo.add(lblId);
        topo.add(lblCliente);
        topo.add(lblOperador);
        topo.add(lblTotal);

        add(topo, BorderLayout.NORTH);

        // 📦 TABELA DE ITENS
        modelItens = new DefaultTableModel(
                new String[]{"Produto", "Qtd", "V. Unit", "Subtotal"}, 0
        );

        tabelaItens = new JTable(modelItens);
        tabelaItens.setRowHeight(25);

        add(new JScrollPane(tabelaItens), BorderLayout.CENTER);
    }

    private void carregarDados(long idVenda) {

        try {
            Venda venda = AppFactory.caixaController().buscarVendaPorId(idVenda);

            lblId.setText("Venda: " + venda.getIdVenda());
            lblCliente.setText("Cliente: " + venda.getClienteNome());
            lblOperador.setText("Operador: " + venda.getUsuarioNome());
            lblTotal.setText("Total: " + Formatador.moeda(venda.getTotalLiquido()));

            modelItens.setRowCount(0);

            for (ItemVenda item : venda.getItens()) {
                modelItens.addRow(new Object[]{
                        item.getNomeProduto(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        Formatador.moeda(item.getPrecoUnitario()),
                        Formatador.moeda(item.getSubtotal())
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar venda: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
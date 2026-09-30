// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.ui.caixas;

import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.presentation.controller.ProdutoController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;

public class ProdutoPesquisaDialog extends JDialog {

    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel model;
    private Produto produtoSelecionado;
    private final ProdutoController controller;
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(java.util.Locale.of("pt", "BR"));

    public ProdutoPesquisaDialog(Frame parent, ProdutoController controller) {
        super(parent, "Pesquisar Produto (F3)", true);
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setSize(700, 500);
        setLocationRelativeTo(parent);

        initComponents();
    }

    private void initComponents() {
        JPanel pnlTopo = new JPanel(new BorderLayout(5, 5));
        pnlTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtBusca = new JTextField();
        txtBusca.setFont(new Font("SansSerif", Font.PLAIN, 16));
        pnlTopo.add(new JLabel("Pesquise por nome ou código:"), BorderLayout.NORTH);
        pnlTopo.add(txtBusca, BorderLayout.CENTER);

        model = new DefaultTableModel(new String[]{"ID", "Cód. Barras", "Descrição", "Preço Venda", "Estoque"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabela);

        add(pnlTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        txtBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (tabela.getRowCount() > 0) {
                        tabela.setRowSelectionInterval(0, 0);
                        tabela.requestFocus();
                    }
                } else {
                    pesquisar();
                }
            }
        });

        tabela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selecionarEFechar();
                }
            }
        });

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selecionarEFechar();
                }
            }
        });

        // Atalho ESC para fechar
        getRootPane().registerKeyboardAction(e -> dispose(), 
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), 
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void pesquisar() {
        String filtro = txtBusca.getText().trim();
        if (filtro.length() < 2) {
            model.setRowCount(0);
            return;
        }

        List<Produto> produtos = controller.listarPorDescricao(filtro, 50);
        model.setRowCount(0);
        for (Produto p : produtos) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getCodigoBarra(),
                    p.getDescricao(),
                    nf.format(p.getPrecoVenda()),
                    p.getQuantidadeEstoque()
            });
        }
    }

    private void selecionarEFechar() {
        int row = tabela.getSelectedRow();
        if (row != -1) {
            long id = (long) model.getValueAt(row, 0);
            produtoSelecionado = controller.buscarPorId(id);
            dispose();
        }
    }

    public Produto getProdutoSelecionado() {
        return produtoSelecionado;
    }
}

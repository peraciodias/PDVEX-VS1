// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.caixas;
import br.com.creativex.config.AppFactory;
import br.com.creativex.domain.entity.venda.VendaResumo;
import br.com.creativex.presentation.controller.VendasConsultaController;
import br.com.creativex.util.Sessao;
import br.com.creativex.util.Formatador;
import br.com.creativex.ui.HomeScreen;
import br.com.creativex.ui.MainWindow;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class VendasConsultaForm extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private JButton btnCancelar, btnVoltar, btnAtualizar;
    private JTextField txtBusca;
    private JLabel lblResumo;
    private MainWindow mainWindow;
    private final VendasConsultaController controller;
    private TableRowSorter<DefaultTableModel> sorter;

   public VendasConsultaForm() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        this.mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(this);
        this.controller = AppFactory.vendasConsultaController();
        initComponents();
        carregarVendas();
        configurarEventos();
    }
    
    public VendasConsultaForm(MainWindow mainWindow) {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        this.mainWindow = mainWindow;
        this.controller = AppFactory.vendasConsultaController();
        initComponents();
        carregarVendas();
        configurarEventos();
    }

    private void initComponents() {

        model = new DefaultTableModel(
                new String[]{"ID", "Data", "Operador", "Cliente", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Long.class : String.class;
            }
        };

        // 🔥 CRIA A TABELA PRIMEIRO
        tabela = new JTable(model);

        tabela.setRowHeight(28);
        tabela.setAutoCreateRowSorter(true);
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);

        // CABEÇALHO
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(new Color(240, 240, 240));

        // SORTER
        sorter = new TableRowSorter<>(model);
        tabela.setRowSorter(sorter);

        // 🔥 CONFIGURA RENDERIZAÇÃO (AGORA SEM ERRO)
        configurarRenderizacaoTabela();

        // SCROLL
        JScrollPane scrollPane = new JScrollPane(tabela);

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout(8, 8));

        JLabel titulo = new JLabel("Consulta de Vendas");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));

        JLabel subtitulo = new JLabel("Pesquise, revise e cancele vendas com mais segurança.");
        subtitulo.setForeground(new Color(90, 90, 90));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.add(titulo);
        tituloPanel.add(Box.createVerticalStrut(4));
        tituloPanel.add(subtitulo);

        JPanel filtrosPanel = new JPanel(new BorderLayout(8, 8));
        txtBusca = new JTextField();
        txtBusca.setToolTipText("Buscar por ID, operador, cliente ou status");

        btnAtualizar = new JButton("Atualizar");

        filtrosPanel.add(new JLabel("Buscar:"), BorderLayout.WEST);
        filtrosPanel.add(txtBusca, BorderLayout.CENTER);
        filtrosPanel.add(btnAtualizar, BorderLayout.EAST);

        headerPanel.add(tituloPanel, BorderLayout.NORTH);
        headerPanel.add(filtrosPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        lblResumo = new JLabel("Carregando vendas...");

        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        centerPanel.add(lblResumo, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // BOTÕES
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar Venda");
        btnVoltar = new JButton("Voltar");

        btnCancelar.setEnabled(false);

        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnAtualizar);
        pnlBotoes.add(btnVoltar);

        add(pnlBotoes, BorderLayout.SOUTH);
    }
//

//
    private void carregarVendas() {
        setLoading(true);

        SwingWorker<List<VendaResumo>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<VendaResumo> doInBackground() {
                return controller.listarVendas();
            }

            @Override
            protected void done() {
                try {
                    List<VendaResumo> vendas = get();
                    model.setRowCount(0);

                    for (VendaResumo venda : vendas) {
                        model.addRow(new Object[]{
                                venda.getIdVenda(), // ✔ VOLTA ISSO
                                Formatador.dataHora(venda.getDataVenda()), // ✔ só esse
                                venda.getOperador(),
                                venda.getCliente(),
                                Formatador.moeda(venda.getTotalLiquido()), // ✔ correto
                                venda.getStatus()
                        });
                    }

                    atualizarResumo();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(VendasConsultaForm.this,
                            "Erro ao carregar vendas: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    lblResumo.setText("Nao foi possivel carregar as vendas.");
                } finally {
                    setLoading(false);
                }
            }
        };

        worker.execute();
    }

    private void configurarEventos() {
        btnCancelar.addActionListener(e -> cancelarVenda());
        btnAtualizar.addActionListener(e -> carregarVendas());
        btnVoltar.addActionListener(e -> voltarParaHome());
        tabela.getSelectionModel().addListSelectionListener(e -> atualizarEstadoBotoes());
        sorter.addRowSorterListener(e -> atualizarResumo());

        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltro();
            }
        });

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirDetalheVenda();
                }
            }
        });

    }
    
    private void voltarParaHome() {
        if (mainWindow != null) {
            mainWindow.abrirModulo(new HomeScreen());
        }
    }

    private void cancelarVenda() {
        int row = tabela.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma venda para cancelar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tabela.convertRowIndexToModel(row);
        long idVenda = ((Number) model.getValueAt(modelRow, 0)).longValue();
        String status = (String) model.getValueAt(modelRow, 5);
        String cliente = (String) model.getValueAt(modelRow, 3);

        if ("CANCELADA".equals(status)) {
            JOptionPane.showMessageDialog(this,
                    "A venda selecionada ja esta cancelada.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente cancelar a venda " + idVenda
                        + " do cliente " + cliente + "?",
                "Confirmar cancelamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.cancelarVenda(idVenda, Sessao.getUsuarioLogado().getId());

            JOptionPane.showMessageDialog(this,
                    "Venda cancelada com sucesso.",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            carregarVendas();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cancelar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFiltro() {
        String termo = txtBusca.getText().trim();

        if (termo.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(termo)));
        }

        atualizarResumo();
        atualizarEstadoBotoes();
    }
//
private void abrirDetalheVenda() {

    int row = tabela.getSelectedRow();

    if (row == -1) return;

    int modelRow = tabela.convertRowIndexToModel(row);

    long idVenda = ((Number) model.getValueAt(modelRow, 0)).longValue();

    Window parent = SwingUtilities.getWindowAncestor(this);

    VendaDetalheDialog dialog =
            new VendaDetalheDialog((Frame) parent, idVenda);

    dialog.setVisible(true);
}
//
    private void atualizarResumo() {
        int total = model.getRowCount();
        int visiveis = tabela.getRowCount();
        lblResumo.setText("Exibindo " + visiveis + " de " + total + " vendas.");
    }

    private void atualizarEstadoBotoes() {
        boolean possuiSelecao = tabela.getSelectedRow() >= 0;
        btnCancelar.setEnabled(possuiSelecao && tabela.isEnabled());
    }

    private void setLoading(boolean loading) {
        tabela.setEnabled(!loading);
        btnAtualizar.setEnabled(!loading);
        btnVoltar.setEnabled(!loading);
        txtBusca.setEnabled(!loading);
        btnCancelar.setEnabled(false);
        lblResumo.setText(loading ? "Carregando vendas..." : lblResumo.getText());
    }

    private void configurarRenderizacaoTabela() {

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    String status = value != null ? value.toString() : "";
                    if ("CANCELADA".equalsIgnoreCase(status)) {
                        component.setForeground(new Color(170, 35, 35));
                    } else {
                        component.setForeground(new Color(20, 110, 55));
                    }
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return component;
            }
        };

        tabela.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(240);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(110);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(4).setCellRenderer(centralizado);
        tabela.getColumnModel().getColumn(5).setCellRenderer(statusRenderer);
    }
}

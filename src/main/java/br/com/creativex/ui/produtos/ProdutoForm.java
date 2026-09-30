// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.produtos;

import br.com.creativex.ui.MainWindow;
import br.com.creativex.ui.HomeScreen;

import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.presentation.controller.ProdutoController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Painel do módulo Produtos.
 * Versão painel (JPanel) do formulário pronto para ser embutido em MainWindow.
 * Refatorado: métodos menores, validações centralizadas e UI separada.
 */
public class ProdutoForm extends JPanel {

    // Campos de UI
    private JTextField txtId, txtCodigoBarra, txtDescricao, txtMarca, txtAtributos, txtUnidadeMedida,
            txtCategoria, txtCodGrupo, txtGrupo, txtTipoBalanca, txtQuantidadeEstoque,
            txtPrecoCusto, txtPrecoVenda, txtNcm, txtCest, txtCfopPadrao,
            txtUnidadeTributavel, txtCeanTributavel, txtCstIcms, txtAliquotaIcms,
            txtCstPis, txtPpis, txtCstCofins, txtPcofins, txtLoja;

    // Botões e tabela
    private JButton btnSalvar;
    private JButton btnNovo;
    private JButton btnAtualizar;
    private JButton btnCancelar;
    private JButton btnExcluir;

    private JButton btnListar;
    private JButton btnBuscar;
    private JButton btnBuscarCodigoBarra;

    private JButton btnVoltar;

    private JTable table;
    private DefaultTableModel model;

    // DAO
    private final ProdutoController controller;

    public ProdutoForm() {
        // Use centralized factory for wiring
        controller = br.com.creativex.config.AppFactory.produtoController();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        initComponents();
        bindEvents();

        // Carrega último produto ao abrir (se existir)
        produtoFinal();
    }

    // ----------------- Inicialização de componentes -----------------
    private void initComponents() {
        // Abas
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Dados do Produto", criarPainelDadosProduto());
        abas.addTab("Dados Fiscais", criarPainelDadosFiscais());
        add(abas, BorderLayout.NORTH);

        // Tabela
        model = new DefaultTableModel(new String[]{
                "ID", "Código Barra", "Descrição", "Categoria", "Qtd", "Preço Venda (R$)"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // proteger edição direta na tabela
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de botões
        JPanel pnlBotoes = new JPanel();
        pnlBotoes.setBorder(BorderFactory.createTitledBorder("Opções de produto"));

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");

        btnListar = new JButton("Listar por Id");
        btnBuscar = new JButton("Buscar");
        btnBuscarCodigoBarra = new JButton("Buscar por Código de Barras");
        btnVoltar = new JButton("Voltar");
        btnCancelar = new JButton("Cancelar");

        pnlBotoes.add(btnExcluir);
        pnlBotoes.add(btnCancelar);
        pnlBotoes.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        pnlBotoes.add(btnNovo);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnAtualizar);


        pnlBotoes.add(btnListar);
        pnlBotoes.add(btnBuscar);
        pnlBotoes.add(btnBuscarCodigoBarra);
        pnlBotoes.add(btnVoltar);

        add(pnlBotoes, BorderLayout.SOUTH);

        // Estado inicial
        btnSalvar.setVisible(false);
        btnSalvar.setEnabled(false);
        btnAtualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnExcluir.setEnabled(false);


    }

    // ----------------- Criação de painéis -----------------
    private JPanel criarPainelDadosProduto() {
        JPanel p = new JPanel(new GridLayout(13, 2, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Dados Operacionais"));

        txtId = new JTextField();               adicionarCampo(p, "ID:", txtId);
        txtCodigoBarra = new JTextField();      adicionarCampo(p, "Código de Barras:", txtCodigoBarra);
        txtDescricao = new JTextField();        adicionarCampo(p, "Descrição:", txtDescricao);
        txtMarca = new JTextField();            adicionarCampo(p, "Marca:", txtMarca);
        txtAtributos = new JTextField();        adicionarCampo(p, "Atributos:", txtAtributos);
        txtUnidadeMedida = new JTextField();    adicionarCampo(p, "Unidade de Medida:", txtUnidadeMedida);
        txtCategoria = new JTextField();        adicionarCampo(p, "Categoria:", txtCategoria);
        txtCodGrupo = new JTextField();         adicionarCampo(p, "Código do Grupo:", txtCodGrupo);
        txtGrupo = new JTextField();            adicionarCampo(p, "Grupo:", txtGrupo);
        txtTipoBalanca = new JTextField();      adicionarCampo(p, "Tipo Balança (B/C/N):", txtTipoBalanca);
        txtQuantidadeEstoque = new JTextField();adicionarCampo(p, "Quantidade Estoque:", txtQuantidadeEstoque);
        txtPrecoCusto = new JTextField();       adicionarCampo(p, "Preço Custo (R$):", txtPrecoCusto);
        txtPrecoVenda = new JTextField();       adicionarCampo(p, "Preço Venda (R$):", txtPrecoVenda);

        return p;
    }

    private JPanel criarPainelDadosFiscais() {
        JPanel p = new JPanel(new GridLayout(12, 2, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Dados Fiscais e Tributários"));

        txtNcm = new JTextField();              adicionarCampo(p, "NCM:", txtNcm);
        txtCest = new JTextField();             adicionarCampo(p, "CEST:", txtCest);
        txtCfopPadrao = new JTextField();       adicionarCampo(p, "CFOP Padrão:", txtCfopPadrao);
        txtUnidadeTributavel = new JTextField();adicionarCampo(p, "Unidade Tributável:", txtUnidadeTributavel);
        txtCeanTributavel = new JTextField();   adicionarCampo(p, "CEAN Tributável:", txtCeanTributavel);
        txtCstIcms = new JTextField();          adicionarCampo(p, "CST ICMS:", txtCstIcms);
        txtAliquotaIcms = new JTextField();     adicionarCampo(p, "Alíquota ICMS (%):", txtAliquotaIcms);
        txtCstPis = new JTextField();           adicionarCampo(p, "CST PIS:", txtCstPis);
        txtPpis = new JTextField();             adicionarCampo(p, "PIS (%):", txtPpis);
        txtCstCofins = new JTextField();        adicionarCampo(p, "CST COFINS:", txtCstCofins);
        txtPcofins = new JTextField();          adicionarCampo(p, "COFINS (%):", txtPcofins);
        txtLoja = new JTextField();             adicionarCampo(p, "Loja:", txtLoja);

        return p;
    }

    private void adicionarCampo(JPanel p, String label, JTextField campo) {
        p.add(new JLabel(label));
        p.add(campo);
    }

    // ----------------bind  Eventos -----------------
    private void bindEvents() {
        btnNovo.addActionListener(e -> modoNovo());
        btnSalvar.addActionListener(e -> salvarProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnCancelar.addActionListener(e -> cancelarEdicao());
        btnExcluir.addActionListener(e -> excluirProduto());

        btnListar.addActionListener(e -> listarPorId());
        btnBuscar.addActionListener(e -> buscarProduto());
        btnBuscarCodigoBarra.addActionListener(e -> buscarPorCodigoBarra());
        btnVoltar.addActionListener(e -> voltarParaHome());

        // ENTER no campo Código de Barras executa a busca
        txtCodigoBarra.addActionListener(e ->
                executarBuscaCodigoBarra(txtCodigoBarra.getText().trim())
        );

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    carregarProdutoDaTabela(row);
                    modoEdicao();
                }
            }
        });
    }


    // ----------------- Operações de UI -----------------
    private MainWindow getMainWindow() {
        Container parent = getParent();
        while (parent != null) {
            if (parent instanceof MainWindow m) return m;
            parent = parent.getParent();
        }
        return null;
    }

    private void voltarParaHome() {
        MainWindow mw = getMainWindow();
        if (mw != null) mw.abrirModulo(new HomeScreen());
        else JOptionPane.showMessageDialog(this, "Não foi possível voltar. Janela principal não encontrada.");
    }

    private void produtoFinal() {
        try {
            Produto p = controller.buscarUltimo();
            if (p != null) {
                preencherCampos(p);
                // opcional: exibir na tabela
                model.setRowCount(0);
                model.addRow(new Object[]{
                        p.getId(),
                        p.getCodigoBarra(),
                        p.getDescricao(),
                        p.getCategoria(),
                        p.getQuantidadeEstoque(),
                        p.getPrecoVenda()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao ler último registro: " + ex.getMessage());
        }
    }

    // ----------------- Modos/UX -----------------
    private void modoNovo() {
        limparCampos();
        txtId.setText("");

        btnSalvar.setVisible(true);
        btnSalvar.setEnabled(true);
        btnAtualizar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnExcluir.setEnabled(false);

        // Para evitar update acidental na tabela
        table.clearSelection();

        txtCodigoBarra.requestFocus();
    }

//===============================================
    private void modoEdicao() {
        btnSalvar.setVisible(false);
        btnSalvar.setEnabled(false);
        btnAtualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnExcluir.setEnabled(true);


    }

    private void cancelarEdicao() {

        int opc = JOptionPane.showConfirmDialog(
                this,
                "Cancelar a operação atual?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (opc != JOptionPane.YES_OPTION) return;

        limparCampos();
        produtoFinal();

        btnSalvar.setVisible(false);
        btnSalvar.setEnabled(false);
        btnAtualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnExcluir.setEnabled(false);

    }

    // ----------------- Ações dos botões -----------------
    private void salvarProduto() {

        // não permitir salvar sem estar em modo novo
        if (!btnSalvar.isVisible() || !isEmpty(txtId.getText()) ) {
            JOptionPane.showMessageDialog(this,
                    "Para salvar um novo produto, clique no botão NOVO.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validação centralizada
        if (!validarCamposObrigatorios()) return;

        try {
            Produto p = criarProdutoDeCampos();

            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "Confirmar cadastro do novo produto?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc != JOptionPane.YES_OPTION) return;

            controller.inserir(p);

            JOptionPane.showMessageDialog(this,
                    "Produto cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            // Atualiza tela e tabela com o último registro gravado
            produtoFinal();
            adicionarLinhaTabela(p);

            // Sai do modo novo
            btnSalvar.setVisible(false);
            btnSalvar.setEnabled(false);
            btnAtualizar.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
//========================================================
private void atualizarProduto() {
    if (isEmpty(txtId.getText())) {
        JOptionPane.showMessageDialog(this, "Nenhum produto carregado para atualizar.");
        return;
    }

        try {
        Produto p = criarProdutoDeCampos();
        p.setId(Long.parseLong(txtId.getText()));

        int opc = JOptionPane.showConfirmDialog(
                this,
                "Confirmar atualização deste produto?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (opc != JOptionPane.YES_OPTION) return;

        controller.atualizar(p);

        JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");

        atualizarLinhaTabela(p);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage());
    }
}
//=================================================
private void excluirProduto() {

    if (isEmpty(txtId.getText())) {
        JOptionPane.showMessageDialog(this,
                "Selecione um produto para excluir.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int opc = JOptionPane.showConfirmDialog(
            this,
            "Confirma a exclusão deste produto?",
            "Excluir Produto",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    );

    if (opc != JOptionPane.YES_OPTION) return;

    try {
    long id = Long.parseLong(txtId.getText());
    controller.excluir(id);

        JOptionPane.showMessageDialog(this,
                "Produto excluído com sucesso.",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );

        limparCampos();
        model.setRowCount(0);
        produtoFinal();

        btnAtualizar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnCancelar.setEnabled(false);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Erro ao excluir: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

//=================================================

    private void listarPorId() {
        String entrada = JOptionPane.showInputDialog(this,
                "Digite o ID inicial para listar (máx. 10 registros):");

        if (entrada == null || entrada.isBlank()) return;

        try {
            long idInicial = Long.parseLong(entrada);
            carregarTabelaPorId(idInicial, 10);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar: " + e.getMessage());
        }
    }
//=========================================================
private void buscarPorCodigoBarra() {

    JTextField campo = new JTextField(20);
    campo.requestFocusInWindow();

    int opc = JOptionPane.showConfirmDialog(
            this,
            campo,
            "Informe ou leia o Código de Barras",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
    );

    if (opc != JOptionPane.OK_OPTION) return;

    String codigoBarra = campo.getText().trim();
    if (codigoBarra.isEmpty()) return;

    executarBuscaCodigoBarra(codigoBarra);
}

//==cria janela para leitor de barras ou digitação ===
    private void executarBuscaCodigoBarra(String codigoBarra) {

        try {
            Produto produto = controller.buscarPorCodigoBarra(codigoBarra);

            if (produto == null) {
                JOptionPane.showMessageDialog(this,
                        "Produto não encontrado.",
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            preencherCampos(produto);

            model.setRowCount(0);
            model.addRow(new Object[]{
                    produto.getId(),
                    produto.getCodigoBarra(),
                    produto.getDescricao(),
                    produto.getCategoria(),
                    produto.getQuantidadeEstoque(),
                    produto.getPrecoVenda()
            });

            modoEdicao();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar por código de barras: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

//=========================================================

    private void buscarProduto() {
        String entrada = JOptionPane.showInputDialog(this, "Digite o ID ou descrição:");
        if (entrada == null || entrada.isBlank()) return;

        try {
            Produto p = entrada.matches("\\d+") ? controller.buscarPorId(Long.parseLong(entrada)) : controller.buscarPorNome(entrada);
            if (p != null) {
                preencherCampos(p);
                JOptionPane.showMessageDialog(this, "Produto encontrado!");
                // Atualiza seleção na tabela (opcional)
                model.setRowCount(0);
                model.addRow(new Object[]{
                        p.getId(),
                        p.getCodigoBarra(),
                        p.getDescricao(),
                        p.getCategoria(),
                        p.getQuantidadeEstoque(),
                        p.getPrecoVenda()
                });
                modoEdicao();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage());
        }
    }

    // ----------------- Utilitários para tabela -----------------
    private void carregarTabelaPorId(long idInicial, int limite) {
        try {
            List<Produto> produtos = controller.listarPorIdLimite(idInicial, limite);
            model.setRowCount(0);
            for (Produto p : produtos) {
                model.addRow(new Object[]{
                        p.getId(),
                        p.getCodigoBarra(),
                        p.getDescricao(),
                        p.getCategoria(),
                        p.getQuantidadeEstoque(),
                        p.getPrecoVenda()
                });
            }
            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum produto encontrado para esse ID.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tabela: " + e.getMessage());
        }
    }

    // adiciona nova linha na tabela com os dados recentes (usa buscarUltimo para pegar id)
    private void adicionarLinhaTabela(Produto p) {
        try {
            Produto salvo = controller.buscarUltimo();
            if (salvo != null) {
                model.addRow(new Object[]{
                        salvo.getId(),
                        salvo.getCodigoBarra(),
                        salvo.getDescricao(),
                        salvo.getCategoria(),
                        salvo.getQuantidadeEstoque(),
                        salvo.getPrecoVenda()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // atualiza linha selecionada na tabela (caso queira refletir edição imediata)
    private void atualizarLinhaTabela(Produto p) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            model.setValueAt(p.getId(), row, 0);
            model.setValueAt(p.getCodigoBarra(), row, 1);
            model.setValueAt(p.getDescricao(), row, 2);
            model.setValueAt(p.getCategoria(), row, 3);
            model.setValueAt(p.getQuantidadeEstoque(), row, 4);
            model.setValueAt(p.getPrecoVenda(), row, 5);
        } else {
            // se nenhuma linha selecionada, atualizar a tabela inteira pode ser feito aqui
        }
    }

    // carrega produto da linha selecionada para os campos do form
    private void carregarProdutoDaTabela(int row) {
        try {
            long id = Long.parseLong(model.getValueAt(row, 0).toString());
            Produto p = controller.buscarPorId(id);
            if (p != null) {
                preencherCampos(p);
            }
            if (isEmpty(txtDescricao.getText()) && isEmpty(txtPrecoVenda.getText())) {
                JOptionPane.showMessageDialog(this,
                        "Produto não pode ser salvo com informações vazias.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produto: " + ex.getMessage());
        }
    }

    // ----------------- Criação / preenchimento do model -----------------
    private Produto criarProdutoDeCampos() {
        Produto p = new Produto();
        p.setCodigoBarra(txtCodigoBarra.getText());
        p.setDescricao(txtDescricao.getText());
        p.setMarca(txtMarca.getText());
        p.setAtributos(txtAtributos.getText());
        p.setUnidadeMedida(txtUnidadeMedida.getText());
        p.setCategoria(txtCategoria.getText());
        p.setCodGrupo(parseInt(txtCodGrupo.getText()));
        p.setGrupo(txtGrupo.getText());
        p.setTipoBalanca(getChar(txtTipoBalanca.getText()));
        p.setQuantidadeEstoque(parseBig(txtQuantidadeEstoque.getText()));
        p.setPrecoCusto(parseBig(txtPrecoCusto.getText()));
        p.setPrecoVenda(parseBig(txtPrecoVenda.getText()));
        p.setNcm(txtNcm.getText());
        p.setCest(txtCest.getText());
        p.setCfopPadrao(txtCfopPadrao.getText());
        p.setUnidadeTributavel(txtUnidadeTributavel.getText());
        p.setCeanTributavel(txtCeanTributavel.getText());
        p.setCstIcms(txtCstIcms.getText());
        p.setAliquotaIcms(parseBig(txtAliquotaIcms.getText()));
        p.setCstPis(txtCstPis.getText());
        p.setPpis(parseBig(txtPpis.getText()));
        p.setCstCofins(txtCstCofins.getText());
        p.setPcofins(parseBig(txtPcofins.getText()));
        p.setLoja(txtLoja.getText());
        return p;
    }

    private void preencherCampos(Produto p) {
        txtId.setText(String.valueOf(p.getId()));
        txtCodigoBarra.setText(p.getCodigoBarra());
        txtDescricao.setText(p.getDescricao());
        txtMarca.setText(p.getMarca());
        txtAtributos.setText(p.getAtributos());
        txtUnidadeMedida.setText(p.getUnidadeMedida());
        txtCategoria.setText(p.getCategoria());
        txtCodGrupo.setText(String.valueOf(p.getCodGrupo()));
        txtGrupo.setText(p.getGrupo());
        txtTipoBalanca.setText(String.valueOf(p.getTipoBalanca()));
        txtQuantidadeEstoque.setText(String.valueOf(p.getQuantidadeEstoque()));
        txtPrecoCusto.setText(String.valueOf(p.getPrecoCusto()));
        txtPrecoVenda.setText(String.valueOf(p.getPrecoVenda()));
        txtNcm.setText(p.getNcm());
        txtCest.setText(p.getCest());
        txtCfopPadrao.setText(p.getCfopPadrao());
        txtUnidadeTributavel.setText(p.getUnidadeTributavel());
        txtCeanTributavel.setText(p.getCeanTributavel());
        txtCstIcms.setText(p.getCstIcms());
        txtAliquotaIcms.setText(String.valueOf(p.getAliquotaIcms()));
        txtCstPis.setText(p.getCstPis());
        txtPpis.setText(String.valueOf(p.getPpis()));
        txtCstCofins.setText(p.getCstCofins());
        txtPcofins.setText(String.valueOf(p.getPcofins()));
        txtLoja.setText(p.getLoja());
    }

    // ----------------- Utilitários -----------------
    private void limparCampos() {
        for (Component c : this.getComponents()) limparComponenteRecursivo(c);
    }

    private void limparComponenteRecursivo(Component c) {
        if (c instanceof JTextField) ((JTextField) c).setText("");
        else if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) limparComponenteRecursivo(child);
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private BigDecimal toBig(String value) {
        if (value == null) return BigDecimal.ZERO;
        try {
            return new BigDecimal(value.replace(",", ".").trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal parseBig(String s) {
        try {
            return new BigDecimal(s.trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private char getChar(String s) {
        return (s != null && !s.isBlank()) ? s.trim().charAt(0) : 'N';
    }

    // Validação centralizada dos campos obrigatórios
    private boolean validarCamposObrigatorios() {

        if (isEmpty(txtDescricao.getText())) {
            JOptionPane.showMessageDialog(this,
                    "O campo DESCRIÇÃO é obrigatório.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            txtDescricao.requestFocus();
            return false;
        }

        if (isEmpty(txtCategoria.getText())) {
            JOptionPane.showMessageDialog(this,
                    "O campo CATEGORIA é obrigatório.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            txtCategoria.requestFocus();
            return false;
        }

        BigDecimal preco = toBig(txtPrecoVenda.getText());
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "O PREÇO DE VENDA deve ser maior que zero.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            txtPrecoVenda.requestFocus();
            return false;
        }

        return true;
    }
}

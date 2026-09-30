// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.ui.caixas;

import br.com.creativex.domain.entity.usuario.Usuario;
import br.com.creativex.domain.entity.venda.ItemVenda;
import br.com.creativex.domain.entity.venda.Venda;
import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.domain.entity.config.Estabelecimento;
import br.com.creativex.domain.entity.config.ProdutoBipagem;
import br.com.creativex.infrastructure.persistence.repository.estabelecimento.EstabelecimentoDAO;
import br.com.creativex.config.AppFactory;
import br.com.creativex.presentation.controller.CaixaController;
import br.com.creativex.presentation.controller.ProdutoController;
import br.com.creativex.application.config.ConsultarTributoProdutoUseCase;
import br.com.creativex.ui.HomeScreen;
import br.com.creativex.ui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class CaixasForm extends JPanel {
//-> 25/04
    private JPanel painelItemAtual;
    private JLabel lblItemAtual;
//<-
    private JTextField txtCodigoBarras, txtQuantidade, txtTotalVenda;
    private JTextArea areaCupom;
    private JButton btnFinalizar, btnRemoverItem, btnVoltar;

    private Usuario usuario;
    private MainWindow mainWindow;

    private final CaixaController caixaController;
    private final ProdutoController produtoController;
    private final ConsultarTributoProdutoUseCase consultarTributoUseCase;

    private Venda vendaAtual = new Venda();
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(java.util.Locale.of("pt", "BR"));

    public CaixasForm(Usuario usuario, MainWindow mainWindow) {
        this.usuario = usuario;
        this.mainWindow = mainWindow;

        this.caixaController = AppFactory.caixaController();
        this.produtoController = AppFactory.produtoController();
        this.consultarTributoUseCase = AppFactory.consultarTributoProdutoUseCase();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        configurarAtalhos();
    }

    private void initComponents() {
        // CAMPOS DE ENTRADA
        txtCodigoBarras = new JTextField();
        txtCodigoBarras.setFont(new Font("SansSerif", Font.BOLD, 18));

        txtQuantidade = new JTextField("1");
        txtQuantidade.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel pnlTopo = new JPanel(new BorderLayout());
      //->
        painelItemAtual = new JPanel(new BorderLayout());
        painelItemAtual.setBackground(Color.BLACK);
        painelItemAtual.setPreferredSize(new Dimension(100, 60));

        lblItemAtual = new JLabel("AGUARDANDO PRODUTO...");
        lblItemAtual.setForeground(Color.GREEN);
        lblItemAtual.setFont(new Font("Monospaced", Font.BOLD, 26));
        lblItemAtual.setHorizontalAlignment(SwingConstants.CENTER);
        painelItemAtual.add(lblItemAtual, BorderLayout.CENTER);
      //<-

        JPanel entrada = new JPanel(new GridLayout(1, 4, 10, 10));
        entrada.add(new JLabel("CÓDIGO DE BARRAS / ID:"));
        entrada.add(txtCodigoBarras);
        entrada.add(new JLabel("QUANTIDADE:"));
        entrada.add(txtQuantidade);

        JLabel lblOperador = new JLabel("Operador: " + usuario.getNome());
        lblOperador.setFont(new Font("SansSerif", Font.BOLD, 14));

        pnlTopo.add(entrada, BorderLayout.CENTER);
        pnlTopo.add(lblOperador, BorderLayout.EAST);

        // ÁREA DE CUPOM
        areaCupom = new JTextArea();
        areaCupom.setEditable(false);
        areaCupom.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaCupom.setBackground(Color.WHITE);

        JScrollPane scrollCupom = new JScrollPane(areaCupom);

        // PAINEL DE AÇÕES
        JPanel pnlAcoes = new JPanel();
        pnlAcoes.setLayout(new BoxLayout(pnlAcoes, BoxLayout.Y_AXIS));
        pnlAcoes.setPreferredSize(new Dimension(250, 0));

        txtTotalVenda = new JTextField("R$ 0,00");
        txtTotalVenda.setEditable(false);
        txtTotalVenda.setBackground(Color.BLACK);
        txtTotalVenda.setForeground(Color.GREEN);
        txtTotalVenda.setFont(new Font("Monospaced", Font.BOLD, 35));
        txtTotalVenda.setHorizontalAlignment(JTextField.CENTER);

        pnlAcoes.add(new JLabel("TOTAL DA VENDA:"));
        pnlAcoes.add(txtTotalVenda);
        pnlAcoes.add(Box.createVerticalGlue()); // Empurra o total para o topo

       // add(pnlTopo, BorderLayout.NORTH);
       //->
        JPanel topoCompleto = new JPanel();
        topoCompleto.setLayout(new BoxLayout(topoCompleto, BoxLayout.Y_AXIS));

        topoCompleto.add(pnlTopo);
        topoCompleto.add(Box.createVerticalStrut(5));
        topoCompleto.add(painelItemAtual);

        add(topoCompleto, BorderLayout.NORTH);
       //<-
        add(scrollCupom, BorderLayout.CENTER);
        add(pnlAcoes, BorderLayout.EAST);
        add(criarPainelOperacao(), BorderLayout.SOUTH);

        configurarEventos();
    }

    private JPanel criarPainelOperacao() {
        JPanel pnlPrincipal = new JPanel(new BorderLayout());
        
        JLabel lblMenu = new JLabel(" MENU CAIXA ");
        lblMenu.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblMenu.setForeground(Color.DARK_GRAY);
        pnlPrincipal.add(lblMenu, BorderLayout.NORTH);

        JPanel pnlAtalhos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pnlAtalhos.setBackground(new Color(235, 235, 235));
        pnlAtalhos.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        pnlAtalhos.add(criarBotaoAtalho("F2", "Cod. Manual", e -> abrirCodigoManualDialog()));
        pnlAtalhos.add(criarBotaoAtalho("F3", "Pesquisar", e -> abrirPesquisaProduto()));
        pnlAtalhos.add(criarBotaoAtalho("F4", "Qtd", e -> {
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
        }));
        pnlAtalhos.add(criarBotaoAtalho("F6", "Preço", e -> abrirConsultaPreco()));
        pnlAtalhos.add(criarBotaoAtalho("F7", "CPF/CNPJ", e -> identificarCliente()));
        pnlAtalhos.add(criarBotaoAtalho("DEL", "Remover", e -> removerItemPorSeqDialog()));
        pnlAtalhos.add(criarBotaoAtalho("F12", "Finalizar", e -> finalizarVenda()));
        pnlAtalhos.add(criarBotaoAtalho("ESC", "Sair", e -> voltarParaHome()));

        pnlPrincipal.add(pnlAtalhos, BorderLayout.CENTER);
        return pnlPrincipal;
    }

    private JButton criarBotaoAtalho(String tecla, String acao, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(tecla + " - " + acao);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setMargin(new Insets(5, 10, 5, 10));
        btn.setFocusable(false);
        btn.addActionListener(listener);
        return btn;
    }

    private void configurarEventos() {
        txtCodigoBarras.addActionListener(e -> adicionarProdutoPeloCodigo());
    }

    private void configurarAtalhos() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "finalizar");

        this.getActionMap().put("finalizar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                finalizarVenda();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "remover");

        this.getActionMap().put("remover", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                removerItemPorSeqDialog();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "codigoManual");

        this.getActionMap().put("codigoManual", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                abrirCodigoManualDialog();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "pesquisar");

        this.getActionMap().put("pesquisar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                abrirPesquisaProduto();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "consulta_preco");

        this.getActionMap().put("consulta_preco", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                abrirConsultaPreco();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "cpf_cnpj");

        this.getActionMap().put("cpf_cnpj", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                identificarCliente();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "quantidade");

        this.getActionMap().put("quantidade", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                txtQuantidade.requestFocus();
                txtQuantidade.selectAll();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "voltar");

        this.getActionMap().put("voltar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                voltarParaHome();
            }
        });
    }

    private void voltarParaHome() {
        if (!vendaAtual.getItens().isEmpty()) {
            int opc = JOptionPane.showConfirmDialog(this, 
                    "Existe uma venda em curso. Deseja realmente sair?", 
                    "Atenção", JOptionPane.YES_NO_OPTION);
            if (opc != JOptionPane.YES_OPTION) return;
        }
        if (mainWindow != null) {
            mainWindow.abrirModulo(new HomeScreen());
        }
    }


    private void adicionarProdutoPeloCodigo() {
        String filtro = txtCodigoBarras.getText().trim();
        adicionarProdutoPorFiltro(filtro);
    }

    private void abrirCodigoManualDialog() {
        String input = JOptionPane.showInputDialog(this,
                "Digite o código de barras ou ID do produto:",
                "Entrada manual (F2)",
                JOptionPane.PLAIN_MESSAGE);

        if (input == null || input.trim().isEmpty()) {
            txtCodigoBarras.requestFocus();
            return;
        }

        txtCodigoBarras.setText(input.trim());
        adicionarProdutoPorFiltro(input.trim());
    }

    private void abrirPesquisaProduto() {
        Window window = SwingUtilities.getWindowAncestor(this);
        Frame parent = (window instanceof Frame) ? (Frame) window : null;

        ProdutoPesquisaDialog dialog = new ProdutoPesquisaDialog(parent, produtoController);
        dialog.setVisible(true);

        Produto p = dialog.getProdutoSelecionado();
        if (p != null) {
            processarAdicaoProduto(p);
        }
        txtCodigoBarras.requestFocus();
    }

    private void abrirConsultaPreco() {
        Window window = SwingUtilities.getWindowAncestor(this);
        Frame parent = (window instanceof Frame) ? (Frame) window : null;

        ConsultaPrecoDialog dialog = new ConsultaPrecoDialog(parent, produtoController);
        dialog.setVisible(true);

        txtCodigoBarras.requestFocus();
    }

    private void identificarCliente() {        String input = JOptionPane.showInputDialog(this,
                "Informe o CPF ou CNPJ do Cliente:",
                "Identificar Cliente (F7)",
                JOptionPane.PLAIN_MESSAGE);

        if (input == null) return; // Cancelou

        String documento = input.trim().replaceAll("[^0-9]", "");
        if (documento.isEmpty()) {
            vendaAtual.setCpfAvulso(null);
            vendaAtual.setNomeClienteAvulso(null);
        } else {
            vendaAtual.setCpfAvulso(documento);
            // Por enquanto tratamos como avulso
            vendaAtual.setNomeClienteAvulso("CLIENTE IDENTIFICADO");
        }

        atualizarCupom();
        txtCodigoBarras.requestFocus();
    }

    private void adicionarProdutoPorFiltro(String filtro) {
        if (filtro.isEmpty()) return;

        try {
            Produto p = produtoController.buscarPorCodigoOuId(filtro);

            if (p != null) {
                processarAdicaoProduto(p);
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado!");
                txtCodigoBarras.requestFocus();
                txtCodigoBarras.selectAll();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void processarAdicaoProduto(Produto p) {
        BigDecimal qtd;

        try {
            qtd = new BigDecimal(txtQuantidade.getText().replace(",", "."));
            if (qtd.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero!");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
            return;
        }

        try {
            if (p.getQuantidadeEstoque().compareTo(qtd) < 0) {
                int opc = JOptionPane.showConfirmDialog(this,
                        "Estoque insuficiente!\nDisponível: " + p.getQuantidadeEstoque() + 
                        "\nDeseja continuar mesmo assim?", "Aviso", JOptionPane.YES_NO_OPTION);
                if (opc != JOptionPane.YES_OPTION) return;
            }

            // --- BUSCAR DADOS TRIBUTÁRIOS DA VIEW vw_pdv_bipagem ---
            ProdutoBipagem produtoComTributo = consultarTributoUseCase.buscarPorCodigoBarra(p.getCodigoBarra());

            // --- LÓGICA DE TRATAMENTO DE TEXTO ---
            String descFinal = p.getDescricao().replaceAll("_x[0-9A-Fa-f]{4}_", "").trim().toUpperCase();

            // Usamos os textos limpos para criar o item da venda
            ItemVenda item = new ItemVenda(
                    p.getId(),
                    descFinal,
                    qtd,
                    p.getPrecoVenda()
            );
            item.setCodigoBarra(p.getCodigoBarra());

            // --- DEFINIR VALORES TRIBUTÁRIOS DO BANCO ---
            item.setPrecoCustoMomento(p.getPrecoCusto());
            item.setCstFiscalMomento(p.getCstIcms());

            if (produtoComTributo != null) {
                item.setAliquotaIcms(produtoComTributo.getAliquotaAplicada());
            } else {
                item.setAliquotaIcms(p.getAliquotaIcms());
            }

            item.setCstPis(p.getCstPis());
            item.setPpis(p.getPpis());
            item.setCstCofins(p.getCstCofins());
            item.setPcofins(p.getPcofins());

            item.calcularTributos();

            vendaAtual.adicionarItem(item);

            atualizarItemAtual(p, produtoComTributo);
            atualizarCupom();
            atualizarExibicaoTotal();
            limparCamposInput();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao processar item: " + ex.getMessage());
        }
    }

    //->
private void atualizarItemAtual(Produto p, ProdutoBipagem produtoComTributo) {
    Toolkit.getDefaultToolkit().beep();
    // Use toUpperCase() e remova o "R$" extra se o nf.format já trouxer
    StringBuilder texto = new StringBuilder();
    texto.append(p.getDescricao().toUpperCase())
         .append("  |  QTD: ").append(txtQuantidade.getText())
         .append("  |  ").append(nf.format(p.getPrecoVenda()));

    // Adicionar informação tributária se disponível
    if (produtoComTributo != null) {
        texto.append("  |  ALÍQ: ").append(produtoComTributo.getAliquotaAplicada()).append("%");
    }

    lblItemAtual.setText(texto.toString());

    lblItemAtual.setForeground(Color.YELLOW);
    new javax.swing.Timer(150, e -> {
        lblItemAtual.setForeground(Color.GREEN);
    }).start();
}
//<-
    private void finalizarVenda() {
        if (vendaAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carrinho vazio!");
            return;
        }

        vendaAtual.recalcularTotais();

        Window window = SwingUtilities.getWindowAncestor(this);
        Frame parent = (window instanceof Frame) ? (Frame) window : null;
        FinalizarVendaDialog dialog = new FinalizarVendaDialog(parent, vendaAtual.getTotalLiquido());
        dialog.setVisible(true);

        if (!dialog.isVendaConfirmada()) {
            return;
        }

        try {
            if (dialog.getMetodoSelecionado() == null) {
                JOptionPane.showMessageDialog(this, "Selecione a forma de pagamento.");
                return;
            }

            BigDecimal valorPago = dialog.getValorPago();
            if (valorPago == null) {
                valorPago = BigDecimal.ZERO;
            }

            vendaAtual.setIdUsuario(usuario.getId());
            vendaAtual.setMetodoPagamento(dialog.getMetodoSelecionado());
            vendaAtual.setValorPago(valorPago);
            vendaAtual.setTroco(dialog.getTroco());

            if (valorPago.compareTo(vendaAtual.getTotalLiquido()) < 0) {
                JOptionPane.showMessageDialog(this,
                        "Valor pago é menor que o total da venda!");
                return;
            }

            caixaController.finalizarVenda(vendaAtual);

            JOptionPane.showMessageDialog(this, "Venda concluída!");
            limparVenda();

        } catch (Exception ex) {
            String mensagem = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
            JOptionPane.showMessageDialog(this, "Erro: " + mensagem);
        }
    }

    private void removerItemPorSeqDialog() {
        if (vendaAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum item no cupom!");
            return;
        }

        String input = JOptionPane.showInputDialog(this,
                "Digite o número SEQ do item a remover:");

        if (input == null || input.trim().isEmpty()) return;

        try {
            int seq = Integer.parseInt(input.trim());
            removerItemPorSeq(seq);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número inválido!");
        }
    }

    private void removerItemPorSeq(int seq) {
        if (seq <= 0 || seq > vendaAtual.getItens().size()) {
            JOptionPane.showMessageDialog(this, "SEQ inexistente!");
            return;
        }

        vendaAtual.getItens().remove(seq - 1);

        vendaAtual.recalcularTotais();
        atualizarCupom();
        atualizarExibicaoTotal();
    }

    private void atualizarCupom() {
    StringBuilder sb = new StringBuilder();

    sb.append(gerarCabecalhoEstabelecimento());
    sb.append("-------------------------------------------------------------------\n");
    
    if (vendaAtual.getCpfAvulso() != null && !vendaAtual.getCpfAvulso().isEmpty()) {
        sb.append("CONSUMIDOR: ").append(vendaAtual.getCpfAvulso()).append("\n");
        sb.append("-------------------------------------------------------------------\n");
    }

    sb.append(String.format("%-3s %-13s %-15s %-5s %-10s %-6s %-10s\n",
            "SEQ", "CÓD.BARRAS", "DESC", "QTD", "VALOR", "ALÍQ", "IMPOSTO"));
    sb.append("-------------------------------------------------------------------\n");

    int seq = 1;
    BigDecimal totalIcms = BigDecimal.ZERO;
    BigDecimal totalPis = BigDecimal.ZERO;
    BigDecimal totalCofins = BigDecimal.ZERO;

    for (ItemVenda item : vendaAtual.getItens()) {
        String codigoBarras = item.getCodigoBarra() != null && !item.getCodigoBarra().isBlank()
                ? item.getCodigoBarra()
                : String.valueOf(item.getIdProduto());
        String descricaoCurta = item.getNomeProduto().length() > 15 ?
                item.getNomeProduto().substring(0, 12) + "..." :
                item.getNomeProduto();

        sb.append(String.format("%03d %-13s %-15s %-5s %-10s %-6s %-10s\n",
            seq++,
            codigoBarras,
            descricaoCurta,
            item.getQuantidade(),
            nf.format(item.getSubtotal()),
            nf.format(item.getAliquotaIcms()) + "%",
            nf.format(item.getValorIcms())));

        totalIcms = totalIcms.add(item.getValorIcms());
        totalPis = totalPis.add(item.getValorPis());
        totalCofins = totalCofins.add(item.getValorCofins());
    }

    BigDecimal tributos = vendaAtual.getTotalTributos();

    sb.append("-------------------------------------------------------------------\n");
    sb.append(String.format("TOTAL LIQUIDO: %s\n", nf.format(vendaAtual.getTotalLiquido())));
    sb.append(String.format("ICMS:          %s\n", nf.format(totalIcms)));
    sb.append(String.format("PIS:           %s\n", nf.format(totalPis)));
    sb.append(String.format("COFINS:        %s\n", nf.format(totalCofins)));
    sb.append(String.format("TOTAL TRIBUTOS: %s\n", nf.format(tributos)));
    sb.append(String.format("VALOR PAGO:    %s\n", nf.format(vendaAtual.getValorPago() != null ? vendaAtual.getValorPago() : BigDecimal.ZERO)));
    sb.append(String.format("TROCO:         %s\n", nf.format(vendaAtual.getTroco() != null ? vendaAtual.getTroco() : BigDecimal.ZERO)));
    sb.append("-------------------------------------------------------------------\n");
    sb.append("       OBRIGADO PELA PREFERENCIA!       \n");

    areaCupom.setText(sb.toString());
}

    private String gerarCabecalhoEstabelecimento() {
        try {
            Estabelecimento estabelecimento = new EstabelecimentoDAO().carregarDados();
            if (estabelecimento != null) {
                String nome = estabelecimento.getRazaoSocial() != null ? estabelecimento.getRazaoSocial() : "NOME DA SUA LOJA";
                String cnpj = estabelecimento.getCnpj() != null ? estabelecimento.getCnpj() : "00.000.000/0001-00";
                String ie = estabelecimento.getInscricaoEstadual() != null ? estabelecimento.getInscricaoEstadual() : "123456789";
                return "              " + nome + "\n" +
                        "      CNPJ: " + cnpj + "  IE: " + ie + "\n";
            }
        } catch (Exception ignored) {
            // Se não houver tabela ou não for possível ler, usa valores padrão.
        }
        return "              NOME DA SUA LOJA\n" +
                "      CNPJ: 00.000.000/0001-00  IE: 123456789\n";
    }
    private void atualizarExibicaoTotal() {
        vendaAtual.recalcularTotais();
        txtTotalVenda.setText(nf.format(vendaAtual.getTotalLiquido()));
    }

    private void limparCamposInput() {
        txtCodigoBarras.setText("");
        txtQuantidade.setText("1");
        txtCodigoBarras.requestFocus();
    }

    private void limparVenda() {
        vendaAtual = new Venda();
        areaCupom.setText("");
        txtTotalVenda.setText("R$ 0,00");
        limparCamposInput();
        //->
        lblItemAtual.setText("AGUARDANDO PRODUTO...");
        //<-
    }
//----
private String center(String texto, int largura) {
    if (texto.length() >= largura) return texto;
    int espacos = (largura - texto.length()) / 2;
    return " ".repeat(espacos) + texto;
}

private String alignRight(String texto, int largura) {
    if (texto.length() >= largura) return texto;
    return " ".repeat(largura - texto.length()) + texto;
}

private String[] quebrarTexto(String texto, int tamanho) {
    int linhas = (int) Math.ceil((double) texto.length() / tamanho);
    String[] resultado = new String[linhas];

    for (int i = 0; i < linhas; i++) {
        int inicio = i * tamanho;
        int fim = Math.min(inicio + tamanho, texto.length());
        resultado[i] = texto.substring(inicio, fim);
    }

    return resultado;
}
//---------
}

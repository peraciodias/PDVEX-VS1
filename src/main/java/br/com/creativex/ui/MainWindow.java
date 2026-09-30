// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui;
import br.com.creativex.ui.cadastrousuario.CadastroUsuarioForm;
import br.com.creativex.ui.caixas.VendasConsultaForm;
import br.com.creativex.ui.produtos.ProdutoForm;
import br.com.creativex.ui.estoque.EstoqueForm;
import br.com.creativex.ui.clientes.ClientesForm;
import br.com.creativex.ui.clientepj.ClientepjForm;
import br.com.creativex.ui.fornecedor.FornecedoresForm;
import br.com.creativex.ui.caixas.CaixasForm;
import br.com.creativex.ui.ajuda.AjudaForm;
import br.com.creativex.util.Sessao;
//import com.creativex.ui.listagens.ListagensForm;

import javax.swing.*;
import java.awt.*;

/**
 * Janela principal do sistema.
 * - Área central para carregar módulos (JPanel)
 * - Menu inferior com botões
 */

public class MainWindow extends JFrame {

    private final JPanel painelConteudo;
    private final JPanel bottomMenu;
    private final br.com.creativex.domain.entity.usuario.Usuario usuarioLogado; // ✅ Atributo adicionado

    // ✅ Construtor agora recebe o Usuario
    public MainWindow(br.com.creativex.domain.entity.usuario.Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("📌 MERCADO-VS1 — Sistema de Gestão");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ... código do windowListener permanece igual ...
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmarSaida();
            }
        });

        painelConteudo = new JPanel(new BorderLayout());
        add(painelConteudo, BorderLayout.CENTER);

        bottomMenu = criarBottomMenu();
        add(bottomMenu, BorderLayout.SOUTH);

        abrirModulo(new HomeScreen());
    }

    private JPanel criarBottomMenu() {
        JPanel menu = new JPanel();
        menu.setBorder(BorderFactory.createTitledBorder("Menu principal"));
        menu.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        menu.setPreferredSize(new Dimension(0, 90));

        // Botões
        JButton btnProdutos = new JButton("Produtos");
        JButton btnEstoque = new JButton("Estoque");
        JButton btnclientes = new JButton("clientes");
        JButton btnclientepj = new JButton("clientesPJ");
        JButton btnFornecedores = new JButton("Fornecedores");
        JButton btnUsuarios = new JButton("Usuarios");
        JButton btnCaixas = new JButton("Caixas");
        JButton btnConsultaVendas = new JButton("Consulta Vendas");
        JButton btnImpressoras = new JButton("Impressoras");
        JButton btnAjuda = new JButton("Ajuda");
        JButton btnSairSistema = new JButton("Sair do sistema");

        // Adicionar botões
        menu.add(btnProdutos);
        menu.add(btnEstoque);
        menu.add(btnclientes);
        menu.add(btnclientepj);
        menu.add(btnFornecedores);
        menu.add(btnUsuarios);
        menu.add(btnCaixas);
        menu.add(btnConsultaVendas);
        menu.add(btnImpressoras);
        menu.add(btnAjuda);
        menu.add(btnSairSistema);

        // === AÇÕES CORRIGIDAS ===
        btnProdutos.addActionListener(e -> abrirModulo(new ProdutoForm()));
        btnEstoque.addActionListener(e -> abrirModulo(new EstoqueForm()));
        btnclientes.addActionListener(e -> abrirModulo(new ClientesForm()));
        btnclientepj.addActionListener(e -> abrirModulo(new ClientepjForm()));
        btnFornecedores.addActionListener(e -> abrirModulo(new FornecedoresForm()));
        btnUsuarios.addActionListener(e -> abrirModulo(new CadastroUsuarioForm()));

        // ✅ CORREÇÃO AQUI: Passando o usuarioLogado que guardamos no construtor
        btnCaixas.addActionListener(e -> abrirModulo(new CaixasForm(this.usuarioLogado, this)));

        btnConsultaVendas.addActionListener(e -> abrirModulo(new VendasConsultaForm(this)));

        btnImpressoras.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Módulo Impressoras em desenvolvimento"));
        btnAjuda.addActionListener(e -> abrirModulo(new AjudaForm()));
        btnSairSistema.addActionListener(e -> confirmarSaida());

        return menu;
    }

    public void abrirModulo(JPanel painel) {
        painelConteudo.removeAll();
        painelConteudo.add(painel, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    public void confirmarSaida() {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do sistema?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            Sessao.logout();
            System.exit(0);
        }
    }
}
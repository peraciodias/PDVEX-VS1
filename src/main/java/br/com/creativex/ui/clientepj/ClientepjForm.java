// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.clientepj;

import br.com.creativex.domain.entity.clientepj.Clientepj;
import br.com.creativex.presentation.controller.ClientepjController;
import br.com.creativex.ui.HomeScreen;
import br.com.creativex.ui.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public class ClientepjForm extends JPanel {

    private JTextField txtId, txtRazaoSocial, txtNomeFantasia, txtIe, txtEmail;
    private JFormattedTextField txtCnpj, txtTelefone, txtCep, txtLimiteCredito;
    private JTextField txtEndereco, txtNumero, txtComplemento, txtBairro, txtCidade, txtUf;

    private JButton btnNovo, btnSalvar, btnAtualizar, btnBuscar, btnListar, btnVoltar;

    private JTable table;
    private DefaultTableModel model;

    private final ClientepjController controller = br.com.creativex.config.AppFactory.clientepjController();

    public ClientepjForm() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        initComponents();
        bindEvents();
    }

    private void initComponents() {

        model = new DefaultTableModel(new String[]{
                "ID", "Razão Social", "CNPJ", "Telefone", "Cidade"
        }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        add(criarPainel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(false);
    }

    private JPanel criarPainel() {

        JPanel p = new JPanel(new GridLayout(18, 1, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Cadastro de ClientesPJ"));

        txtId = new JTextField(); txtId.setEnabled(false); addCampo(p, "ID", txtId);
        txtRazaoSocial = new JTextField(); addCampo(p, "Razão Social*", txtRazaoSocial);
        txtNomeFantasia = new JTextField(); addCampo(p, "Nome Fantasia", txtNomeFantasia);
        txtIe = new JTextField(); addCampo(p, "IE", txtIe);
        txtCnpj = new JFormattedTextField(criarMascara("##.###.###/####-##"));
        addCampo(p, "CNPJ*", txtCnpj);

        txtTelefone = new JFormattedTextField(criarMascara("(##) #####-####"));
        addCampo(p, "Telefone", txtTelefone);

        txtEmail = new JTextField(); addCampo(p, "Email", txtEmail);
        txtEndereco = new JTextField(); addCampo(p, "Endereço", txtEndereco);
        txtNumero = new JTextField(); addCampo(p, "Número", txtNumero);
        txtComplemento = new JTextField(); addCampo(p, "Complemento", txtComplemento);
        txtBairro = new JTextField(); addCampo(p, "Bairro", txtBairro);
        txtCidade = new JTextField(); addCampo(p, "Cidade*", txtCidade);
        txtUf = new JTextField(); addCampo(p, "UF", txtUf);
        txtCep = new JFormattedTextField(criarMascara("#####-###"));
        addCampo(p, "CEP", txtCep);
        txtLimiteCredito = new JFormattedTextField(); txtLimiteCredito.setValue(BigDecimal.ZERO);
        addCampo(p, "Limite Crédito R$", txtLimiteCredito);

        return p;
    }
    private JPanel criarPainelBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnAtualizar = new JButton("Atualizar");
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Listar/Id");
        btnVoltar = new JButton("Voltar");
        p.add(btnNovo); p.add(btnSalvar); p.add(btnAtualizar);
        p.add(btnBuscar); p.add(btnListar); p.add(btnVoltar);
        return p;
    }
    private void addCampo(JPanel p, String l, JTextField c) {
        p.add(new JLabel(l));
        p.add(c);
    }

    private MaskFormatter criarMascara(String m) {
        try {
            MaskFormatter mf = new MaskFormatter(m);
            mf.setPlaceholderCharacter('_');
            return mf;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    // ================= EVENTOS =================
    private void bindEvents() {
        btnNovo.addActionListener(e -> modoNovo());
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnBuscar.addActionListener(e -> buscar());
        btnListar.addActionListener(e -> listar());
        btnVoltar.addActionListener(e -> voltar());

        txtCnpj.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                String cnpj = somenteNumeros(txtCnpj.getText());
                //PASSA O Cnpj para checar se existe !!!
                txtCnpj.setForeground(validarCNPJ(cnpj) ? new Color(0, 120, 0) : Color.RED);

            }
        });

    }
    // ================= AÇÕES =================
    private void modoNovo() {
        limparCampos();
        txtId.setText("");
        btnSalvar.setVisible(true);
        btnAtualizar.setEnabled(false);
        txtRazaoSocial.requestFocus();
    }

    private void modoEdicao() {
        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(true);
    }

    private void salvar() {
        if (!validar()) return;
        try {
            controller.save(criar());
            JOptionPane.showMessageDialog(this, "ClientesPJ cadastrado!");
            listar();
            modoEdicao();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void atualizar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor para atualizar.");
            return;
        }
        try {
            Clientepj c = criar();
            c.setId(Long.parseLong(txtId.getText()));
            controller.save(c);
            JOptionPane.showMessageDialog(this, "ClientesPJ atualizado!");
            listar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage());
        }
    }

//== Remove caracteres não numéricos antes de validar
private String somenteNumeros(String s) { return s == null ? "" : s.replaceAll("\\D", ""); }

//=== teste de cnpj====
private boolean validarCNPJ(String cnpj) {

    // CNPJ deve ter 14 dígitos e não pode ser uma sequência repetida
    if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;

    try {
        int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        // Cálculo do primeiro dígito (r1)
        int soma1 = 0;
        for (int i = 0; i < 12; i++) {
            soma1 += Integer.parseInt(cnpj.substring(i, i + 1)) * peso1[i];
        }
        int r1 = soma1 % 11;
        r1 = (r1 < 2) ? 0 : 11 - r1;

        // Cálculo do segundo dígito (r2)
        int soma2 = 0;
        for (int i = 0; i < 12; i++) {
            soma2 += Integer.parseInt(cnpj.substring(i, i + 1)) * peso2[i];
        }
        soma2 += r1 * peso2[12]; // Inclui o primeiro dígito calculado no peso 2

        int r2 = soma2 % 11;
        r2 = (r2 < 2) ? 0 : 11 - r2;

        return cnpj.endsWith("" + r1 + r2);
    } catch (Exception e) {
        return false;
    }
}
//======BUSCAR======
    private void buscar() {
        String filtro = JOptionPane.showInputDialog(
                this, "Digite ID, Razão Social ou CNPJ:"
        );
        if (filtro == null || filtro.isBlank()) return;
        // remove tudo que não for número
        String somenteNumeros = filtro.replaceAll("\\D", "");
        try {
            model.setRowCount(0);
            // ================= BUSCA POR ID =================
            if (somenteNumeros.matches("\\d+") && somenteNumeros.length() <= 9) {
                Clientepj c = controller.findById(Long.parseLong(somenteNumeros));
                if (c != null) {
                    preencherCampos(c);
                    modoEdicao(); //  habilita atualização
                } else {
                    JOptionPane.showMessageDialog(this, "Fornecedor não encontrado.");
                }
                return;
            }
            // ================= BUSCA POR CNPJ =================
               if (somenteNumeros.length() == 14) {
                 Clientepj c = controller.findByCnpj(somenteNumeros);
               if (c != null) {
                    preencherCampos(c);
                    modoEdicao(); //  habilita atualização
                } else {
                    JOptionPane.showMessageDialog(this, "Fornecedor não encontrado.");
                }
                return;
            }
            // ================= BUSCA POR RAZÃO SOCIAL =================
            List<Clientepj> lista = controller.findByRazaoSocial(filtro);

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum fornecedor encontrado.");
                return;
            }
            for (Clientepj c : lista) {
                model.addRow(new Object[]{
                        c.getId(),
                        c.getRazaoSocial(),
                        c.getCnpj(),
                        c.getTelefone(),
                        c.getCidade()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage());
        }
    }

    private void listar() {
        try {
            model.setRowCount(0);
            List<Clientepj> lista = controller.listByIdLimit(1, 10);
            for (Clientepj c : lista) {
                model.addRow(new Object[]{
                        c.getId(), c.getRazaoSocial(), c.getCnpj(),
                        c.getTelefone(), c.getCidade()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    //==
    private Clientepj criar() {
        Clientepj c = new Clientepj();
        c.setRazaoSocial(txtRazaoSocial.getText());
        c.setNomeFantasia(txtNomeFantasia.getText());
        c.setCnpj(txtCnpj.getText().replaceAll("\\D", ""));
        c.setIe(txtIe.getText());

        c.setTelefone(txtTelefone.getText().replaceAll("\\D", ""));
        c.setEmail(txtEmail.getText());
        c.setEndereco(txtEndereco.getText());
        c.setNumero(txtNumero.getText());
        c.setComplemento(txtComplemento.getText());
        c.setBairro(txtBairro.getText());
        c.setCidade(txtCidade.getText());
        c.setUf(txtUf.getText());
        c.setCep(txtCep.getText().replaceAll("\\D", ""));
        c.setLimiteCredito(parseBig(txtLimiteCredito.getText()));

        return c;
    }
    //====================================
    private void preencherCampos(Clientepj c) {
        txtId.setText(String.valueOf(c.getId()));
        txtRazaoSocial.setText(c.getRazaoSocial());
        txtNomeFantasia.setText(c.getNomeFantasia());
        txtIe.setText(c.getIe());
        txtCnpj.setText(c.getCnpj());      // máscara aplicada automaticamente
        txtTelefone.setText(c.getTelefone());
        txtEmail.setText(c.getEmail());
        txtEndereco.setText(c.getEndereco());
        txtNumero.setText(c.getNumero());
        txtBairro.setText(c.getBairro());
        txtCidade.setText(c.getCidade());
        txtUf.setText(c.getUf());
        txtCep.setText(c.getCep());
        txtLimiteCredito.setValue(c.getLimiteCredito());
    }
    //================================================
    //===Validar
    private boolean validar() {
        if (txtRazaoSocial.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Razão Social é obrigatória.");
            return false;
        }
        if (txtCidade.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Cidade é obrigatória.");
            return false;
        }
        return true;
    }
    //===
    private BigDecimal parseBig(String v) {
        try {
            return new BigDecimal(v.replaceAll("[^0-9,]", "").replace(",", "."));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    //===

    private void limparCampos() {

        for (Component c : getComponents()) limparRec(c);
    }
    //===
    private void limparRec(Component c) {

        if (c instanceof JFormattedTextField ft) {
            ft.setValue(null);
        } else if (c instanceof JTextField tf) {
            tf.setText("");
        }

        if (c instanceof Container ct) {
            for (Component cc : ct.getComponents()) {
                limparRec(cc);
            }
        }
    }

    //===
    private void voltar() {
        MainWindow mw = getMainWindow();
        if (mw != null) mw.abrirModulo(new HomeScreen());
    }
    //===
    private MainWindow getMainWindow() {
        Container p = getParent();
        while (p != null) {
            if (p instanceof MainWindow mw) return mw;
            p = p.getParent();
        }
        return null;
    }
}

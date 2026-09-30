// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.fornecedor;

import br.com.creativex.domain.entity.fornecedor.Fornecedor;
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

public class FornecedoresForm extends JPanel {

    // Campos
    private JTextField txtId, txtRazaoSocial, txtNomeFantasia, txtIe, txtEmail;
    private JFormattedTextField txtCnpj, txtTelefone, txtCep, txtLimiteCredito;
    private JTextField txtEndereco, txtNumero, txtContato, txtComplemento, txtBairro, txtCidade, txtUf;

    // Botões
    private JButton btnNovo, btnSalvar, btnAtualizar, btnBuscar, btnListar, btnVoltar;

    // Tabela
    private JTable table;
    private DefaultTableModel model;

    // Controller
    private final br.com.creativex.presentation.controller.FornecedorController controller = br.com.creativex.config.AppFactory.fornecedorController();

    public FornecedoresForm() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        initComponents();
        bindEvents();
    }

    // ================= UI =================
    private void initComponents() {

        model = new DefaultTableModel(new String[]{
                "ID", "Razão Social", "CNPJ", "Telefone", "Cidade"
        }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        add(criarPainelFornecedor(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(false);
    }

    private JPanel criarPainelFornecedor() {

        JPanel p = new JPanel(new GridLayout(18, 1, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Cadastro de Fornecedor"));

        txtId = new JTextField(); txtId.setEnabled(false); addCampo(p, "ID", txtId);
        txtRazaoSocial = new JTextField(); addCampo(p, "Razão Social*", txtRazaoSocial);
        txtNomeFantasia = new JTextField(); addCampo(p, "Nome Fantasia", txtNomeFantasia);
        txtIe = new JTextField();  addCampo(p, "IE", txtIe);

        txtCnpj = new JFormattedTextField(criarMascara("##.###.###/####-##"));
        addCampo(p, "CNPJ*", txtCnpj);

        txtContato = new JTextField();   addCampo(p, "Contato*", txtContato);
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

        txtLimiteCredito = new JFormattedTextField();
        txtLimiteCredito.setValue(BigDecimal.ZERO);
        addCampo(p, "Limite Crédito", txtLimiteCredito);

        return p;
    }

    private JPanel criarPainelBotoes() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnAtualizar = new JButton("Atualizar");
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Listar");
        btnVoltar = new JButton("Voltar");

        p.add(btnNovo);
        p.add(btnSalvar);
        p.add(btnAtualizar);
        p.add(btnBuscar);
        p.add(btnListar);
        p.add(btnVoltar);
        return p;
    }
    private void addCampo(JPanel p, String label, JTextField campo) {
        p.add(new JLabel(label));
        p.add(campo);
    }
    private MaskFormatter criarMascara(String m) {
        try {
            MaskFormatter mf = new MaskFormatter(m);
            mf.setPlaceholderCharacter('_');
            mf.setValueContainsLiteralCharacters(true);
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

    // ================= AÇÕES =================
    private void modoNovo() {
        limparCampos();
        txtId.setText("");
        btnSalvar.setVisible(true);
        txtRazaoSocial.requestFocus();
    }

    private void modoEdicao() {
        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(true);

    }

    private void salvar() {
        if (!validar()) return;

        try {
            controller.save(criarFornecedor());
            JOptionPane.showMessageDialog(this, "Fornecedor cadastrado!");
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
            Fornecedor f = criarFornecedor();
            f.setId(Long.parseLong(txtId.getText()));

            controller.save(f);

            JOptionPane.showMessageDialog(this, "Fornecedor atualizado!");
            listar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage());
        }
    }

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

                Fornecedor f = controller.findById(Long.parseLong(somenteNumeros));
                if (f != null) {
                    preencherCampos(f);
                    modoEdicao(); //  habilita atualização
                } else {
                    JOptionPane.showMessageDialog(this, "Fornecedor não encontrado.");
                }
                return;
            }

            // ================= BUSCA POR CNPJ =================
            if (somenteNumeros.length() == 14) {

                Fornecedor f = controller.findByCnpj(somenteNumeros);
                if (f != null) {
                    preencherCampos(f);
                    modoEdicao(); //  habilita atualização
                } else {
                    JOptionPane.showMessageDialog(this, "Fornecedor não encontrado.");
                }
                return;
            }

            // ================= BUSCA POR RAZÃO SOCIAL =================
            List<Fornecedor> lista = controller.findByRazaoSocial(filtro);

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum fornecedor encontrado.");
                return;
            }

            for (Fornecedor f : lista) {
                model.addRow(new Object[]{
                        f.getId(),
                        f.getRazaoSocial(),
                        f.getCnpj(),
                        f.getTelefone(),
                        f.getCidade()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage());
        }
    }


    private void listar() {
        try {
            model.setRowCount(0);
            List<Fornecedor> lista = controller.listByIdLimit(1, 10);
            for (Fornecedor f : lista) {
                model.addRow(new Object[]{
                        f.getId(), f.getRazaoSocial(), f.getCnpj(),
                        f.getTelefone(), f.getCidade()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar.");
        }
    }
    // ================= UTIL =================

	    //====================================
    private Fornecedor criarFornecedor() {

        Fornecedor f = new Fornecedor();
        f.setRazaoSocial(txtRazaoSocial.getText());
        f.setNomeFantasia(txtNomeFantasia.getText());
        f.setCnpj(txtCnpj.getText().replaceAll("\\D", ""));
        f.setIe(txtIe.getText());
        f.setContato(txtContato.getText());

        // TELEFONE: SOMENTE NÚMEROS (VARCHAR 11)
        f.setTelefone(txtTelefone.getText().replaceAll("\\D", ""));

        f.setEmail(txtEmail.getText());
        f.setEndereco(txtEndereco.getText());
        f.setNumero(txtNumero.getText());
        f.setBairro(txtBairro.getText());
        f.setCidade(txtCidade.getText());
        f.setUf(txtUf.getText());

        // CEP sem máscara
        f.setCep(txtCep.getText().replaceAll("\\D", ""));

        f.setLimiteCredito(parseBig(txtLimiteCredito.getText()));

        return f;
    }
    //====================================
    private void preencherCampos(Fornecedor f) {
        txtId.setText(String.valueOf(f.getId()));
        txtRazaoSocial.setText(f.getRazaoSocial());
        txtNomeFantasia.setText(f.getNomeFantasia());
        txtIe.setText(f.getIe());
        txtContato.setText(f.getContato());
        txtCnpj.setText(f.getCnpj());      // máscara aplicada automaticamente
        txtTelefone.setText(f.getTelefone());
        txtEmail.setText(f.getEmail());
        txtEndereco.setText(f.getEndereco());
        txtNumero.setText(f.getNumero());
        txtBairro.setText(f.getBairro());
        txtCidade.setText(f.getCidade());
        txtUf.setText(f.getUf());
        txtCep.setText(f.getCep());

        txtLimiteCredito.setValue(f.getLimiteCredito());
    }
    //===
    //===Validar
    private boolean validar() {
        if (txtContato.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Contato é obrigatório.");
            txtContato.requestFocus();
            return false;
        }

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

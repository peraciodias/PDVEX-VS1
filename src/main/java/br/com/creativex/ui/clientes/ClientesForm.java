// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.clientes;

import br.com.creativex.domain.entity.cliente.Cliente;
import br.com.creativex.ui.HomeScreen;
import br.com.creativex.ui.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientesForm extends JPanel {

    private JTextField txtId, txtNome, txtRg, txtEmail, txtEndereco, txtNumero, txtBairro, txtCidade, txtUf;
    private JFormattedTextField txtCpf, txtTelefone, txtCep, txtLimiteCredito;
    private JButton btnNovo, btnSalvar, btnAtualizar, btnBuscar, btnListar, btnVoltar;
    private JTable table;
    private DefaultTableModel model;
    private final br.com.creativex.presentation.controller.ClienteController controller = br.com.creativex.config.AppFactory.clienteController();

    public ClientesForm() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        initComponents();
        bindEvents();
    }

    private void initComponents() {
        model = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "Telefone", "Cidade"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);

        add(criarPainelCampos(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(false);
    }

    private JPanel criarPainelCampos() {
        JPanel p = new JPanel(new GridLayout(15, 1, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Cadastro de Clientes PF"));

        txtId = new JTextField(); txtId.setEditable(false); addCampo(p, "ID", txtId);
        txtNome = new JTextField(); addCampo(p, "Nome Completo*", txtNome);
        txtRg = new JTextField(); addCampo(p, "RG", txtRg);
        txtCpf = new JFormattedTextField(criarMascara("###.###.###-##")); addCampo(p, "CPF*", txtCpf);
        txtTelefone = new JFormattedTextField(criarMascara("(##) #####-####")); addCampo(p, "Telefone", txtTelefone);
        txtEmail = new JTextField(); addCampo(p, "Email", txtEmail);
        txtCep = new JFormattedTextField(criarMascara("#####-###")); addCampo(p, "CEP (Busca Automática)", txtCep);
        txtEndereco = new JTextField(); addCampo(p, "Logradouro", txtEndereco);
        txtNumero = new JTextField(); addCampo(p, "Número", txtNumero);
        txtBairro = new JTextField(); addCampo(p, "Bairro", txtBairro);
        txtCidade = new JTextField(); addCampo(p, "Cidade*", txtCidade);
        txtUf = new JTextField(); addCampo(p, "UF", txtUf);
        txtLimiteCredito = new JFormattedTextField(); txtLimiteCredito.setValue(BigDecimal.ZERO);
        addCampo(p, "Limite de Crédito (R$)", txtLimiteCredito);

        return p;
    }

    private void addCampo(JPanel p, String label, JTextField campo) {
        p.add(new JLabel(label));
        p.add(campo);
    }

    private JPanel criarPainelBotoes() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnAtualizar = new JButton("Atualizar");
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Listar");
        btnVoltar = new JButton("Voltar");
        pnl.add(btnNovo); pnl.add(btnSalvar); pnl.add(btnAtualizar);
        pnl.add(btnBuscar); pnl.add(btnListar); pnl.add(btnVoltar);
        return pnl;
    }

    private void bindEvents() {
        btnNovo.addActionListener(e -> modoNovo());
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnBuscar.addActionListener(e -> buscar());
        btnListar.addActionListener(e -> listar());
        btnVoltar.addActionListener(e -> voltar());

        // Busca de CEP Automática
        txtCep.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) { buscarCep(txtCep.getText()); }
        });

        txtCpf.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                String cpf = somenteNumeros(txtCpf.getText());
                if (cpf.length() == 11) {
                    //PASSA O CPF para checar se existe !!!
                    txtCpf.setForeground(validarCPF(cpf) ? new Color(0, 120, 0) : Color.RED);
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) carregarCliente((long) model.getValueAt(row, 0));
            }
        });
    }

    private void buscarCep(String cep) {
        String cleanCep = somenteNumeros(cep);
        if (cleanCep.length() != 8) return;
        new Thread(() -> {
            try {
                URL url = java.net.URI.create("https://viacep.com.br/ws/" + cleanCep + "/json/").toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String json = br.lines().reduce("", (acc, line) -> acc + line);
                    if (json.contains("\"erro\": true")) return;
                    SwingUtilities.invokeLater(() -> {
                        txtEndereco.setText(extrairValorJson(json, "logradouro"));
                        txtBairro.setText(extrairValorJson(json, "bairro"));
                        txtCidade.setText(extrairValorJson(json, "localidade"));
                        txtUf.setText(extrairValorJson(json, "uf"));
                        txtNumero.requestFocus();
                    });
                }
            } catch (Exception e) { System.err.println("Erro CEP: " + e.getMessage()); }
        }).start();
    }

    private String extrairValorJson(String json, String campo) {
        try {
            String chave = "\"" + campo + "\": \"";
            int i = json.indexOf(chave) + chave.length();
            return json.substring(i, json.indexOf("\"", i));
        } catch (Exception e) { return ""; }
    }

    private void atualizar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para atualizar.");
            return;
        }
        if (!validarForm()) return;
        try {
            Cliente c = criarObjetoCliente();
            c.setId(Long.parseLong(txtId.getText()));
            controller.save(c);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            listar();
            modoNovo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage());
        }
    }

    private boolean validarCPF(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;
        try {
            int d1 = 0, d2 = 0;
            for (int i = 0; i < 9; i++) {
                int n = Integer.parseInt(cpf.substring(i, i + 1));
                d1 += (10 - i) * n;
                d2 += (11 - i) * n;
            }
            int r1 = 11 - (d1 % 11); if (r1 > 9) r1 = 0;
            d2 += 2 * r1;
            int r2 = 11 - (d2 % 11); if (r2 > 9) r2 = 0;
            return cpf.endsWith("" + r1 + r2);
        } catch (Exception e) { return false; }
    }

    private void salvar() {
        if (!validarForm()) return;
        try {
            controller.save(criarObjetoCliente());
            JOptionPane.showMessageDialog(this, "Cliente salvo!");
            listar();
            modoNovo();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
    }

    private boolean validarForm() {
        if (txtNome.getText().isBlank()) return msgErro("Nome é obrigatório.");
        if (!validarCPF(somenteNumeros(txtCpf.getText()))) return msgErro("CPF inválido.");
        return true;
    }

    private boolean msgErro(String m) {
        JOptionPane.showMessageDialog(this, m);
        return false;
    }

    private void listar() {
        try {
            model.setRowCount(0);
            List<Cliente> lista = controller.listByIdLimit(1, 100);
            for (Cliente c : lista) {
                model.addRow(new Object[]{c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), c.getCidade()});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void carregarCliente(long id) {
        try {
            Cliente c = controller.findById(id);
            if (c != null) {
                txtId.setText(String.valueOf(c.getId()));
                txtNome.setText(c.getNome());
                txtCpf.setText(c.getCpf());
                txtRg.setText(c.getRg());
                txtTelefone.setText(c.getTelefone());
                txtEmail.setText(c.getEmail());
                txtCep.setText(c.getCep());
                txtEndereco.setText(c.getEndereco());
                txtNumero.setText(c.getNumero());
                txtBairro.setText(c.getBairro());
                txtCidade.setText(c.getCidade());
                txtUf.setText(c.getUf());
                txtLimiteCredito.setValue(c.getLimiteCredito());
                btnAtualizar.setEnabled(true);
                btnSalvar.setVisible(false);

                btnSalvar.setVisible(false);  // Esconde o salvar (evita duplicar)
                btnAtualizar.setEnabled(true); // Habilita o atualizar

                revalidate();
                repaint();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void modoNovo() {
        limparRec(this); // Limpa todos os campos
        txtId.setText(""); // Garante que o ID esteja vazio
        txtLimiteCredito.setValue(BigDecimal.ZERO);

        btnSalvar.setVisible(true);    // Mostra o Salvar para novo cadastro
        btnAtualizar.setEnabled(false); // Desativa o atualizar (pois não há ID ainda)

        txtNome.requestFocus(); // Coloca o cursor no nome
        revalidate(); // Avisa o Swing que o painel mudou
        repaint();
    }

    private void limparRec(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField tf) tf.setText("");
            else if (c instanceof Container ct) limparRec(ct);
        }
    }

    private Cliente criarObjetoCliente() {
        Cliente c = new Cliente();
        c.setNome(txtNome.getText());
        c.setCpf(somenteNumeros(txtCpf.getText()));
        c.setRg(txtRg.getText());
        c.setTelefone(somenteNumeros(txtTelefone.getText()));
        c.setEmail(txtEmail.getText());
        c.setCep(somenteNumeros(txtCep.getText()));
        c.setEndereco(txtEndereco.getText());
        c.setNumero(txtNumero.getText());
        c.setBairro(txtBairro.getText());
        c.setCidade(txtCidade.getText());
        c.setUf(txtUf.getText());
        c.setLimiteCredito(new BigDecimal(txtLimiteCredito.getValue().toString()));
        return c;
    }

    private String somenteNumeros(String s) { return s == null ? "" : s.replaceAll("\\D", ""); }

    private MaskFormatter criarMascara(String m) {
        try {
            MaskFormatter mf = new MaskFormatter(m);
            mf.setPlaceholderCharacter('_');
            mf.setValueContainsLiteralCharacters(true);
            return mf;
        } catch (Exception e) { return null; }
    }

    private void buscar() {
        String f = JOptionPane.showInputDialog(this, "ID, Nome ou CPF:");
        if (f == null || f.isBlank()) return;
        try {
            model.setRowCount(0);
            String num = somenteNumeros(f);

            if (num.length() > 0 && num.length() <= 6) {
                Cliente c = controller.findById(Long.parseLong(num));
                if (c != null) {
                    model.addRow(new Object[]{c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), c.getCidade()});
                    carregarCliente(c.getId()); // Sincroniza campos
                }
            } else if (num.length() == 11) {
                Cliente c = controller.findByCpf(num);
                if (c != null) {
                    model.addRow(new Object[]{c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), c.getCidade()});
                    carregarCliente(c.getId()); // Sincroniza campos
                }
            } else {
                List<Cliente> lista = controller.findByName(f);
                for (Cliente c : lista) {
                    model.addRow(new Object[]{c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), c.getCidade()});
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void voltar() {
        MainWindow mw = (MainWindow) SwingUtilities.getWindowAncestor(this);
        if (mw != null) mw.abrirModulo(new HomeScreen());
    }
}

// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.cadastrousuario;

import br.com.creativex.config.AppFactory;
import br.com.creativex.domain.entity.usuario.Usuario;
import br.com.creativex.presentation.controller.UsuarioController;
import br.com.creativex.ui.HomeScreen; // Import para voltar
import br.com.creativex.ui.MainWindow; // Para acessar o método abrirModulo
import javax.swing.*;
import java.awt.*;

public class CadastroUsuarioForm extends JPanel {
    private JTextField txtNome, txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbPerfil;
    private final UsuarioController controller = AppFactory.usuarioController();

    public CadastroUsuarioForm() {
        // Usamos GridBagLayout no painel principal apenas para centralizar o formulário
        setLayout(new GridBagLayout());

        // --- PAINEL DO FORMULÁRIO (O que fica no meio) ---
        JPanel formReal = new JPanel(new BorderLayout(10, 10));
        formReal.setPreferredSize(new Dimension(450, 350)); // Tamanho fixo para não esticar
        formReal.setBorder(BorderFactory.createTitledBorder("Dados do Novo Usuário"));

        // Painel Interno de Campos
        JPanel painelCampos = new JPanel(new GridLayout(4, 2, 10, 20));

        painelCampos.add(new JLabel(" Nome Completo:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel(" Login de Acesso:"));
        txtLogin = new JTextField();
        painelCampos.add(txtLogin);

        painelCampos.add(new JLabel(" Senha:"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        painelCampos.add(new JLabel(" Perfil:"));
        cbPerfil = new JComboBox<>(new String[]{"OPERADOR", "ADMIN"});
        painelCampos.add(cbPerfil);

        formReal.add(painelCampos, BorderLayout.CENTER);

        // --- PAINEL DE BOTÕES (SALVAR E VOLTAR) ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnVoltar = new JButton("Voltar");
        JButton btnSalvar = new JButton("Cadastrar Usuário");
        btnSalvar.setBackground(new Color(0, 120, 215)); // Um destaque azul
        btnSalvar.setForeground(Color.WHITE);

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnSalvar);
        formReal.add(painelBotoes, BorderLayout.SOUTH);

        // Adiciona o formulário real no centro do GridBagLayout
        add(formReal);

        // --- AÇÕES ---
        btnSalvar.addActionListener(e -> cadastrar());

        btnVoltar.addActionListener(e -> {
            // Obtém a referência da MainWindow para abrir a Home novamente
            MainWindow main = (MainWindow) SwingUtilities.getWindowAncestor(this);
            main.abrirModulo(new HomeScreen());
        });
    }

    private void cadastrar() {
        if (txtLogin.getText().isEmpty() || new String(txtSenha.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios!");
            return;
        }

        Usuario u = new Usuario();
        u.setNome(txtNome.getText());
        u.setLogin(txtLogin.getText());
        u.setSenha(new String(txtSenha.getPassword()));
        u.setPerfil(cbPerfil.getSelectedItem().toString());

        if (controller.salvar(u)) {
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário.");
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        cbPerfil.setSelectedIndex(0);
    }
}

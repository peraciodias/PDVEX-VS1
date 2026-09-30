// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.login;

import br.com.creativex.config.AppFactory;
import br.com.creativex.domain.entity.usuario.Usuario;
import br.com.creativex.presentation.controller.UsuarioController;
import br.com.creativex.ui.MainWindow;
import br.com.creativex.util.Sessao;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private final UsuarioController usuarioController = AppFactory.usuarioController();

    public LoginForm() {
        setTitle("MERCADO-VS1 - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new GridLayout(5, 1, 10, 10));


        // Estilização básica
        add(new JLabel("  Login:", SwingConstants.LEFT));
        txtLogin = new JTextField();
        add(txtLogin);

        add(new JLabel("  Senha:", SwingConstants.LEFT));
        txtSenha = new JPasswordField();
        add(txtSenha);

        btnEntrar = new JButton("Entrar");
        add(btnEntrar);

        // Ação do Botão
        btnEntrar.addActionListener(this::efetuarLogin);

        // Permite dar Enter para logar
        getRootPane().setDefaultButton(btnEntrar);
    }

    private void efetuarLogin(ActionEvent e) {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        Usuario usuario = usuarioController.autenticar(
                txtLogin.getText(),
                new String(txtSenha.getPassword())
        );

        if (usuario != null) {
            Sessao.login(usuario);          // 🔥 aqui está o ponto-chave
            new MainWindow(usuario).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login ou senha inválidos.");
        }
    }
    public static void main(String[] args) {
        // Para testar o formulário isoladamente
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}

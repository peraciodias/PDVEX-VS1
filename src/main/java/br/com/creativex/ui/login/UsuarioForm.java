// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/



// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.login;
import br.com.creativex.config.AppFactory;
import br.com.creativex.domain.entity.usuario.Usuario;
import br.com.creativex.presentation.controller.UsuarioController;
import javax.swing.*;
import java.awt.*;

public class UsuarioForm extends JFrame {
    private JTextField txtNome, txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbPerfil;
    private JButton btnSalvar;
    private final UsuarioController controller = AppFactory.usuarioController();

    public UsuarioForm() {
        setTitle("Cadastro de Usuários - MERCADO-VS1");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel(" Nome Completo:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel(" Login de Acesso:"));
        txtLogin = new JTextField();
        add(txtLogin);

        add(new JLabel(" Senha:"));
        txtSenha = new JPasswordField();
        add(txtSenha);

        add(new JLabel(" Perfil:"));
        cbPerfil = new JComboBox<>(new String[]{"OPERADOR", "ADMIN"});
        add(cbPerfil);

        btnSalvar = new JButton("Cadastrar Usuário");
        add(btnSalvar);

        btnSalvar.addActionListener(e -> cadastrar());
    }

    private void cadastrar() {
        Usuario u = new Usuario();
        u.setNome(txtNome.getText());
        u.setLogin(txtLogin.getText());
        u.setSenha(new String(txtSenha.getPassword()));
        u.setPerfil(cbPerfil.getSelectedItem().toString());

        if (controller.salvar(u)) {
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            dispose(); // Fecha a tela após cadastrar
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar. Verifique o console.");
        }
    }
}

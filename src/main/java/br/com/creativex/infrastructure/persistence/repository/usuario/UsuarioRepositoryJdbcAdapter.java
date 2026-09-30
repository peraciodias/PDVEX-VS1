// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.usuario;

import br.com.creativex.domain.entity.usuario.Usuario;
import br.com.creativex.domain.repository.UsuarioRepository;

public class UsuarioRepositoryJdbcAdapter implements UsuarioRepository {

    private final UsuarioDAO usuarioDAO;

    public UsuarioRepositoryJdbcAdapter(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Usuario autenticar(String login, String senha) {
        return usuarioDAO.autenticar(login, senha);
    }

    @Override
    public boolean salvar(Usuario usuario) {
        return usuarioDAO.salvar(usuario);
    }
}

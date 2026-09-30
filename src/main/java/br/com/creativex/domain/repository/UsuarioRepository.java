// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.repository;

import br.com.creativex.domain.entity.usuario.Usuario;

public interface UsuarioRepository {
    Usuario autenticar(String login, String senha);
    boolean salvar(Usuario usuario);
}

// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.application.usuario;

import br.com.creativex.domain.entity.usuario.Usuario;
import br.com.creativex.domain.repository.UsuarioRepository;

public class UsuarioUseCases {

    private final UsuarioRepository repository;

    public UsuarioUseCases(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario autenticar(String login, String senha) {
        return repository.autenticar(login, senha);
    }

    public boolean salvar(Usuario usuario) {
        return repository.salvar(usuario);
    }
}

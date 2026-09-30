// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.presentation.controller;

import br.com.creativex.application.usuario.UsuarioUseCases;
import br.com.creativex.domain.entity.usuario.Usuario;

public class UsuarioController {

    private final UsuarioUseCases useCases;

    public UsuarioController(UsuarioUseCases useCases) {
        this.useCases = useCases;
    }

    public Usuario autenticar(String login, String senha) {
        return useCases.autenticar(login, senha);
    }

    public boolean salvar(Usuario usuario) {
        return useCases.salvar(usuario);
    }
}

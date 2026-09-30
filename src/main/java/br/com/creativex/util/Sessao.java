// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.util;

import br.com.creativex.domain.entity.usuario.Usuario;

/**
 * Gerenciador de sessão do usuário logado (Singleton).
 * 
 * Responsável por manter o contexto do usuário autenticado durante toda
 * a sessão da aplicação. Implementa o padrão Singleton com construtor privado
 * para garantir que não exista mais de uma instância.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public class Sessao {

    /** Instância estática do usuário logado */
    private static Usuario usuarioLogado;

    /**
     * Construtor privado para impedir instanciação da classe.
     * 
     * Garante que esta classe seja usada apenas como gerenciadora estática
     * de sessão.
     */
    private Sessao() {
        // impede instanciação
    }

    /**
     * Realiza o login de um usuário, armazenando-o na sessão.
     * 
     * @param usuario O usuário a ser autenticado
     */
    public static void login(Usuario usuario) {
        usuarioLogado = usuario;
    }

    /**
     * Retorna o usuário atualmente logado na sessão.
     * 
     * @return O usuário logado, ou {@code null} se nenhum usuário está logado
     */
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    /**
     * Verifica se há um usuário atualmente logado na sessão.
     * 
     * @return {@code true} se um usuário está logado, {@code false} caso contrário
     */
    public static boolean isLogado() {
        return usuarioLogado != null;
    }

    /**
     * Realiza o logout do usuário atual, limpando a sessão.
     */
    public static void logout() {
        usuarioLogado = null;
    }
}

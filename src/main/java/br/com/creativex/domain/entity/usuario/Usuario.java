// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.usuario;

/**
 * Entidade que representa um usuário do sistema ERP-PDVex.
 * 
 * Encapsula informações de identificação, autenticação e perfil de um usuário.
 * Utilizada para autenticação, autorização e auditoria de operações no sistema.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public class Usuario {

    /** Identificador único do usuário */
    private long id;
    
    /** Nome completo do usuário */
    private String nome;
    
    /** Login/username para autenticação */
    private String login;
    
    /** Senha criptografada do usuário */
    private String senha;
    
    /** Perfil/role do usuário (ex: ADMIN, VENDEDOR, GERENTE) */
    private String perfil;
    
    /** Indica se o usuário está ativo no sistema */
    private boolean ativo;

    /**
     * Construtor padrão sem argumentos.
     */
    public Usuario() {
    }

    /**
     * Construtor com inicialização dos dados essenciais.
     * 
     * @param id Identificador único do usuário
     * @param nome Nome completo do usuário
     * @param perfil Perfil/role do usuário no sistema
     */
    public Usuario(long id, String nome, String perfil) {
        this.id = id;
        this.nome = nome;
        this.perfil = perfil;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}

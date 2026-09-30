// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.cliente;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entidade que representa um cliente pessoa física no sistema ERP-PDVex.
 * 
 * Encapsula dados pessoais, de contato, endereço e informações comerciais
 * (limite de crédito) de um cliente. Utilizada para gerenciamento de clientes
 * e operações de vendas.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public class Cliente {

    /** Identificador único do cliente */
    private Long id;
    
    /** Nome completo do cliente */
    private String nome;
    
    /** Cadastro de Pessoa Física (CPF) */
    private String cpf;
    
    /** Registro Geral (RG) */
    private String rg;
    
    /** Telefone de contato */
    private String telefone;
    
    /** Email do cliente */
    private String email;
    
    /** Logradouro do endereço */
    private String endereco;
    
    /** Número do imóvel */
    private String numero;
    
    /** Bairro */
    private String bairro;
    
    /** Cidade */
    private String cidade;
    
    /** Unidade federativa (estado) */
    private String uf;
    
    /** Código de endereçamento postal (CEP) */
    private String cep;

    /** Limite de crédito disponível para compras */
    private BigDecimal limiteCredito;
    
    /** Data e hora do cadastro do cliente */
    private Timestamp dataCadastro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(BigDecimal limiteCredito) { this.limiteCredito = limiteCredito; }
    public Timestamp getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Timestamp dataCadastro) { this.dataCadastro = dataCadastro; }
}

// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.config;

import java.math.BigDecimal;

public class Estabelecimento {
    private String razaoSocial;
    private String cnpj;
    private String inscricaoEstadual;
    private int regimeTributario;
    private BigDecimal aliqIbpt;

    // Getters e Setters
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public int getRegimeTributario() { return regimeTributario; }
    public void setRegimeTributario(int regimeTributario) { this.regimeTributario = regimeTributario; }

    public BigDecimal getAliqIbpt() { return aliqIbpt; }
    public void setAliqIbpt(BigDecimal aliqIbpt) { this.aliqIbpt = aliqIbpt; }

// Método SET (para o DAO conseguir gravar o dado vindo do banco)
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    // Método GET (para o Cupom conseguir ler o dado)
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }
}

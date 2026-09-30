// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.config;

import java.math.BigDecimal;

/**
 * Entidade que representa o resultado da view vw_pdv_bipagem.
 * Contém informações de produto com cálculos tributários aplicados.
 *
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-05-09
 */
public class ProdutoBipagem {

    private Long id;
    private String codigoBarra;
    private String descricao;
    private String marca;
    private BigDecimal precoVenda;
    private BigDecimal quantidadeEstoque;
    private String cstIcms;
    private BigDecimal precoCusto;
    private BigDecimal aliquotaAplicada;
    private BigDecimal valorImpostoItem;

    public ProdutoBipagem() {}

    public ProdutoBipagem(Long id, String codigoBarra, String descricao, String marca,
                          BigDecimal precoVenda, BigDecimal quantidadeEstoque,
                          String cstIcms, BigDecimal precoCusto,
                          BigDecimal aliquotaAplicada, BigDecimal valorImpostoItem) {
        this.id = id;
        this.codigoBarra = codigoBarra;
        this.descricao = descricao;
        this.marca = marca;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.cstIcms = cstIcms;
        this.precoCusto = precoCusto;
        this.aliquotaAplicada = aliquotaAplicada;
        this.valorImpostoItem = valorImpostoItem;
    }

    // ========================
    // GETTERS E SETTERS
    // ========================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public BigDecimal getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getCstIcms() {
        return cstIcms;
    }

    public void setCstIcms(String cstIcms) {
        this.cstIcms = cstIcms;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getAliquotaAplicada() {
        return aliquotaAplicada;
    }

    public void setAliquotaAplicada(BigDecimal aliquotaAplicada) {
        this.aliquotaAplicada = aliquotaAplicada;
    }

    public BigDecimal getValorImpostoItem() {
        return valorImpostoItem;
    }

    public void setValorImpostoItem(BigDecimal valorImpostoItem) {
        this.valorImpostoItem = valorImpostoItem;
    }

    @Override
    public String toString() {
        return "ProdutoBipagem{" +
                "id=" + id +
                ", codigoBarra='" + codigoBarra + '\'' +
                ", descricao='" + descricao + '\'' +
                ", marca='" + marca + '\'' +
                ", precoVenda=" + precoVenda +
                ", quantidadeEstoque=" + quantidadeEstoque +
                ", cstIcms='" + cstIcms + '\'' +
                ", precoCusto=" + precoCusto +
                ", aliquotaAplicada=" + aliquotaAplicada +
                ", valorImpostoItem=" + valorImpostoItem +
                '}';
    }
}

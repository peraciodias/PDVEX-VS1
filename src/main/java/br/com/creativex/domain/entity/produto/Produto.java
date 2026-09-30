// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.produto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Produto {

    private long id;
    private String codigoBarra;
    private String origem;
    private String descricao;
    private String marca;
    private String atributos;
    private String unidadeMedida;
    private String categoria;
    private int codGrupo;
    private String grupo;
    private char tipoBalanca;
    private BigDecimal quantidadeEstoque;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private String ncm;
    private String cest;
    private String cfopPadrao;
    private String unidadeTributavel;
    private String ceanTributavel;
    private String cstIcms;
    private BigDecimal aliquotaIcms;
    private String cstPis;
    private BigDecimal ppis;
    private String cstCofins;
    private BigDecimal pcofins;
    private Timestamp dataCadastro;
    private String loja;

    // Novos campos da View
    private BigDecimal aliquotaAplicada;
    private BigDecimal valorImpostoItem;

    public Produto() {}

    // --- TODOS OS SETTERS NECESSÁRIOS PARA O MAPEAMENTO ---
    public void setId(long id) { this.id = id; }
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra = codigoBarra; }
    public void setOrigem(String origem) { this.origem = origem; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setAtributos(String atributos) { this.atributos = atributos; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setCodGrupo(int codGrupo) { this.codGrupo = codGrupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public void setTipoBalanca(char tipoBalanca) { this.tipoBalanca = tipoBalanca; }
    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
    public void setNcm(String ncm) { this.ncm = ncm; }
    public void setCest(String cest) { this.cest = cest; }
    public void setCfopPadrao(String cfopPadrao) { this.cfopPadrao = cfopPadrao; }
    public void setUnidadeTributavel(String unidadeTributavel) { this.unidadeTributavel = unidadeTributavel; }
    public void setCeanTributavel(String ceanTributavel) { this.ceanTributavel = ceanTributavel; }
    public void setCstIcms(String cstIcms) { this.cstIcms = cstIcms; }
    public void setAliquotaIcms(BigDecimal aliquotaIcms) { this.aliquotaIcms = aliquotaIcms; }
    public void setCstPis(String cstPis) { this.cstPis = cstPis; }
    public void setPpis(BigDecimal ppis) { this.ppis = ppis; }
    public void setCstCofins(String cstCofins) { this.cstCofins = cstCofins; }
    public void setPcofins(BigDecimal pcofins) { this.pcofins = pcofins; }
    public void setDataCadastro(Timestamp dataCadastro) { this.dataCadastro = dataCadastro; }
    public void setLoja(String loja) { this.loja = loja; }
    public void setAliquotaAplicada(BigDecimal aliquotaAplicada) { this.aliquotaAplicada = aliquotaAplicada; }
    public void setValorImpostoItem(BigDecimal valorImpostoItem) { this.valorImpostoItem = valorImpostoItem; }

    // --- GETTERS ---
    public long getId() { return id; }
    public String getCodigoBarra() { return codigoBarra; }
    public String getOrigem() { return origem; }
    public String getDescricao() { return descricao; }
    public String getMarca() { return marca; }
    public String getAtributos() { return atributos; }
    public String getUnidadeMedida() { return unidadeMedida; }
    public String getCategoria() { return categoria; }
    public int getCodGrupo() { return codGrupo; }
    public String getGrupo() { return grupo; }
    public char getTipoBalanca() { return tipoBalanca; }
    public BigDecimal getQuantidadeEstoque() { return quantidadeEstoque; }
    public BigDecimal getPrecoCusto() { return precoCusto; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public String getNcm() { return ncm; }
    public String getCest() { return cest; }
    public String getCfopPadrao() { return cfopPadrao; }
    public String getUnidadeTributavel() { return unidadeTributavel; }
    public String getCeanTributavel() { return ceanTributavel; }
    public String getCstIcms() { return cstIcms; }
    public BigDecimal getAliquotaIcms() { return aliquotaIcms; }
    public String getCstPis() { return cstPis; }
    public BigDecimal getPpis() { return ppis; }
    public String getCstCofins() { return cstCofins; }
    public BigDecimal getPcofins() { return pcofins; }
    public Timestamp getDataCadastro() { return dataCadastro; }
    public String getLoja() { return loja; }
    public BigDecimal getAliquotaAplicada() { return aliquotaAplicada; }
    public BigDecimal getValorImpostoItem() { return valorImpostoItem; }
}
// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.venda;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ItemVenda {

    private Long idProduto;
    private String codigoBarra;
    private String nomeProduto;

    private BigDecimal quantidade = BigDecimal.ZERO;
    private BigDecimal precoUnitario = BigDecimal.ZERO;
    private BigDecimal descontoItem = BigDecimal.ZERO;
    private BigDecimal subtotal = BigDecimal.ZERO;

    // Snapshots (histórico)
    private BigDecimal precoCustoMomento;
    private String cstFiscalMomento;

    private BigDecimal aliquotaIcms = BigDecimal.ZERO;
    private BigDecimal valorIcms = BigDecimal.ZERO;

    private String cstPis;
    private BigDecimal ppis = BigDecimal.ZERO;
    private BigDecimal valorPis = BigDecimal.ZERO;

    private String cstCofins;
    private BigDecimal pcofins = BigDecimal.ZERO;
    private BigDecimal valorCofins = BigDecimal.ZERO;

    public ItemVenda() {}

    public ItemVenda(Long idProduto, String nomeProduto,
                     BigDecimal quantidade, BigDecimal precoUnitario) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade != null ? quantidade : BigDecimal.ZERO;
        this.precoUnitario = precoUnitario != null ? precoUnitario : BigDecimal.ZERO;
        calcularSubtotal();
    }

    // =========================
    // REGRA DE NEGÓCIO
    // =========================
    public void calcularSubtotal() {
        BigDecimal desconto = descontoItem != null ? descontoItem : BigDecimal.ZERO;
        this.subtotal = quantidade
                .multiply(precoUnitario)
                .subtract(desconto)
                .setScale(2, RoundingMode.HALF_UP);
        calcularTributos();
    }

    public void calcularTributos() {
        BigDecimal base = subtotal != null ? subtotal : BigDecimal.ZERO;
        this.valorIcms = base.multiply(aliquotaIcms != null ? aliquotaIcms : BigDecimal.ZERO)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        this.valorPis = base.multiply(ppis != null ? ppis : BigDecimal.ZERO)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        this.valorCofins = base.multiply(pcofins != null ? pcofins : BigDecimal.ZERO)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalTributos() {
        return valorIcms.add(valorPis).add(valorCofins);
    }

    // =========================
    // GETTERS E SETTERS
    // =========================
    public Long getIdProduto() { return idProduto; }
    public void setIdProduto(Long idProduto) { this.idProduto = idProduto; }
    public String getCodigoBarra() { return codigoBarra; }
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra = codigoBarra; }
    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade != null ? quantidade : BigDecimal.ZERO;
        calcularSubtotal();
    }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario != null ? precoUnitario : BigDecimal.ZERO;
        calcularSubtotal();
    }
    public BigDecimal getDescontoItem() { return descontoItem; }
    public void setDescontoItem(BigDecimal descontoItem) {
        this.descontoItem = descontoItem != null ? descontoItem : BigDecimal.ZERO;
        calcularSubtotal();
    }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getPrecoCustoMomento() { return precoCustoMomento; }
    public void setPrecoCustoMomento(BigDecimal precoCustoMomento) { this.precoCustoMomento = precoCustoMomento; }
    public String getCstFiscalMomento() { return cstFiscalMomento; }
    public void setCstFiscalMomento(String cstFiscalMomento) { this.cstFiscalMomento = cstFiscalMomento; }
    public BigDecimal getAliquotaIcms() { return aliquotaIcms; }
    public void setAliquotaIcms(BigDecimal aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms != null ? aliquotaIcms : BigDecimal.ZERO;
        calcularTributos();
    }
    public BigDecimal getValorIcms() { return valorIcms; }
    public String getCstPis() { return cstPis; }
    public void setCstPis(String cstPis) { this.cstPis = cstPis; }
    public BigDecimal getPpis() { return ppis; }
    public void setPpis(BigDecimal ppis) {
        this.ppis = ppis != null ? ppis : BigDecimal.ZERO;
        calcularTributos();
    }
    public BigDecimal getValorPis() { return valorPis; }
    public String getCstCofins() { return cstCofins; }
    public void setCstCofins(String cstCofins) { this.cstCofins = cstCofins; }
    public BigDecimal getPcofins() { return pcofins; }
    public void setPcofins(BigDecimal pcofins) {
        this.pcofins = pcofins != null ? pcofins : BigDecimal.ZERO;
        calcularTributos();
    }
    public BigDecimal getValorCofins() { return valorCofins; }
}

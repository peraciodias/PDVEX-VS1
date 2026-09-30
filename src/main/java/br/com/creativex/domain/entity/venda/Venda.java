// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.venda;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private Long idVenda;
    private Long idUsuario;
    private Long idCliente;
    private String nomeClienteAvulso;
    private String cpfAvulso;
    private String clienteNome;
    private String usuarioNome;
    private BigDecimal totalBruto = BigDecimal.ZERO;
    private BigDecimal totalDesconto = BigDecimal.ZERO;
    private BigDecimal totalLiquido = BigDecimal.ZERO;
    private BigDecimal totalTributos = BigDecimal.ZERO;

    private String status = "CONCLUIDA";
    private String metodoPagamento;
    private BigDecimal valorPago;
    private BigDecimal troco;

    private List<ItemVenda> itens = new ArrayList<>();

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================
    public void adicionarItem(ItemVenda item) {
        itens.add(item);
        recalcularTotais();
    }

    public void recalcularTotais() {
        totalBruto = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(item.getQuantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        totalDesconto = itens.stream()
                .map(ItemVenda::getDescontoItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        totalTributos = itens.stream()
                .map(ItemVenda::getTotalTributos)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        totalLiquido = totalBruto.subtract(totalDesconto).setScale(2, RoundingMode.HALF_UP);
    }
    public boolean isClienteAvulso() {
        return idCliente == null;
    }
//
public String getClienteNome() {
    return clienteNome;
}

    public String getUsuarioNome() {
        return usuarioNome;
    }
//
    public String getNomeClienteExibicao() {
        return (idCliente != null) ? null :
                (nomeClienteAvulso != null ? nomeClienteAvulso : "NÃO INFORMADO");
    }

    //regra de negócio aqui só pra facilitar a vida
    public void validarCliente() {
        if (idCliente == null && (nomeClienteAvulso == null || nomeClienteAvulso.isEmpty())) {
            throw new IllegalStateException("Venda sem identificação de cliente");
        }
    }
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }
    // =========================
    // GETTERS E SETTERS
    // =========================

    public Long getIdVenda() { return idVenda; }
    public void setIdVenda(Long idVenda) { this.idVenda = idVenda; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public String getNomeClienteAvulso() { return nomeClienteAvulso; }
    public void setNomeClienteAvulso(String nomeClienteAvulso) { this.nomeClienteAvulso = nomeClienteAvulso; }

    public String getCpfAvulso() { return cpfAvulso; }
    public void setCpfAvulso(String cpfAvulso) { this.cpfAvulso = cpfAvulso; }

    public java.math.BigDecimal getTotalBruto() { return totalBruto; }
    public void setTotalBruto(java.math.BigDecimal totalBruto) { this.totalBruto = totalBruto; }
    public java.math.BigDecimal getTotalDesconto() { return totalDesconto; }
    public void setTotalDesconto(java.math.BigDecimal totalDesconto) { this.totalDesconto = totalDesconto; }
    public java.math.BigDecimal getTotalLiquido() { return totalLiquido; }
    public void setTotalLiquido(java.math.BigDecimal totalLiquido) { this.totalLiquido = totalLiquido; }
    public java.math.BigDecimal getTotalTributos() { return totalTributos; }
    public void setTotalTributos(java.math.BigDecimal totalTributos) { this.totalTributos = totalTributos; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }
    public java.math.BigDecimal getValorPago() { return valorPago; }
    public void setValorPago(java.math.BigDecimal valorPago) { this.valorPago = valorPago; }
    public java.math.BigDecimal getTroco() { return troco; }
    public void setTroco(java.math.BigDecimal troco) { this.troco = troco; }
    public List<ItemVenda> getItens() { return itens; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; recalcularTotais(); }
}

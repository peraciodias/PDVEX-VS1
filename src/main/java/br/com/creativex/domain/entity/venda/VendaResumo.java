// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.venda;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class VendaResumo {
    private String cliente;
    private final long idVenda;
    private final Timestamp dataVenda;
    private final String operador;
    private final BigDecimal totalLiquido;
    private final String status;

    public VendaResumo(long idVenda, Timestamp dataVenda, String operador,
                       String cliente, BigDecimal totalLiquido, String status) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.operador = operador;
        this.cliente = cliente;
        this.totalLiquido = totalLiquido;
        this.status = status;
    }


    public long getIdVenda() {
        return idVenda;
    }

    public Timestamp getDataVenda() {
        return dataVenda;
    }

    public String getOperador() {
        return operador;
    }

    public BigDecimal getTotalLiquido() {
        return totalLiquido;
    }

    public String getStatus() {
        return status;
    }
    public String getCliente() {
        return cliente;
    }
}

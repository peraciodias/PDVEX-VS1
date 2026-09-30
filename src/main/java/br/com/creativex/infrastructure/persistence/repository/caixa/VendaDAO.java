// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas

package br.com.creativex.infrastructure.persistence.repository.caixa;

import br.com.creativex.db.Conexao;
import br.com.creativex.domain.entity.venda.Venda;
import br.com.creativex.domain.entity.venda.ItemVenda;

import java.math.BigDecimal;
import java.sql.*;

public class VendaDAO {

    public void finalizarVenda(Venda venda) throws SQLException {

        if (venda.getIdCliente() == null &&
                (venda.getNomeClienteAvulso() == null || venda.getNomeClienteAvulso().isBlank())) {

            venda.setNomeClienteAvulso("NÃO INFORMADO");
        }

        String sqlVenda = """
            INSERT INTO tabela_vendas 
            (id_usuario, id_cliente, nome_cliente_avulso, cpf_avulso, 
            total_bruto, total_desconto, total_liquido, total_tributos,
            metodo_pagamento, valor_pago, troco)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id_venda
            """;

        String sqlItem = """
            INSERT INTO tabela_itens_venda
            (id_venda, id_produto, quantidade, preco_unitario,
             desconto_item, subtotal,
             preco_custo_momento, cst_fiscal_momento,
             icms_aliquota_momento, icms_valor_momento,
             cst_pis_momento, pis_aliquota_momento, pis_valor_momento,
             cst_cofins_momento, cofins_aliquota_momento, cofins_valor_momento)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        String sqlSaldoAtual =
                "SELECT quantidade_estoque FROM tabela_produtos WHERE id = ? FOR UPDATE";

        String sqlBaixaEstoque =
                "UPDATE tabela_produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";

        String sqlMov = """
            INSERT INTO tabela_movimentacoes_estoque
            (id_produto, tipo, quantidade,
             saldo_anterior, saldo_posterior,
             motivo, id_usuario, id_venda_origem)
            VALUES (?, 'SAIDA', ?, ?, ?, 'VENDA', ?, ?)
        """;

        Connection conn = Conexao.getConnection();

        try {
            conn.setAutoCommit(false);

            // 1️⃣ Grava VENDA

            long idVenda;
            venda.recalcularTotais();
            BigDecimal totalTributos = venda.getTotalTributos() != null ? venda.getTotalTributos() : BigDecimal.ZERO;

            try (PreparedStatement ps = conn.prepareStatement(sqlVenda)) {
                ps.setLong(1, venda.getIdUsuario());
                
                if (venda.getIdCliente() != null) {
                    ps.setLong(2, venda.getIdCliente());
                } else {
                    ps.setNull(2, Types.BIGINT);
                }

                ps.setString(3, (venda.getNomeClienteAvulso() != null) ? venda.getNomeClienteAvulso() : "NÃO INFORMADO");
                
                if (venda.getCpfAvulso() != null) {
                    ps.setString(4, venda.getCpfAvulso());
                } else {
                    ps.setNull(4, Types.VARCHAR);
                }   

                // Alinhando com a ordem do seu SQL INSERT:
                ps.setBigDecimal(5, venda.getTotalBruto());      // total_bruto
                ps.setBigDecimal(6, venda.getTotalDesconto());   // total_desconto
                ps.setBigDecimal(7, venda.getTotalLiquido());    // total_liquido
                ps.setBigDecimal(8, totalTributos);              // total_tributos (O NOVO CAMPO)
                ps.setString(9, venda.getMetodoPagamento());     // metodo_pagamento (SUBIU PARA 9)
                ps.setBigDecimal(10, venda.getValorPago());      // valor_pago (SUBIU PARA 10)
                ps.setBigDecimal(11, venda.getTroco());          // troco (SUBIU PARA 11)

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Erro ao gerar ID da venda.");
                }
                idVenda = rs.getLong("id_venda");
            }
            // 2️⃣ Itens + estoque + movimentação
            for (ItemVenda item : venda.getItens()) {

                BigDecimal saldoAnterior;

                // trava o produto (Postgres)
                try (PreparedStatement ps = conn.prepareStatement(sqlSaldoAtual)) {
                    ps.setLong(1, item.getIdProduto());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    saldoAnterior = rs.getBigDecimal("quantidade_estoque");
                }

                BigDecimal saldoPosterior =
                        saldoAnterior.subtract(item.getQuantidade());

                // Item da venda
                try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                    ps.setLong(1, idVenda);
                    ps.setLong(2, item.getIdProduto());
                    ps.setBigDecimal(3, item.getQuantidade());
                    ps.setBigDecimal(4, item.getPrecoUnitario());
                    ps.setBigDecimal(5, item.getDescontoItem());
                    ps.setBigDecimal(6, item.getSubtotal());
                    ps.setBigDecimal(7, item.getPrecoCustoMomento());
                    ps.setString(8, item.getCstFiscalMomento());
                    ps.setBigDecimal(9, item.getAliquotaIcms());
                    ps.setBigDecimal(10, item.getValorIcms());
                    ps.setString(11, item.getCstPis());
                    ps.setBigDecimal(12, item.getPpis());
                    ps.setBigDecimal(13, item.getValorPis());
                    ps.setString(14, item.getCstCofins());
                    ps.setBigDecimal(15, item.getPcofins());
                    ps.setBigDecimal(16, item.getValorCofins());
                    ps.executeUpdate();
                }

                // Baixa estoque
                try (PreparedStatement ps = conn.prepareStatement(sqlBaixaEstoque)) {
                    ps.setBigDecimal(1, item.getQuantidade());
                    ps.setLong(2, item.getIdProduto());
                    ps.executeUpdate();
                }

                // Movimentação
                try (PreparedStatement ps = conn.prepareStatement(sqlMov)) {
                    ps.setLong(1, item.getIdProduto());
                    ps.setBigDecimal(2, item.getQuantidade());
                    ps.setBigDecimal(3, saldoAnterior);
                    ps.setBigDecimal(4, saldoPosterior);
                    ps.setLong(5, venda.getIdUsuario());
                    ps.setLong(6, idVenda);
                    ps.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    //
    // Cancelamento de venda
    //
    public void cancelarVenda(long idVenda, long idUsuario) throws SQLException {

        String sqlBuscaItens = """
            SELECT id_produto, quantidade
            FROM tabela_itens_venda
            WHERE id_venda = ?
        """;

        String sqlAtualizaStatus = """
            UPDATE tabela_vendas
            SET status = 'CANCELADA'
            WHERE id_venda = ?
        """;

        String sqlSaldoAtual =
                "SELECT quantidade_estoque FROM tabela_produtos WHERE id = ? FOR UPDATE";

        String sqlAtualizaEstoque =
                "UPDATE tabela_produtos SET quantidade_estoque = quantidade_estoque + ? WHERE id = ?";

        String sqlMov = """
            INSERT INTO tabela_movimentacoes_estoque
            (id_produto, tipo, quantidade,
             saldo_anterior, saldo_posterior,
             motivo, id_usuario, id_venda_origem)
            VALUES (?, 'ENTRADA', ?, ?, ?, 'ESTORNO VENDA', ?, ?)
        """;

        Connection conn = Conexao.getConnection();

        try {
            conn.setAutoCommit(false);

            // 1️⃣ Atualiza status
            try (PreparedStatement ps = conn.prepareStatement(sqlAtualizaStatus)) {
                ps.setLong(1, idVenda);
                ps.executeUpdate();
            }

            // 2️⃣ Busca itens da venda
            try (PreparedStatement psItens = conn.prepareStatement(sqlBuscaItens)) {
                psItens.setLong(1, idVenda);

                ResultSet rsItens = psItens.executeQuery();

                while (rsItens.next()) {

                    long idProduto = rsItens.getLong("id_produto");
                    BigDecimal qtd = rsItens.getBigDecimal("quantidade");

                    BigDecimal saldoAnterior;

                    // 🔒 Lock do produto
                    try (PreparedStatement psSaldo = conn.prepareStatement(sqlSaldoAtual)) {
                        psSaldo.setLong(1, idProduto);
                        ResultSet rs = psSaldo.executeQuery();
                        rs.next();
                        saldoAnterior = rs.getBigDecimal("quantidade_estoque");
                    }

                    BigDecimal saldoPosterior = saldoAnterior.add(qtd);

                    // 3️⃣ Devolve estoque
                    try (PreparedStatement psEstoque = conn.prepareStatement(sqlAtualizaEstoque)) {
                        psEstoque.setBigDecimal(1, qtd);
                        psEstoque.setLong(2, idProduto);
                        psEstoque.executeUpdate();
                    }

                    // 4️⃣ Registra movimentação reversa
                    try (PreparedStatement psMov = conn.prepareStatement(sqlMov)) {
                        psMov.setLong(1, idProduto);
                        psMov.setBigDecimal(2, qtd);
                        psMov.setBigDecimal(3, saldoAnterior);
                        psMov.setBigDecimal(4, saldoPosterior);
                        psMov.setLong(5, idUsuario);
                        psMov.setLong(6, idVenda);
                        psMov.executeUpdate();
                    }
                }
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }


}

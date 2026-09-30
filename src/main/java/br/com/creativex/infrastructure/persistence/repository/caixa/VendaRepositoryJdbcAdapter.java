// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.caixa;

import br.com.creativex.domain.entity.venda.Venda;
import br.com.creativex.domain.repository.VendaRepository;
import br.com.creativex.db.Conexao;
import br.com.creativex.domain.entity.venda.ItemVenda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendaRepositoryJdbcAdapter implements VendaRepository {

    private final VendaDAO vendaDAO;

    public VendaRepositoryJdbcAdapter(VendaDAO vendaDAO) {
        this.vendaDAO = vendaDAO;
    }

    @Override
    public void finalizarVenda(Venda venda) {
        try {
            vendaDAO.finalizarVenda(venda);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao finalizar venda", e);
        }
    }

    @Override
    public void cancelarVenda(long idVenda, long idUsuario) {
        try {
            vendaDAO.cancelarVenda(idVenda, idUsuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cancelar venda", e);
        }
    }
    @Override
    public Venda buscarPorIdComItens(long idVenda) {

        String sqlVenda = """
        SELECT v.id_venda,
               u.nome AS usuario,
               COALESCE(c.nome, v.nome_cliente_avulso, 'NÃO INFORMADO') AS cliente,
               v.total_liquido,
               v.total_tributos
        FROM tabela_vendas v
        JOIN tabela_usuarios u ON v.id_usuario = u.id
        LEFT JOIN tabela_clientes c ON c.id = v.id_cliente
        WHERE v.id_venda = ?
    """;

        String sqlItens = """
        SELECT i.id_produto,
               p.codigo_barra,
               p.descricao AS produto_descricao,
               i.quantidade,
               i.preco_unitario,
               i.desconto_item,
               i.preco_custo_momento,
               i.cst_fiscal_momento,
               i.icms_aliquota_momento,
               i.icms_valor_momento,
               i.cst_pis_momento,
               i.pis_aliquota_momento,
               i.pis_valor_momento,
               i.cst_cofins_momento,
               i.cofins_aliquota_momento,
               i.cofins_valor_momento
        FROM tabela_itens_venda i
        LEFT JOIN tabela_produtos p ON p.id = i.id_produto
        WHERE id_venda = ?
    """;

        try (Connection conn = Conexao.getConnection()) {

            Venda venda = null;

            // 🔹 VENDA
            try (PreparedStatement ps = conn.prepareStatement(sqlVenda)) {
                ps.setLong(1, idVenda);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    venda = new Venda();

                    venda.setIdVenda(rs.getLong("id_venda")); // 🔥 CORRETO
                    venda.setTotalLiquido(rs.getBigDecimal("total_liquido"));
                    venda.setTotalTributos(rs.getBigDecimal("total_tributos"));

                    venda.setClienteNome(rs.getString("cliente"));
                    venda.setUsuarioNome(rs.getString("usuario"));
                }
            }

            // 🔹 ITENS
            if (venda != null) {
                try (PreparedStatement ps = conn.prepareStatement(sqlItens)) {
                    ps.setLong(1, idVenda);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        ItemVenda item = new ItemVenda(
                                rs.getLong("id_produto"),
                                rs.getString("produto_descricao"),
                                rs.getBigDecimal("quantidade"),
                                rs.getBigDecimal("preco_unitario")
                        );
                        item.setCodigoBarra(rs.getString("codigo_barra"));

                        item.setDescontoItem(rs.getBigDecimal("desconto_item"));
                        item.setPrecoCustoMomento(rs.getBigDecimal("preco_custo_momento"));
                        item.setCstFiscalMomento(rs.getString("cst_fiscal_momento"));
                        item.setAliquotaIcms(rs.getBigDecimal("icms_aliquota_momento"));
                        item.setPpis(rs.getBigDecimal("pis_aliquota_momento"));
                        item.setCstPis(rs.getString("cst_pis_momento"));
                        item.setCstCofins(rs.getString("cst_cofins_momento"));
                        item.setPcofins(rs.getBigDecimal("cofins_aliquota_momento"));
                        venda.getItens().add(item);
                    }
                }
                venda.recalcularTotais();
            }

            return venda;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar venda", e);
        }
    }
}

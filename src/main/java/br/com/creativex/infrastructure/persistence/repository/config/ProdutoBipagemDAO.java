// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.config;

import br.com.creativex.domain.entity.config.ProdutoBipagem;
import br.com.creativex.domain.transaction.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para consultar a view vw_pdv_bipagem.
 * Fornece acesso aos dados de produtos com cálculos tributários pré-calculados.
 *
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-05-09
 */
public class ProdutoBipagemDAO {
    private final Transaction tx;

    public ProdutoBipagemDAO(Transaction tx) {
        this.tx = tx;
    }

    /**
     * Busca um produto pela view vw_pdv_bipagem pelo código de barras.
     * Retorna os dados com alíquota e imposto já calculados.
     *
     * @param codigoBarra o código de barras do produto
     * @return ProdutoBipagem com dados tributários, ou null se não encontrado
     */
    public ProdutoBipagem buscarPorCodigoBarra(String codigoBarra) {
        return tx.execute(conn -> {
            String sql = "SELECT * FROM public.vw_pdv_bipagem WHERE codigo_barra = ? LIMIT 1";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, codigoBarra);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapear(rs);
                    }
                }
            }
            return null;
        });
    }

    /**
     * Busca um produto pela view vw_pdv_bipagem pelo ID.
     *
     * @param id o ID do produto
     * @return ProdutoBipagem com dados tributários, ou null se não encontrado
     */
    public ProdutoBipagem buscarPorId(Long id) {
        return tx.execute(conn -> {
            String sql = "SELECT * FROM public.vw_pdv_bipagem WHERE id = ? LIMIT 1";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapear(rs);
                    }
                }
            }
            return null;
        });
    }

    /**
     * Lista todos os produtos com os cálculos tributários da view.
     * Útil para relatórios e sincronização de dados.
     *
     * @return Lista de todos os produtos com dados tributários
     */
    public List<ProdutoBipagem> listarTodos() {
        return tx.execute(conn -> {
            String sql = "SELECT * FROM public.vw_pdv_bipagem ORDER BY codigo_barra";
            List<ProdutoBipagem> lista = new ArrayList<>();

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        });
    }

    /**
     * Lista produtos pela view com filtro por descrição.
     * Útil para buscar produtos em tempo real durante vendas.
     *
     * @param filtroDescricao parte da descrição para buscar (ILIKE)
     * @return Lista de produtos encontrados com dados tributários
     */
    public List<ProdutoBipagem> listarPorDescricao(String filtroDescricao) {
        return tx.execute(conn -> {
            String sql = "SELECT * FROM public.vw_pdv_bipagem WHERE descricao ILIKE ? ORDER BY descricao LIMIT 100";
            List<ProdutoBipagem> lista = new ArrayList<>();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + filtroDescricao + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapear(rs));
                    }
                }
            }
            return lista;
        });
    }

    /**
     * Lista produtos com estoque disponível (quantidade > 0).
     * Garante que apenas produtos com estoque apareçam.
     *
     * @return Lista de produtos com estoque
     */
    public List<ProdutoBipagem> listarComEstoque() {
        return tx.execute(conn -> {
            String sql = "SELECT * FROM public.vw_pdv_bipagem WHERE quantidade_estoque > 0 ORDER BY descricao";
            List<ProdutoBipagem> lista = new ArrayList<>();

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        });
    }

    /**
     * Mapeia um ResultSet da view para um objeto ProdutoBipagem.
     *
     * @param rs ResultSet contendo os dados da view vw_pdv_bipagem
     * @return ProdutoBipagem mapeado com todos os valores
     * @throws SQLException se houver erro ao ler os dados
     */
    private ProdutoBipagem mapear(ResultSet rs) throws SQLException {
        ProdutoBipagem pb = new ProdutoBipagem();

        pb.setId(rs.getLong("id"));
        pb.setCodigoBarra(rs.getString("codigo_barra"));
        pb.setDescricao(rs.getString("descricao"));
        pb.setMarca(rs.getString("marca"));
        pb.setPrecoVenda(rs.getBigDecimal("preco_venda"));
        pb.setQuantidadeEstoque(rs.getBigDecimal("quantidade_estoque"));
        pb.setCstIcms(rs.getString("cst_icms"));
        pb.setPrecoCusto(rs.getBigDecimal("preco_custo"));
        pb.setAliquotaAplicada(rs.getBigDecimal("aliquota_aplicada"));
        pb.setValorImpostoItem(rs.getBigDecimal("valor_imposto_item"));

        return pb;
    }
}

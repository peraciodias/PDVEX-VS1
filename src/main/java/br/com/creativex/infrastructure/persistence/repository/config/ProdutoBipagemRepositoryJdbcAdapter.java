// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.config;

import br.com.creativex.domain.entity.config.ProdutoBipagem;
import br.com.creativex.domain.repository.ProdutoBipagemRepository;
import br.com.creativex.domain.transaction.Transaction;

import java.util.List;

/**
 * Implementação JDBC do repositório de ProdutoBipagem.
 * Adapta o DAO para a interface do repositório do domínio.
 *
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-05-09
 */
public class ProdutoBipagemRepositoryJdbcAdapter implements ProdutoBipagemRepository {

    private final ProdutoBipagemDAO dao;

    public ProdutoBipagemRepositoryJdbcAdapter(Transaction tx) {
        this.dao = new ProdutoBipagemDAO(tx);
    }

    @Override
    public ProdutoBipagem buscarPorCodigoBarra(String codigoBarra) {
        return dao.buscarPorCodigoBarra(codigoBarra);
    }

    @Override
    public ProdutoBipagem buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<ProdutoBipagem> listarTodos() {
        return dao.listarTodos();
    }

    @Override
    public List<ProdutoBipagem> listarPorDescricao(String filtroDescricao) {
        return dao.listarPorDescricao(filtroDescricao);
    }

    @Override
    public List<ProdutoBipagem> listarComEstoque() {
        return dao.listarComEstoque();
    }
}

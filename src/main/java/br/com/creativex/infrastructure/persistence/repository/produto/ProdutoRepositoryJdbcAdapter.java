// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.produto;

import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.domain.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepositoryJdbcAdapter implements ProdutoRepository {

    private final ProdutoDAO produtoDAO;

    public ProdutoRepositoryJdbcAdapter(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    @Override
    public Optional<Produto> findById(long id) {
        Produto p = produtoDAO.buscarPorId(id);
        return p == null ? Optional.empty() : Optional.of(p);
    }

    @Override
    public Optional<Produto> findByCodigoBarra(String codigo) {
        Produto p = produtoDAO.buscarPorCodigoBarra(codigo);
        return p == null ? Optional.empty() : Optional.of(p);
    }

    @Override
    public Optional<Produto> findByNome(String nome) {
        Produto p = produtoDAO.buscarPorNome(nome);
        return p == null ? Optional.empty() : Optional.of(p);
    }

    @Override
    public List<Produto> findByDescricao(String filtro, int limite) {
        return produtoDAO.listarPorDescricao(filtro, limite);
    }

    @Override
    public Optional<Produto> findLast() {
        Produto p = produtoDAO.buscarUltimo();
        return p == null ? Optional.empty() : Optional.of(p);
    }

    @Override
    public List<Produto> findAll() {
        return new ArrayList<>(produtoDAO.listarPorIdLimite(0, 1000));
    }

    @Override
    public List<Produto> findByIdLimit(long idInicial, int limite) {
        return new ArrayList<>(produtoDAO.listarPorIdLimite(idInicial, limite));
    }

    @Override
    public Produto save(Produto produto) {
        if (produto.getId() <= 0) {
            produtoDAO.inserir(produto);
        } else {
            produtoDAO.atualizar(produto);
        }
        return produto;
    }

    @Override
    public void deleteById(long id) {
        produtoDAO.excluir(id);
    }
}

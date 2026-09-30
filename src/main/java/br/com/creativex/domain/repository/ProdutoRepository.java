// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.repository;

import br.com.creativex.domain.entity.produto.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {
    Optional<Produto> findById(long id);
    List<Produto> findAll();
    Produto save(Produto produto);
    void deleteById(long id);
    Optional<Produto> findByCodigoBarra(String codigo);
    Optional<Produto> findByNome(String nome);
    List<Produto> findByDescricao(String filtro, int limite);
    Optional<Produto> findLast();
    List<Produto> findByIdLimit(long idInicial, int limite);
}

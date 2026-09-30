// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.application.produto;

import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.domain.repository.ProdutoRepository;

public class CreateProdutoUseCase {

    private final ProdutoRepository produtoRepository;

    public CreateProdutoUseCase(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto execute(Produto produto) {
        // validações simples de domínio podem ficar aqui
        return produtoRepository.save(produto);
    }
}

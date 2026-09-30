// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.presentation.controller;

import br.com.creativex.application.produto.CreateProdutoUseCase;
import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.domain.repository.ProdutoRepository;

import java.util.List;

public class ProdutoController {

    private final CreateProdutoUseCase createProdutoUseCase;
    private final ProdutoRepository produtoRepository;

    public ProdutoController(CreateProdutoUseCase createProdutoUseCase, ProdutoRepository produtoRepository) {
        this.createProdutoUseCase = createProdutoUseCase;
        this.produtoRepository = produtoRepository;
    }

    public Produto criarProduto(Produto produto) {
        return createProdutoUseCase.execute(produto);
    }

    public Produto buscarUltimo() {
        return produtoRepository.findLast().orElse(null);
    }

    public Produto buscarPorId(long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public Produto buscarPorNome(String nome) {
        return produtoRepository.findByNome(nome).orElse(null);
    }

    public List<Produto> listarPorDescricao(String filtro, int limite) {
        return produtoRepository.findByDescricao(filtro, limite);
    }

    public Produto buscarPorCodigoBarra(String codigo) {
        return produtoRepository.findByCodigoBarra(codigo).orElse(null);
    }

    public Produto buscarPorCodigoOuId(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return null;
        }

        Produto produto = buscarPorCodigoBarra(filtro.trim());
        if (produto != null) {
            return produto;
        }

        if (filtro.trim().matches("\\d+")) {
            try {
                return buscarPorId(Long.parseLong(filtro.trim()));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    public List<Produto> listarPorIdLimite(long idInicial, int limite) {
        return produtoRepository.findByIdLimit(idInicial, limite);
    }

    public void inserir(Produto p) {
        criarProduto(p);
    }

    public void atualizar(Produto p) {
        produtoRepository.save(p);
    }

    public void excluir(long id) {
        produtoRepository.deleteById(id);
    }
}

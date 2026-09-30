// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.application.config;

import br.com.creativex.domain.entity.config.ProdutoBipagem;
import br.com.creativex.domain.repository.ProdutoBipagemRepository;

import java.util.List;

/**
 * Use Case para consultar dados tributários de produtos da view vw_pdv_bipagem.
 * Encapsula a lógica de negócio para buscar produtos com cálculos já realizados.
 *
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-05-09
 */
public class ConsultarTributoProdutoUseCase {

    private final ProdutoBipagemRepository repository;

    public ConsultarTributoProdutoUseCase(ProdutoBipagemRepository repository) {
        this.repository = repository;
    }

    /**
     * Consulta um produto com dados tributários pelo código de barras.
     * Utiliza a view vw_pdv_bipagem que já faz o LEFT JOIN com config_tributaria.
     *
     * @param codigoBarra o código de barras do produto
     * @return ProdutoBipagem com alíquota e imposto calculados, ou null
     */
    public ProdutoBipagem buscarPorCodigoBarra(String codigoBarra) {
        if (codigoBarra == null || codigoBarra.isBlank()) {
            throw new IllegalArgumentException("Código de barras não pode ser vazio");
        }
        return repository.buscarPorCodigoBarra(codigoBarra);
    }

    /**
     * Consulta um produto com dados tributários pelo ID.
     *
     * @param id o ID do produto
     * @return ProdutoBipagem com alíquota e imposto calculados, ou null
     */
    public ProdutoBipagem buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do produto inválido");
        }
        return repository.buscarPorId(id);
    }

    /**
     * Lista todos os produtos com dados tributários.
     * Use com cuidado se houver muitos produtos (implementar paginação se necessário).
     *
     * @return lista de ProdutoBipagem
     */
    public List<ProdutoBipagem> listarTodos() {
        return repository.listarTodos();
    }

    /**
     * Busca produtos por descrição com dados tributários.
     * Útil para buscas em tempo real durante operações de caixa.
     *
     * @param descricao parte da descrição a buscar
     * @return lista de ProdutoBipagem encontrados
     */
    public List<ProdutoBipagem> buscarPorDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia");
        }
        return repository.listarPorDescricao(descricao);
    }

    /**
     * Lista apenas produtos com estoque disponível.
     * Garante que o operador de caixa só veja produtos vendíveis.
     *
     * @return lista de ProdutoBipagem com quantidade_estoque > 0
     */
    public List<ProdutoBipagem> listarComEstoque() {
        return repository.listarComEstoque();
    }
}

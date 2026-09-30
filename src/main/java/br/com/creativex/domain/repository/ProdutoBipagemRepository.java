// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.repository;

import br.com.creativex.domain.entity.config.ProdutoBipagem;

import java.util.List;

/**
 * Interface do repositório para consultar dados da view vw_pdv_bipagem.
 * Define o contrato para acessar produtos com cálculos tributários.
 *
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-05-09
 */
public interface ProdutoBipagemRepository {

    /**
     * Busca um produto pela view pelo código de barras.
     *
     * @param codigoBarra o código de barras
     * @return ProdutoBipagem ou null se não encontrado
     */
    ProdutoBipagem buscarPorCodigoBarra(String codigoBarra);

    /**
     * Busca um produto pela view pelo ID.
     *
     * @param id o ID do produto
     * @return ProdutoBipagem ou null se não encontrado
     */
    ProdutoBipagem buscarPorId(Long id);

    /**
     * Lista todos os produtos da view com dados tributários.
     *
     * @return lista de ProdutoBipagem
     */
    List<ProdutoBipagem> listarTodos();

    /**
     * Lista produtos da view filtrados por descrição.
     *
     * @param filtroDescricao parte da descrição a buscar
     * @return lista de ProdutoBipagem encontrados
     */
    List<ProdutoBipagem> listarPorDescricao(String filtroDescricao);

    /**
     * Lista apenas produtos com estoque disponível.
     *
     * @return lista de ProdutoBipagem com estoque > 0
     */
    List<ProdutoBipagem> listarComEstoque();
}

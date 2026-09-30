// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.application.caixa;

import br.com.creativex.domain.entity.venda.Venda;
import br.com.creativex.domain.entity.venda.ItemVenda;
import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.domain.repository.ProdutoRepository;
import br.com.creativex.domain.repository.VendaRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Caso de uso para finalizar uma venda.
 * 
 * Implementa a regra de negócio para conclusão de uma transação de venda,
 * realizando validações necessárias e persistindo os dados através do
 * repositório de vendas. Segue o padrão Clean Architecture para separação
 * de responsabilidades.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public class FinalizeVendaUseCase {

    /** Repositório para persistência de vendas */
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;

    /**
     * Construtor que inicializa o caso de uso com o repositório de vendas.
     * 
     * @param vendaRepository Repositório para operações de venda
     */
    public FinalizeVendaUseCase(VendaRepository vendaRepository, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
    }

    /**
     * Executa o caso de uso de finalização de venda.
     * 
     * Valida os dados críticos da venda e a persiste no repositório. Este é o
     * ponto de entrada para a regra de negócio de conclusão de uma transação.
     * 
     * @param venda A venda a ser finalizada
     */
    public void execute(Venda venda) {
        validarVenda(venda);
        venda.recalcularTotais();
        vendaRepository.finalizarVenda(venda);
    }

    private void validarVenda(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("Venda não informada.");
        }

        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Venda sem itens.");
        }

        if (venda.getIdUsuario() == null || venda.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("Usuário da venda não informado.");
        }

        if (venda.getMetodoPagamento() == null || venda.getMetodoPagamento().isBlank()) {
            throw new IllegalArgumentException("Método de pagamento não informado.");
        }

        validarItens(venda);
        validarEstoque(venda);
        venda.recalcularTotais();
        validarPagamento(venda);
    }

    private void validarItens(Venda venda) {
        int sequencia = 1;
        for (ItemVenda item : venda.getItens()) {
            if (item == null) {
                throw new IllegalArgumentException("Item " + sequencia + " não informado.");
            }

            if (item.getIdProduto() == null || item.getIdProduto() <= 0) {
                throw new IllegalArgumentException("Produto do item " + sequencia + " não informado.");
            }

            if (item.getQuantidade() == null || item.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantidade inválida no item " + sequencia + ".");
            }

            if (item.getPrecoUnitario() == null || item.getPrecoUnitario().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Preço inválido no item " + sequencia + ".");
            }

            if (item.getDescontoItem() != null && item.getDescontoItem().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Desconto inválido no item " + sequencia + ".");
            }

            sequencia++;
        }
    }

    private void validarEstoque(Venda venda) {
        Map<Long, BigDecimal> quantidadePorProduto = new HashMap<>();
        for (ItemVenda item : venda.getItens()) {
            quantidadePorProduto.merge(item.getIdProduto(), item.getQuantidade(), BigDecimal::add);
        }

        for (Map.Entry<Long, BigDecimal> entry : quantidadePorProduto.entrySet()) {
            long idProduto = entry.getKey();
            BigDecimal quantidadeVendida = entry.getValue();

            Produto produto = produtoRepository.findById(idProduto)
                    .orElseThrow(() -> new IllegalArgumentException("Produto " + idProduto + " não encontrado."));

            BigDecimal estoqueDisponivel = produto.getQuantidadeEstoque() != null
                    ? produto.getQuantidadeEstoque()
                    : BigDecimal.ZERO;

            if (estoqueDisponivel.compareTo(quantidadeVendida) < 0) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para o produto " + idProduto + ". Disponível: " + estoqueDisponivel + "."
                );
            }
        }
    }

    private void validarPagamento(Venda venda) {
        if (venda.getValorPago() == null) {
            throw new IllegalArgumentException("Valor pago não informado.");
        }

        if (venda.getValorPago().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor pago não pode ser negativo.");
        }

        if (venda.getValorPago().compareTo(venda.getTotalLiquido()) < 0) {
            throw new IllegalArgumentException("Valor pago é menor que o total da venda.");
        }

        if (venda.getTroco() != null && venda.getTroco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Troco não pode ser negativo.");
        }
    }
}

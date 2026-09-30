// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.presentation.controller;

import br.com.creativex.application.caixa.FinalizeVendaUseCase;
import br.com.creativex.domain.entity.venda.Venda;
import br.com.creativex.domain.repository.VendaRepository;

public class CaixaController {

    private final FinalizeVendaUseCase finalizeVendaUseCase;
    private final VendaRepository vendaRepository;

    public CaixaController(FinalizeVendaUseCase finalizeVendaUseCase, VendaRepository vendaRepository) {
        this.finalizeVendaUseCase = finalizeVendaUseCase;
        this.vendaRepository = vendaRepository;
    }
    public Venda buscarVendaPorId(long idVenda) {
        return vendaRepository.buscarPorIdComItens(idVenda);
    }
    public void finalizarVenda(Venda venda) {
        finalizeVendaUseCase.execute(venda);
    }

    public void cancelarVenda(long idVenda, long idUsuario) {
        vendaRepository.cancelarVenda(idVenda, idUsuario);
    }
}

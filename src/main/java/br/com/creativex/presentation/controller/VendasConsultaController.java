// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.presentation.controller;

import br.com.creativex.application.caixa.ListarVendasUseCase;
import br.com.creativex.domain.entity.venda.VendaResumo;

import java.util.List;

public class VendasConsultaController {

    private final CaixaController caixaController;
    private final ListarVendasUseCase listarVendasUseCase;

    public VendasConsultaController(CaixaController caixaController, ListarVendasUseCase listarVendasUseCase) {
        this.caixaController = caixaController;
        this.listarVendasUseCase = listarVendasUseCase;
    }

    public List<VendaResumo> listarVendas() {
        return listarVendasUseCase.execute();
    }

    public void cancelarVenda(long idVenda, long idUsuario) {
        caixaController.cancelarVenda(idVenda, idUsuario);
    }
}

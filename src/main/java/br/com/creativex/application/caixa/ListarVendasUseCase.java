// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/




package br.com.creativex.application.caixa;

import br.com.creativex.domain.entity.venda.VendaResumo;
import br.com.creativex.domain.repository.VendaConsultaRepository;

import java.util.List;

public class ListarVendasUseCase {

    private final VendaConsultaRepository repository;

    public ListarVendasUseCase(VendaConsultaRepository repository) {
        this.repository = repository;
    }

    public List<VendaResumo> execute() {
        return repository.listarVendas();
    }
}

// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.repository;

import br.com.creativex.domain.entity.venda.Venda;

public interface VendaRepository {

    void finalizarVenda(Venda venda);

    void cancelarVenda(long idVenda, long idUsuario);

    // 🔥 NOVO MÉTODO
    Venda buscarPorIdComItens(long idVenda);
}
// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.repository;

import br.com.creativex.domain.entity.clientepj.Clientepj;

import java.util.List;

public interface ClientepjRepository {
    Clientepj save(Clientepj c);
    void deleteById(long id);
    Clientepj findById(long id);
    Clientepj findByCnpj(String cnpj);
    List<Clientepj> findByRazaoSocial(String nome);
    List<Clientepj> findByIdLimit(int inicio, int limite);
}

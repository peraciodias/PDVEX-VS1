// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.presentation.controller;

import br.com.creativex.application.clientepj.ClientepjUseCases;
import br.com.creativex.domain.entity.clientepj.Clientepj;
import br.com.creativex.domain.repository.ClientepjRepository;

import java.util.List;

public class ClientepjController {

    private final ClientepjUseCases useCases;

    public ClientepjController(ClientepjRepository repository) {
        this.useCases = new ClientepjUseCases(repository);
    }

    public Clientepj save(Clientepj c) { return useCases.save(c); }
    public Clientepj findById(long id) { return useCases.findById(id); }
    public Clientepj findByCnpj(String cnpj) { return useCases.findByCnpj(cnpj); }
    public List<Clientepj> findByRazaoSocial(String nome) { return useCases.findByRazaoSocial(nome); }
    public List<Clientepj> listByIdLimit(int inicio, int limite) { return useCases.listByIdLimit(inicio, limite); }
}

// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.application.clientepj;

import br.com.creativex.domain.entity.clientepj.Clientepj;
import br.com.creativex.domain.repository.ClientepjRepository;

import java.util.List;

public class ClientepjUseCases {

    private final ClientepjRepository repo;

    public ClientepjUseCases(ClientepjRepository repo) { this.repo = repo; }

    public Clientepj save(Clientepj c) { return repo.save(c); }
    public Clientepj findById(long id) { return repo.findById(id); }
    public Clientepj findByCnpj(String cnpj) { return repo.findByCnpj(cnpj); }
    public List<Clientepj> findByRazaoSocial(String nome) { return repo.findByRazaoSocial(nome); }
    public List<Clientepj> listByIdLimit(int inicio, int limite) { return repo.findByIdLimit(inicio, limite); }
}

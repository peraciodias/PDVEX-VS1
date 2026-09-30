// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
// creativex sistemas
package br.com.creativex.application.cliente;

import br.com.creativex.domain.entity.cliente.Cliente;
import br.com.creativex.domain.repository.ClienteRepository;

import java.util.List;

public class ClienteUseCases {

    private final ClienteRepository repo;

    public ClienteUseCases(ClienteRepository repo) {
        this.repo = repo;
    }

    public Cliente save(Cliente cliente) {
        return repo.save(cliente);
    }

    public Cliente findById(long id) {
        return repo.findById(id);
    }

    public Cliente findByCpf(String cpf) {
        return repo.findByCpf(cpf);
    }

    public List<Cliente> findByName(String nome) {
        return repo.findByName(nome);
    }

    public List<Cliente> listByIdLimit(long idInicial, int limite) {
        return repo.findByIdLimit(idInicial, limite);
    }
}

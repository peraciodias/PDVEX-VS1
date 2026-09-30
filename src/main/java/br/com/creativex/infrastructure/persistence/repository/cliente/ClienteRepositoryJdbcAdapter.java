// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.cliente;

import br.com.creativex.domain.entity.cliente.Cliente;
import br.com.creativex.domain.repository.ClienteRepository;

import java.util.List;

public class ClienteRepositoryJdbcAdapter implements ClienteRepository {

    private final ClienteDAO clienteDAO;

    public ClienteRepositoryJdbcAdapter(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Cliente save(Cliente cliente) {
        try {
            if (cliente.getId() == null) {
                clienteDAO.inserir(cliente);
            } else {
                clienteDAO.atualizar(cliente);
            }
            return cliente;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar cliente", e);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            clienteDAO.excluir(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir cliente", e);
        }
    }

    @Override
    public Cliente findById(long id) {
        try {
            return clienteDAO.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente por ID", e);
        }
    }

    @Override
    public Cliente findByCpf(String cpf) {
        try {
            return clienteDAO.buscarPorCpf(cpf);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente por CPF", e);
        }
    }

    @Override
    public List<Cliente> findByName(String nome) {
        try {
            return clienteDAO.buscarPorNome(nome);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente por nome", e);
        }
    }

    @Override
    public List<Cliente> findByIdLimit(long idInicial, int limite) {
        try {
            return clienteDAO.listarPorIdLimite(idInicial, limite);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar clientes", e);
        }
    }
}

// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.repository;

import br.com.creativex.domain.entity.cliente.Cliente;
import java.util.List;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    void deleteById(long id);
    Cliente findById(long id);
    Cliente findByCpf(String cpf);
    List<Cliente> findByName(String nome);
    List<Cliente> findByIdLimit(long idInicial, int limite);
}

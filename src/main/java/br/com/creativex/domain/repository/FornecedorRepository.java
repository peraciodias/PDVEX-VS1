// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.repository;

import br.com.creativex.domain.entity.fornecedor.Fornecedor;

import java.util.List;

public interface FornecedorRepository {

    Fornecedor save(Fornecedor fornecedor);

    void deleteById(long id);

    Fornecedor findById(long id);

    Fornecedor findByCnpj(String cnpj);

    List<Fornecedor> findByRazaoSocial(String razaoSocial);

    List<Fornecedor> findByIdLimit(long idInicial, int limite);
}

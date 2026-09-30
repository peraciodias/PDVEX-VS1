// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.application.fornecedor;

import br.com.creativex.domain.entity.fornecedor.Fornecedor;
import br.com.creativex.domain.repository.FornecedorRepository;

import java.util.List;

public class FornecedorUseCases {

    private final FornecedorRepository repo;

    public FornecedorUseCases(FornecedorRepository repo) {
        this.repo = repo;
    }

    public Fornecedor save(Fornecedor f) { return repo.save(f); }
    public Fornecedor findById(long id) { return repo.findById(id); }
    public Fornecedor findByCnpj(String cnpj) { return repo.findByCnpj(cnpj); }
    public List<Fornecedor> findByRazaoSocial(String nome) { return repo.findByRazaoSocial(nome); }
    public List<Fornecedor> listByIdLimit(long idInicial, int limite) { return repo.findByIdLimit(idInicial, limite); }
}

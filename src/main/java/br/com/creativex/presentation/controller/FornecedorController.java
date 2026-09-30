// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.presentation.controller;

import br.com.creativex.application.fornecedor.FornecedorUseCases;
import br.com.creativex.domain.entity.fornecedor.Fornecedor;
import br.com.creativex.domain.repository.FornecedorRepository;

import java.util.List;

public class FornecedorController {

    private final FornecedorUseCases useCases;

    public FornecedorController(FornecedorRepository repository) {
        this.useCases = new FornecedorUseCases(repository);
    }

    public Fornecedor save(Fornecedor f) { return useCases.save(f); }
    public Fornecedor findById(long id) { return useCases.findById(id); }
    public Fornecedor findByCnpj(String cnpj) { return useCases.findByCnpj(cnpj); }
    public List<Fornecedor> findByRazaoSocial(String nome) { return useCases.findByRazaoSocial(nome); }
    public List<Fornecedor> listByIdLimit(long idInicial, int limite) { return useCases.listByIdLimit(idInicial, limite); }
}

// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.fornecedor;

import br.com.creativex.domain.entity.fornecedor.Fornecedor;
import br.com.creativex.domain.repository.FornecedorRepository;

import java.util.List;

public class FornecedorRepositoryJdbcAdapter implements FornecedorRepository {

    private final FornecedorDAO dao;

    public FornecedorRepositoryJdbcAdapter(FornecedorDAO dao) {
        this.dao = dao;
    }

    @Override
    public Fornecedor save(Fornecedor fornecedor) {
        try {
            if (fornecedor.getId() == null) {
                dao.inserir(fornecedor);
            } else {
                dao.atualizar(fornecedor);
            }
            return fornecedor;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar fornecedor", e);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            dao.excluir(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir fornecedor", e);
        }
    }

    @Override
    public Fornecedor findById(long id) {
        try {
            return dao.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar fornecedor por ID", e);
        }
    }

    @Override
    public Fornecedor findByCnpj(String cnpj) {
        try {
            return dao.buscarPorCnpj(cnpj);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar fornecedor por CNPJ", e);
        }
    }

    @Override
    public List<Fornecedor> findByRazaoSocial(String razaoSocial) {
        try {
            return dao.buscarPorRazaoSocial(razaoSocial);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar fornecedor por razao social", e);
        }
    }

    @Override
    public List<Fornecedor> findByIdLimit(long idInicial, int limite) {
        try {
            return dao.listarPorIdLimite(idInicial, limite);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar fornecedores", e);
        }
    }
}

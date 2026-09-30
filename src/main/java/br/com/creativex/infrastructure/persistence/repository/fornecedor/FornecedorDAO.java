// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.fornecedor;

import br.com.creativex.db.Conexao;
import br.com.creativex.domain.entity.fornecedor.Fornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public void inserir(Fornecedor f) throws SQLException {

        String sql = """
            INSERT INTO tabela_fornecedores (
                razao_social, nome_fantasia, cnpj, ie,
                contato, telefone, email,
                endereco, numero, complemento, bairro, cidade, uf, cep,
                limite_credito
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (existeCnpj(f.getCnpj())) {
                throw new SQLException("Já existe fornecedor cadastrado com este CNPJ.");
            }

            stmt.setString(1, f.getRazaoSocial());
            stmt.setString(2, f.getNomeFantasia());
            stmt.setString(3, f.getCnpj());
            stmt.setString(4, f.getIe());
            stmt.setString(5, f.getContato());
            stmt.setString(6, f.getTelefone());
            stmt.setString(7, f.getEmail());
            stmt.setString(8, f.getEndereco());
            stmt.setString(9, f.getNumero());
            stmt.setString(10, f.getComplemento());

            stmt.setString(11, f.getBairro());
            stmt.setString(12, f.getCidade());
            stmt.setString(13, f.getUf());
            stmt.setString(14, f.getCep());
            stmt.setBigDecimal(15, f.getLimiteCredito());

            stmt.executeUpdate();
        }
    }

    public void atualizar(Fornecedor f) throws SQLException {

        String sql = """
        UPDATE tabela_fornecedores SET
            razao_social = ?, nome_fantasia = ?, cnpj = ?, ie = ?,
            telefone = ?, contato = ?, email = ?, endereco = ?, 
            numero = ?, complemento = ?, bairro = ?, cidade = ?, 
            uf = ?, cep = ?, limite_credito = ?
        WHERE id = ?
    """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getRazaoSocial());
            stmt.setString(2, f.getNomeFantasia());
            stmt.setString(3, f.getCnpj());
            stmt.setString(4, f.getIe());
            stmt.setString(5, f.getTelefone());
            stmt.setString(6, f.getContato());
            stmt.setString(7, f.getEmail());
            stmt.setString(8, f.getEndereco());
            stmt.setString(9, f.getNumero());
            stmt.setString(10, f.getComplemento());
            stmt.setString(11, f.getBairro());
            stmt.setString(12, f.getCidade());
            stmt.setString(13, f.getUf());
            stmt.setString(14, f.getCep());
            stmt.setBigDecimal(15, f.getLimiteCredito());
            stmt.setLong(16, f.getId());

            stmt.executeUpdate();
        }
    }

    public void excluir(long id) throws SQLException {
        String sql = "DELETE FROM tabela_fornecedores WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public Fornecedor buscarPorId(long id) throws SQLException {

        String sql = "SELECT * FROM tabela_fornecedores WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public Fornecedor buscarPorCnpj(String cnpj) throws SQLException {

        String sql = "SELECT * FROM tabela_fornecedores WHERE cnpj = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public List<Fornecedor> buscarPorRazaoSocial(String nome) throws SQLException {

        String sql = """
            SELECT * FROM tabela_fornecedores
            WHERE razao_social LIKE ?
            ORDER BY razao_social
        """;

        List<Fornecedor> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public boolean existeCnpj(String cnpj) throws SQLException {

        String sql = "SELECT 1 FROM tabela_fornecedores WHERE cnpj = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Fornecedor> listarPorIdLimite(long idInicial, int limite) throws SQLException {

        String sql = """
            SELECT * FROM tabela_fornecedores
            WHERE id >= ?
            ORDER BY id ASC
            LIMIT ?
        """;

        List<Fornecedor> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idInicial);
            stmt.setInt(2, limite);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    private Fornecedor mapear(ResultSet rs) throws SQLException {

        Fornecedor f = new Fornecedor();

        f.setId(rs.getLong("id"));
        f.setRazaoSocial(rs.getString("razao_social"));
        f.setNomeFantasia(rs.getString("nome_fantasia"));
        f.setCnpj(rs.getString("cnpj"));
        f.setIe(rs.getString("ie"));
        f.setContato(rs.getString("contato"));
        f.setTelefone(rs.getString("telefone"));
        f.setEmail(rs.getString("email"));
        f.setEndereco(rs.getString("endereco"));
        f.setNumero(rs.getString("numero"));
        f.setComplemento(rs.getString("complemento"));
        f.setBairro(rs.getString("bairro"));
        f.setCidade(rs.getString("cidade"));
        f.setUf(rs.getString("uf"));
        f.setCep(rs.getString("cep"));
        f.setLimiteCredito(rs.getBigDecimal("limite_credito"));

        return f;
    }
}

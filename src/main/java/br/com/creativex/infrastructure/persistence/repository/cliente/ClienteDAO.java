// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas

package br.com.creativex.infrastructure.persistence.repository.cliente;

import br.com.creativex.db.Conexao;
import br.com.creativex.domain.entity.cliente.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente c) throws SQLException {
        String sql = """
            INSERT INTO tabela_clientes 
            (nome, cpf, rg, telefone, email, cep, endereco, numero, bairro, cidade, uf, limite_credito) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(c, stmt);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Cliente c) throws SQLException {
        String sql = """
            UPDATE tabela_clientes SET 
            nome=?, cpf=?, rg=?, telefone=?, email=?, cep=?, 
            endereco=?, numero=?, bairro=?, cidade=?, uf=?, limite_credito=? 
            WHERE id=?
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(c, stmt);
            stmt.setLong(13, c.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(long id) throws SQLException {
        String sql = "DELETE FROM tabela_clientes WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public Cliente buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM tabela_clientes WHERE cpf = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Cliente buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM tabela_clientes WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Cliente> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tabela_clientes WHERE nome LIKE ? ORDER BY nome";
        List<Cliente> lista = new ArrayList<>();

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

    public List<Cliente> listarPorIdLimite(long idInicial, int limite) throws SQLException {
        String sql = "SELECT * FROM tabela_clientes WHERE id >= ? ORDER BY id LIMIT ?";
        List<Cliente> lista = new ArrayList<>();

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

    private void preencherStatement(Cliente c, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, c.getNome());
        stmt.setString(2, c.getCpf());
        stmt.setString(3, c.getRg());
        stmt.setString(4, c.getTelefone());
        stmt.setString(5, c.getEmail());
        stmt.setString(6, c.getCep());
        stmt.setString(7, c.getEndereco());
        stmt.setString(8, c.getNumero());
        stmt.setString(9, c.getBairro());
        stmt.setString(10, c.getCidade());
        stmt.setString(11, c.getUf());
        stmt.setBigDecimal(12, c.getLimiteCredito());
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setRg(rs.getString("rg"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setCep(rs.getString("cep"));
        c.setEndereco(rs.getString("endereco"));
        c.setNumero(rs.getString("numero"));
        c.setBairro(rs.getString("bairro"));
        c.setCidade(rs.getString("cidade"));
        c.setUf(rs.getString("uf"));
        c.setLimiteCredito(rs.getBigDecimal("limite_credito"));
        return c;
    }
}

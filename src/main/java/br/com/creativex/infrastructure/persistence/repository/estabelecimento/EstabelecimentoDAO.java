// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.estabelecimento;
import br.com.creativex.domain.entity.config.Estabelecimento;
import br.com.creativex.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstabelecimentoDAO {

    public Estabelecimento carregarDados() throws SQLException {
        String sql = "SELECT * FROM tabela_estabelecimento LIMIT 1";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                Estabelecimento est = new Estabelecimento();
                est.setRazaoSocial(rs.getString("razao_social"));
                est.setCnpj(rs.getString("cnpj"));
                est.setInscricaoEstadual(rs.getString("inscricao_estadual"));
                est.setRegimeTributario(rs.getInt("regime_tributario"));
                est.setAliqIbpt(rs.getBigDecimal("aliq_ibpt"));
                return est;
            }
        }
        return null;
    }
}
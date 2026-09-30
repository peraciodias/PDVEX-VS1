// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gerenciador de conexões com o banco de dados PostgreSQL.
 * 
 * Classe utilitária responsável pelo gerenciamento de conexões JDBC com
 * banco de dados PostgreSQL. Encapsula as configurações de conexão
 * em um ponto único de acesso e permite carregar credenciais de
 * application.properties ou variáveis de ambiente.
 * 
 * @author Peracio Dias
 * @version 1.1
 * @since 2026-02-01
 */
public class Conexao {
    private static final Properties CONFIG = loadConfig();
    //***descomente a linha abaixo para banco de dados original***
    private static final String URL = getConfig("jdbc.url", "jdbc:postgresql://localhost:5432/bco_dados_mercado");
    //***use a linha abaixo para testes de banco auxiliar****
   // private static final String URL = getConfig("jdbc.url", "jdbc:postgresql://localhost:5432/banco_teste");

    private static final String USER = getConfig("jdbc.user", "postgres");
    private static final String PASSWORD = getConfig("jdbc.password", "postgres");

    private static Properties loadConfig() {
        Properties props = new Properties();
        try (InputStream in = Conexao.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            System.err.println("Não foi possível carregar application.properties: " + e.getMessage());
        }
        return props;
    }

    private static String getConfig(String key, String defaultValue) {
        String value = CONFIG.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value.trim();
        }
        String envKey = key.toUpperCase().replace('.', '_');
        value = System.getenv(envKey);
        return (value != null && !value.isBlank()) ? value.trim() : defaultValue;
    }

    /**
     * Obtém uma conexão ativa com o banco de dados PostgreSQL.
     * 
     * Carrega o driver JDBC do PostgreSQL e estabelece uma conexão usando
     * as credenciais configuradas no arquivo de propriedades ou nas
     * variáveis de ambiente.
     * 
     * @return Uma instância ativa de {@link Connection} com o banco de dados
     * @throws SQLException Se falhar ao obter a conexão ou driver não for encontrado
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado!", e);
        }
    }
}

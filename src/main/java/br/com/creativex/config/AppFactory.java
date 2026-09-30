// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.config;

import br.com.creativex.infrastructure.persistence.repository.fornecedor.FornecedorDAO;
import br.com.creativex.infrastructure.persistence.repository.fornecedor.FornecedorRepositoryJdbcAdapter;
import br.com.creativex.presentation.controller.FornecedorController;
import br.com.creativex.infrastructure.persistence.repository.caixa.VendaDAO;
import br.com.creativex.infrastructure.persistence.repository.caixa.VendaConsultaRepositoryJdbcAdapter;
import br.com.creativex.infrastructure.persistence.repository.caixa.VendaRepositoryJdbcAdapter;
import br.com.creativex.infrastructure.persistence.repository.cliente.ClienteDAO;
import br.com.creativex.infrastructure.persistence.repository.cliente.ClienteRepositoryJdbcAdapter;
import br.com.creativex.infrastructure.persistence.repository.usuario.UsuarioDAO;
import br.com.creativex.infrastructure.persistence.repository.usuario.UsuarioRepositoryJdbcAdapter;
import br.com.creativex.application.caixa.FinalizeVendaUseCase;
import br.com.creativex.application.caixa.ListarVendasUseCase;
import br.com.creativex.application.usuario.UsuarioUseCases;
import br.com.creativex.presentation.controller.ClienteController;
import br.com.creativex.presentation.controller.ClientepjController;
import br.com.creativex.infrastructure.persistence.repository.produto.ProdutoDAO;
import br.com.creativex.infrastructure.persistence.repository.produto.ProdutoRepositoryJdbcAdapter;
import br.com.creativex.presentation.controller.CaixaController;
import br.com.creativex.presentation.controller.ProdutoController;
import br.com.creativex.presentation.controller.UsuarioController;
import br.com.creativex.presentation.controller.VendasConsultaController;
import br.com.creativex.application.produto.CreateProdutoUseCase;
import br.com.creativex.domain.repository.VendaConsultaRepository;
import br.com.creativex.domain.repository.VendaRepository;
import br.com.creativex.domain.repository.ProdutoRepository;
import br.com.creativex.infrastructure.persistence.repository.config.ProdutoBipagemRepositoryJdbcAdapter;
import br.com.creativex.application.config.ConsultarTributoProdutoUseCase;

/**
 * Factory centralizada para injeção de dependências (Dependency Injection).
 * 
 * Implementa o padrão Factory para criar e orquestrar a composição de objetos,
 * conectando Controllers → Use Cases → Repositories (adapters JDBC) → DAOs.
 * Centralizando assim a criação de instâncias e mantendo a flexibilidade de
 * alterações na estrutura de dependências.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public final class AppFactory {

    /**
     * Construtor privado para impedir instanciação da classe.
     */
    private AppFactory() {}

    /**
     * Cria e retorna a instância do ClienteController com suas dependências.
     * 
     * @return ClienteController configurado com DAO e Repository
     */
    public static ClienteController clienteController() {
        ClienteDAO dao = new ClienteDAO();
        ClienteRepositoryJdbcAdapter repo = new ClienteRepositoryJdbcAdapter(dao);
        return new ClienteController(repo);
    }

    /**
     * Cria e retorna a instância do FornecedorController com suas dependências.
     * 
     * @return FornecedorController configurado com DAO e Repository
     */
    public static FornecedorController fornecedorController() {
        FornecedorDAO dao = new FornecedorDAO();
        FornecedorRepositoryJdbcAdapter repo = new FornecedorRepositoryJdbcAdapter(dao);
        return new FornecedorController(repo);
    }

    /**
     * Cria e retorna a instância do ProdutoController com suas dependências.
     * 
     * Orquestra a criação do CreateProdutoUseCase com transações gerenciadas
     * para operações de criação de produtos.
     * 
     * @return ProdutoController configurado com use case e repository
     * @throws RuntimeException Se falhar ao obter conexão com o banco de dados
     */
    public static ProdutoController produtoController() {
        try {
            var conn = br.com.creativex.db.Conexao.getConnection();
            var tx = new br.com.creativex.infrastructure.transaction.TransactionManager(conn);
            ProdutoDAO dao = new ProdutoDAO(tx);
            ProdutoRepositoryJdbcAdapter repo = new ProdutoRepositoryJdbcAdapter(dao);
            CreateProdutoUseCase useCase = new CreateProdutoUseCase(repo);
            return new ProdutoController(useCase, repo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar ProdutoController", e);
        }
    }

    /**
     * Cria e retorna a instância do ClientepjController com suas dependências.
     * 
     * @return ClientepjController configurado com DAO e Repository
     */
    public static ClientepjController clientepjController() {
        br.com.creativex.infrastructure.persistence.repository.clientepj.ClientepjDAO dao = new br.com.creativex.infrastructure.persistence.repository.clientepj.ClientepjDAO();
        br.com.creativex.infrastructure.persistence.repository.clientepj.ClientepjRepositoryJdbcAdapter repo = new br.com.creativex.infrastructure.persistence.repository.clientepj.ClientepjRepositoryJdbcAdapter(dao);
        return new ClientepjController(repo);
    }

    /**
     * Cria e retorna a instância do CaixaController com suas dependências.
     * 
     * Configura o FinalizeVendaUseCase para operações de finalização de vendas.
     * 
     * @return CaixaController configurado com use case e repository de vendas
     */
    public static CaixaController caixaController() {
        VendaDAO vendaDAO = new VendaDAO();
        VendaRepository vendaRepo = new VendaRepositoryJdbcAdapter(vendaDAO);
        ProdutoRepository produtoRepo = produtoRepository();
        FinalizeVendaUseCase finalizeUseCase = new FinalizeVendaUseCase(vendaRepo, produtoRepo);
        return new CaixaController(finalizeUseCase, vendaRepo);
    }

    private static ProdutoRepository produtoRepository() {
        try {
            var conn = br.com.creativex.db.Conexao.getConnection();
            var tx = new br.com.creativex.infrastructure.transaction.TransactionManager(conn);
            ProdutoDAO dao = new ProdutoDAO(tx);
            return new ProdutoRepositoryJdbcAdapter(dao);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar ProdutoRepository", e);
        }
    }

    /**
     * Cria e retorna a instância do VendasConsultaController com suas dependências.
     * 
     * Configura o ListarVendasUseCase para consultas e relatórios de vendas.
     * 
     * @return VendasConsultaController configurado com use case de consulta
     */
    public static VendasConsultaController vendasConsultaController() {
        VendaConsultaRepository consultaRepository = new VendaConsultaRepositoryJdbcAdapter();
        ListarVendasUseCase listarVendasUseCase = new ListarVendasUseCase(consultaRepository);
        return new VendasConsultaController(caixaController(), listarVendasUseCase);
    }

    /**
     * Cria e retorna a instância do UsuarioController com suas dependências.
     * 
     * Configura o UsuarioUseCases para operações de autenticação e gerenciamento
     * de usuários.
     * 
     * @return UsuarioController configurado com use cases de usuário
     */
    public static UsuarioController usuarioController() {
        UsuarioDAO dao = new UsuarioDAO();
        UsuarioRepositoryJdbcAdapter repo = new UsuarioRepositoryJdbcAdapter(dao);
        UsuarioUseCases useCases = new UsuarioUseCases(repo);
        return new UsuarioController(useCases);
    }

    /**
     * Cria e retorna a instância do ConsultarTributoProdutoUseCase com suas dependências.
     *
     * Configura o use case para consultar produtos com cálculos tributários da view vw_pdv_bipagem.
     *
     * @return ConsultarTributoProdutoUseCase configurado com repository
     * @throws RuntimeException Se falhar ao obter conexão com o banco de dados
     */
    public static ConsultarTributoProdutoUseCase consultarTributoProdutoUseCase() {
        try {
            var conn = br.com.creativex.db.Conexao.getConnection();
            var tx = new br.com.creativex.infrastructure.transaction.TransactionManager(conn);
            ProdutoBipagemRepositoryJdbcAdapter repo = new ProdutoBipagemRepositoryJdbcAdapter(tx);
            return new ConsultarTributoProdutoUseCase(repo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar ConsultarTributoProdutoUseCase", e);
        }
    }
}

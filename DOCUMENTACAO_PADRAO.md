# Padrão de Documentação do Projeto ERP-PDVex-vs1

Este documento define o padrão de documentação JavaDoc utilizado no projeto. Siga este guia ao adicionar documentação a novas classes e métodos.

---

## 1. Padrão para Classes

### Template Básico

```java
/**
 * Breve descrição do propósito da classe (uma linha).
 * 
 * Descrição detalhada explicando as responsabilidades, como usar a classe
 * e qualquer informação importante sobre o comportamento. Pode ocupar
 * múltiplas linhas conforme necessário.
 *
 * @author Nome do Autor
 * @version 1.0
 * @since 2026-02-01
 */
public class MinhaClasse {
    // implementação
}
```

### Exemplo Real (Classe Principal)

```java
/**
 * Classe principal de inicialização da aplicação ERP-PDVex-vs1.
 * 
 * Responsável pelo ponto de entrada da aplicação, configuração do tema visual
 * (Look and Feel) do sistema operacional e inicialização da tela de autenticação.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public class Main {
    // implementação
}
```

### Tags Obrigatórias para Classes
- `@author` - Nome do(s) autor(es) - Use "Peracio Dias" como padrão
- `@version` - Versão da classe (geralmente "1.0")
- `@since` - Data de criação (formato: YYYY-MM-DD)

---

## 2. Padrão para Métodos Públicos

### Template Básico

```java
/**
 * Breve descrição do que o método faz (uma linha).
 *
 * Descrição detalhada do comportamento, algoritmos especiais, 
 * efeitos colaterais, ou qualquer informação importante.
 *
 * @param nomeParametro Descrição do parâmetro
 * @param outroParametro Descrição do outro parâmetro
 * @return Descrição do valor retornado
 * @throws TipoExcecao Se ocorrer uma situação específica
 */
public TipoRetorno meuMetodo(String nomeParametro, int outroParametro) 
    throws TipoExcecao {
    // implementação
}
```

### Exemplo Real (Factory)

```java
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
    // implementação
}
```

### Exemplo Real (Validação)

```java
/**
 * Verifica se há um usuário atualmente logado na sessão.
 * 
 * @return {@code true} se um usuário está logado, {@code false} caso contrário
 */
public static boolean isLogado() {
    return usuarioLogado != null;
}
```

### Tags para Métodos
- `@param` - Um para cada parâmetro (na ordem que aparecem)
- `@return` - Descrição do retorno (omitir se retorna void)
- `@throws` - Uma por cada exceção checked que pode ser lançada

---

## 3. Padrão para Atributos/Campos

### Template Básico

```java
/** Breve descrição do atributo e seu propósito */
private String meuAtributo;
```

### Exemplo Real (Entidade)

```java
/** Identificador único do cliente */
private Long id;

/** Nome completo do cliente */
private String nome;

/** Cadastro de Pessoa Física (CPF) */
private String cpf;

/** Limite de crédito disponível para compras */
private BigDecimal limiteCredito;

/** Data e hora do cadastro do cliente */
private Timestamp dataCadastro;
```

### Boas Práticas para Atributos
- Descreva qual informação o atributo armazena
- Para valores numéricos, deixe claro se é monetário, quantidade, etc.
- Para datas, especifique o tipo (Timestamp, LocalDate, etc.)

---

## 4. Padrão para Construtores

### Template Básico

```java
/**
 * Construtor que inicializa os dados essenciais.
 * 
 * @param param1 Descrição do primeiro parâmetro
 * @param param2 Descrição do segundo parâmetro
 */
public MinhaClasse(String param1, int param2) {
    // implementação
}
```

### Construtor Privado (Utilidades/Singleton)

```java
/**
 * Construtor privado para impedir instanciação da classe.
 * 
 * Garante que esta classe seja usada apenas como gerenciadora estática
 * de sessão.
 */
private Sessao() {
    // implementação
}
```

---

## 5. Padrão para Interfaces/Contratos

```java
/**
 * Interface que define contrato para operações de persistência de clientes.
 * 
 * Abstrai a implementação específica de banco de dados, permitindo
 * diferentes estratégias de persistência.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public interface ClienteRepository {
    
    /**
     * Salva um cliente no repositório.
     * 
     * @param cliente O cliente a ser salvo
     * @return O cliente salvo com ID preenchido
     */
    Cliente save(Cliente cliente);
    
    /**
     * Remove um cliente pelo ID.
     * 
     * @param id O identificador do cliente
     * @return {@code true} se removido com sucesso, {@code false} caso contrário
     */
    boolean deleteById(Long id);
}
```

---

## 6. Checklist de Documentação

Antes de submeter código, verifique:

### Classes
- [ ] Tem descrição breve (1 linha)
- [ ] Tem descrição detalhada explicando propósito e responsabilidades
- [ ] Tem `@author` definido
- [ ] Tem `@version` definido
- [ ] Tem `@since` definido
- [ ] Campos privados têm descrição JavaDoc

### Métodos Públicos
- [ ] Tem descrição breve (1 linha)
- [ ] Tem descrição detalhada se necessário
- [ ] Cada parâmetro tem `@param`
- [ ] Tem `@return` (se não void)
- [ ] Tem `@throws` para cada exceção verificada

### Atributos
- [ ] Atributos públicos têm `/** descrição */`
- [ ] Atributos privados importantes têm `/** descrição */`

---

## 7. Gerando a Documentação

### Via Maven (Recomendado)
```bash
mvn javadoc:javadoc
```

A documentação será gerada em: `target/site/apidocs/index.html`

### Abrindo a Documentação
```bash
# Linux/Mac
open target/site/apidocs/index.html

# Windows
start target/site/apidocs/index.html
```

---

## 8. Dicas de Estilo

### Use Tags HTML com Moderação
```java
/**
 * Processa uma lista de <code>String</code> e retorna um {@code Map}.
 */
```

### Referencie Outras Classes
```java
/**
 * Retorna uma lista de {@link Cliente clientes} ativos.
 * 
 * @see ClienteRepository
 * @see br.com.creativex.application.cliente.ListarClientesUseCase
 */
```

### Use {@code} para Código Inline
```java
/**
 * @return {@code true} se logado, {@code false} caso contrário
 */
```

### Use {@literal} para Caracteres Especiais
```java
/**
 * Descrição com {@literal <} e {@literal >}
 */
```

---

## 9. Estrutura de Pacotes e Documentação

### Padrão de Camadas

```
br.com.creativex
├── Main                          // Ponto de entrada
├── config/
│   └── AppFactory               // Factory de dependências
├── domain/
│   ├── entity/                  // Entidades de domínio
│   ├── repository/              // Interfaces de repositório
│   └── transaction/             // Transações
├── application/
│   ├── cliente/                 // Use cases de cliente
│   ├── produto/                 // Use cases de produto
│   └── caixa/                   // Use cases de vendas
├── infrastructure/
│   └── persistence/             // Implementações JDBC
├── presentation/
│   └── controller/              // Controllers
├── ui/                          // Componentes Swing
├── util/                        // Utilidades
└── db/                          // Gerenciamento de conexão
```

Documente classes em cada nível de acordo com seu propósito:
- **Domain**: Foque em regras de negócio
- **Application**: Foque em casos de uso
- **Infrastructure**: Foque em detalhes técnicos
- **Presentation**: Foque em fluxo de interação

---

## 10. Exemplos Completos

### Entidade de Domínio

```java
/**
 * Entidade que representa uma venda no sistema ERP-PDVex.
 * 
 * Encapsula dados da transação, itens vendidos e cálculos de totais.
 * As operações de venda são realizadas através de casos de uso que
 * implementam as regras de negócio (validações, limites, etc).
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 * @see br.com.creativex.application.caixa.FinalizeVendaUseCase
 */
public class Venda {
    
    /** Identificador único da venda */
    private long idVenda;
    
    /** ID do usuário que realizou a venda */
    private long idUsuario;
    
    /** ID do cliente (null para cliente avulso) */
    private Long idCliente;
    
    /**
     * Obtém o identificador único da venda.
     * 
     * @return ID da venda
     */
    public long getIdVenda() {
        return idVenda;
    }
    
    /**
     * Define o identificador da venda.
     * 
     * @param idVenda ID a ser definido
     */
    public void setIdVenda(long idVenda) {
        this.idVenda = idVenda;
    }
}
```

### Caso de Uso

```java
/**
 * Caso de uso para listar vendas com filtros e paginação.
 * 
 * Implementa a lógica de recuperação de vendas do repositório,
 * aplicando filtros de período, cliente ou vendedor, e retornando
 * os resultados organizados por data descrescente.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public class ListarVendasUseCase {
    
    /** Repositório para consulta de vendas */
    private final VendaConsultaRepository repository;
    
    /**
     * Construtor que inicializa com o repositório de consulta.
     * 
     * @param repository Repositório de vendas
     */
    public ListarVendasUseCase(VendaConsultaRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Executa a consulta de vendas com os filtros especificados.
     * 
     * @param filtro Objeto contendo data inicial, data final, cliente, etc
     * @return Lista de vendas que atendem aos critérios
     */
    public List<Venda> execute(VendaFiltro filtro) {
        return repository.buscar(filtro);
    }
}
```

---

## 11. Referências

- [Oracle JavaDoc Guide](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)
- [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

---

## Próximos Passos

1. ✅ Aplique este padrão a todas as novas classes criadas
2. ✅ Atualize classes existentes incrementalmente
3. ✅ Execute `mvn javadoc:javadoc` regularmente
4. ✅ Revise a documentação antes de commits/PRs
5. ✅ Mantenha a documentação atualizada junto com o código


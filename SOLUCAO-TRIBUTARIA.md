# Solução: Erro "Coluna aliquota_estimada não encontrada"

## 📋 Resumo do Problema

Seu sistema estava tentando acessar uma coluna `aliquota_estimada` que:
1. **Não tinha o tipo de dado correto** no SQL (era NUMERIC(5,2), deveria ser NUMERIC(10,2))
2. **Tinha um alias errado** na view (`aliquota_applied` em vez de `aliquota_aplicada`)
3. **Não tinha constraint NOT NULL**, permitindo valores nulos indesejados

---

## ✅ O Que Foi Corrigido

### 1. **Arquivos SQL Atualizados**
- ✅ `src/main/resources/TABELAS-PDVEX-VS1.sql`
- ✅ `target/classes/TABELAS-PDVEX-VS1.sql`
- ✅ Criado: `DADOS-DE-INSTALACAO/ESQUEMAS-SQL/CORRIGIR-TRIBUTARIA-VIEW.sql`

### 2. **Novo Código Java Criado**

Para que seu Java consiga buscar os cálculos tributários DO BANCO, foram criadas:

#### Entidades
```
src/main/java/br/com/creativex/domain/entity/config/
└── ProdutoBipagem.java
```
Representa os dados da view com alíquota e imposto já calculados.

#### DAOs & Repositories
```
src/main/java/br/com/creativex/domain/repository/
└── ProdutoBipagemRepository.java  (interface)

src/main/java/br/com/creativex/infrastructure/persistence/repository/config/
├── ProdutoBipagemDAO.java
└── ProdutoBipagemRepositoryJdbcAdapter.java
```

#### Use Cases
```
src/main/java/br/com/creativex/application/config/
└── ConsultarTributoProdutoUseCase.java
```

---

## 🚀 Como Usar Agora

### Passo 1: Executar o Script SQL de Correção
```bash
# No seu cliente PostgreSQL (DBeaver, pgAdmin, psql):
# Execute o arquivo: DADOS-DE-INSTALACAO/ESQUEMAS-SQL/CORRIGIR-TRIBUTARIA-VIEW.sql
```

### Passo 2: Testar o Sistema
```bash
# Execute o script de teste para validar:
# DADOS-DE-INSTALACAO/ESQUEMAS-SQL/TESTE-TRIBUTACAO.sql
```

### Passo 3: Usar no Código Java

**Exemplo em um formulário de caixa ou gerador de vendas:**

```java
import br.com.creativex.domain.transaction.Transaction;
import br.com.creativex.infrastructure.persistence.repository.config.ProdutoBipagemRepositoryJdbcAdapter;
import br.com.creativex.application.config.ConsultarTributoProdutoUseCase;
import br.com.creativex.domain.entity.config.ProdutoBipagem;

// Dentro do seu código:
public void buscarProdutoComImposto(String codigoBarra) {
    // 1. Instanciar o repository
    ProdutoBipagemRepositoryJdbcAdapter repository = 
        new ProdutoBipagemRepositoryJdbcAdapter(transaction);
    
    // 2. Criar o use case
    ConsultarTributoProdutoUseCase useCase = 
        new ConsultarTributoProdutoUseCase(repository);
    
    // 3. Buscar o produto
    ProdutoBipagem produto = useCase.buscarPorCodigoBarra(codigoBarra);
    
    if (produto != null) {
        System.out.println("Produto: " + produto.getDescricao());
        System.out.println("Preço: " + produto.getPrecoVenda());
        System.out.println("Alíquota: " + produto.getAliquotaAplicada()); // ✅ DO BANCO!
        System.out.println("Imposto Item: " + produto.getValorImpostoItem()); // ✅ DO BANCO!
        
        // Usar esses valores em vez de calcular localmente
    }
}
```

---

## 🎯 Integração no Cupom Virtual

### ✅ **Modificações Implementadas**

#### 1. **Classe `CaixasForm.java` Atualizada**
- ✅ Adicionado `ConsultarTributoProdutoUseCase` como dependência
- ✅ Método `adicionarProdutoPeloCodigo()` agora busca tributos da view
- ✅ Cupom mostra **Alíquota** e **Valor do Imposto** por item
- ✅ Visor mostra alíquota aplicada no momento da venda

#### 2. **Novo Layout do Cupom**
```
-------------------------------------------------------------------
SEQ CÓD.BARRAS    DESC           QTD   VALOR     ALÍQ  IMPOSTO
-------------------------------------------------------------------
001 7896565700638 PRODUTO ABC    1     R$ 15,72  17.00% R$ 2,67
002 7891234567890 OUTRO PROD     2     R$ 10,00  12.00% R$ 2,40
-------------------------------------------------------------------
TOTAL LIQUIDO: R$ 25,72
ICMS:          R$ 5,07
PIS:           R$ 0,00
COFINS:        R$ 0,00
TOTAL TRIBUTOS: R$ 5,07
-------------------------------------------------------------------
```

#### 3. **Visor de Item Atualizado**
```
PRODUTO ABC  |  QTD: 1  |  R$ 15,72  |  ALÍQ: 17.00%
```

---

## 📊 O Que a View Faz

```sql
CREATE OR REPLACE VIEW public.vw_pdv_bipagem AS
SELECT 
    p.id,
    p.codigo_barra,
    p.descricao,
    p.marca,
    p.preco_venda,
    p.quantidade_estoque,
    p.cst_icms,
    p.preco_custo,
    COALESCE(t.aliquota_estimada, 13.45) AS aliquota_aplicada,      -- ✅ ALÍQUOTA
    ROUND(p.preco_venda * COALESCE(t.aliquota_estimada, 13.45) / 100, 2) AS valor_imposto_item  -- ✅ IMPOSTO
FROM 
    public.tabela_produtos p
LEFT JOIN 
    public.config_tributaria t ON TRIM(p.cst_icms) = TRIM(t.cst_icms);
```

- **LEFT JOIN**: Se o CST não tiver entrada em `config_tributaria`, usa 13.45% como padrão
- **TRIM()**: Remove espaços em branco na comparação de CST
- **COALESCE()**: Garante que sempre há um valor

---

## 🔍 Como Validar

### 1. Verificar a View no Banco
```sql
-- No PostgreSQL, execute:
SELECT * FROM public.vw_pdv_bipagem LIMIT 5;

-- Deve retornar colunas:
-- id, codigo_barra, descricao, marca, preco_venda, 
-- quantidade_estoque, cst_icms, preco_custo, 
-- aliquota_aplicada, valor_imposto_item
```

### 2. Verificar a Tabela de Configuração
```sql
SELECT * FROM public.config_tributaria;

-- Deve retornar:
-- id | cst_icms | aliquota_estimada
--  1 |   '00'   |     17.00
--  2 |   '20'   |     12.00
--  3 |   '40'   |      0.00
```

---

## 📝 Métodos Disponíveis

### `ConsultarTributoProdutoUseCase`

| Método | Descrição |
|--------|-----------|
| `buscarPorCodigoBarra(String)` | Busca 1 produto pelo código de barras |
| `buscarPorId(Long)` | Busca 1 produto pelo ID |
| `buscarPorDescricao(String)` | Busca múltiplos produtos por descrição (ILIKE) |
| `listarTodos()` | Lista TODOS os produtos (cuidado com performance) |
| `listarComEstoque()` | Lista apenas produtos com estoque > 0 |

---

## ⚠️ Importante

1. **Faça backup** do seu banco ANTES de executar o script SQL
2. **CST_ICMS** deve ter sempre o mesmo número de dígitos (use '00', '20', não '0', '2')
3. Se um produto não tiver CST na `config_tributaria`, será aplicado 13.45% como padrão
4. O cálculo do imposto já vem pronto do banco - **não calcule novamente em Java**

---

## 📞 Próximos Passos

1. Execute `CORRIGIR-TRIBUTARIA-VIEW.sql` no seu PostgreSQL
2. Execute `TESTE-TRIBUTACAO.sql` para validar
3. Teste o cupom virtual - agora mostra alíquotas e impostos!
4. Verifique se as vendas estão gravando os valores corretos

Qualquer dúvida, consulte os exemplos nos comentários de cada classe Java!

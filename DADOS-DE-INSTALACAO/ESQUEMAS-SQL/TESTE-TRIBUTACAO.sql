-- ============================================================================
-- Script de Teste: Sistema de Tributação Integrado
-- Executa consultas para validar o funcionamento do cupom virtual
-- ============================================================================
-- Data: 2026-05-09
-- Autor: Peracio Dias
-- ============================================================================

-- 1. VERIFICAR SE A VIEW ESTÁ FUNCIONANDO
SELECT '=== TESTE 1: View vw_pdv_bipagem ===' AS teste;
SELECT id, codigo_barra, descricao, aliquota_aplicada, valor_imposto_item
FROM public.vw_pdv_bipagem
LIMIT 3;

-- 2. VERIFICAR SE A TABELA config_tributaria TEM DADOS
SELECT '=== TESTE 2: Tabela config_tributaria ===' AS teste;
SELECT cst_icms, aliquota_estimada
FROM public.config_tributaria
ORDER BY cst_icms;

-- 3. SIMULAR BUSCA POR CÓDIGO DE BARRAS (como faz o Java)
SELECT '=== TESTE 3: Busca por código de barras ===' AS teste;
SELECT 'Buscando produto com código: 7896565700638' AS info;
SELECT *
FROM public.vw_pdv_bipagem
WHERE codigo_barra = '7896565700638';

-- 4. VERIFICAR CÁLCULO MANUAL vs VIEW
SELECT '=== TESTE 4: Comparação de cálculos ===' AS teste;
SELECT
    p.descricao,
    p.preco_venda,
    t.aliquota_estimada AS aliquota_config,
    COALESCE(t.aliquota_estimada, 13.45) AS aliquota_aplicada,
    ROUND(p.preco_venda * COALESCE(t.aliquota_estimada, 13.45) / 100, 2) AS imposto_calculado,
    vb.aliquota_aplicada AS aliquota_view,
    vb.valor_imposto_item AS imposto_view,
    CASE
        WHEN ROUND(p.preco_venda * COALESCE(t.aliquota_estimada, 13.45) / 100, 2) = vb.valor_imposto_item
        THEN '✅ CORRETO'
        ELSE '❌ ERRO'
    END AS status
FROM public.tabela_produtos p
LEFT JOIN public.config_tributaria t ON TRIM(p.cst_icms) = TRIM(t.cst_icms)
LEFT JOIN public.vw_pdv_bipagem vb ON p.id = vb.id
WHERE p.cst_icms IS NOT NULL
LIMIT 5;

-- 5. VERIFICAR PRODUTOS SEM CST (usam padrão 13.45%)
SELECT '=== TESTE 5: Produtos sem CST (padrão 13.45%) ===' AS teste;
SELECT
    p.descricao,
    p.cst_icms,
    vb.aliquota_aplicada,
    vb.valor_imposto_item,
    CASE
        WHEN vb.aliquota_aplicada = 13.45 THEN '✅ PADRÃO CORRETO'
        ELSE '❌ PADRÃO INCORRETO'
    END AS status
FROM public.tabela_produtos p
LEFT JOIN public.vw_pdv_bipagem vb ON p.id = vb.id
WHERE p.cst_icms IS NULL OR TRIM(p.cst_icms) = ''
LIMIT 3;

-- 6. CONTAGEM GERAL
SELECT '=== TESTE 6: Estatísticas Gerais ===' AS teste;
SELECT
    (SELECT COUNT(*) FROM public.tabela_produtos) AS total_produtos,
    (SELECT COUNT(*) FROM public.config_tributaria) AS regras_tributarias,
    (SELECT COUNT(*) FROM public.vw_pdv_bipagem) AS produtos_com_tributo,
    (SELECT COUNT(*) FROM public.vw_pdv_bipagem WHERE aliquota_aplicada > 0) AS produtos_tributados;

-- FIM DOS TESTES
SELECT '=== FIM DOS TESTES ===' AS conclusao;
SELECT 'Se todos os testes mostrarem ✅ CORRETO, o sistema está funcionando!' AS status;
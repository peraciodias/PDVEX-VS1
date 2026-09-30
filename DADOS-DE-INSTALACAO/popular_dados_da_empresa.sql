-- =============================================================================
-- POPULAÇÃO INICIAL — PDVEX-VS1
-- CREATIVEX SISTEMAS © 2026
-- =============================================================================

--------------------------------------------------------------------------------
-- 1. DADOS DA EMPRESA / ESTABELECIMENTO
-- Altere os dados abaixo com os da sua empresa
--------------------------------------------------------------------------------
INSERT INTO public.tabela_estabelecimento (
    razao_social, nome_fantasia, cnpj, inscricao_estadual,
    logradouro, numero, bairro, cidade, estado, cep,
    codigo_municipio_ibge, regime_tributario, aliq_ibpt
) VALUES (
    'Creativex Sistemas Ltda',
    'PDVex Comércio',
    '00.000.000/0001-00',
    'ISENTO',
    'Rua Principal',
    '100',
    'Centro',
    'Sorocaba',
    'SP',
    '18000-000',
    '3552205',
    1,
    13.45
) ON CONFLICT DO NOTHING;
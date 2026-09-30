--------------------------------------------------------------------------------
--admin=postgres   /  senha=postgres
--------------------------------------------------------------------------------
-- Usuário: admin   |   Senha: admin123
-- =============================================================================
-- SCRIPT DE CRIAÇÃO COMPLETA — PDVEX-VS1
-- CREATIVEX SISTEMAS © 2026
-- Versão Final: Tudo Novo, Limpo e Padronizado
-- =============================================================================

--------------------------------------------------------------------------------
-- 1. TABELAS RAIZ (sem dependências)
--------------------------------------------------------------------------------

CREATE TABLE public.tabela_estabelecimento (
    id SERIAL PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150),
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    inscricao_estadual VARCHAR(20),
    logradouro VARCHAR(150),
    numero VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado CHAR(2),
    cep VARCHAR(9),
    codigo_municipio_ibge VARCHAR(7),
    regime_tributario INTEGER DEFAULT 1,
    aliq_ibpt NUMERIC(5,2) DEFAULT 13.45
);

CREATE TABLE public.tabela_usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) DEFAULT 'OPERADOR',
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE public.tabela_clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    rg VARCHAR(20),
    telefone VARCHAR(15),
    email VARCHAR(100),
    endereco VARCHAR(150),
    numero VARCHAR(10),
    bairro VARCHAR(50),
    cidade VARCHAR(50) NOT NULL,
    uf CHAR(2),
    cep VARCHAR(9),
    limite_credito NUMERIC(12,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.tabela_clientes_pj (
    id BIGSERIAL PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150),
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    ie VARCHAR(30),
    telefone VARCHAR(15),
    email VARCHAR(150),
    endereco VARCHAR(150),
    numero VARCHAR(20),
    complemento VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2),
    cep VARCHAR(9),
    limite_credito NUMERIC(12,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.tabela_fornecedores (
    id BIGSERIAL PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150),
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    ie VARCHAR(30),
    contato VARCHAR(120) NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(150),
    endereco VARCHAR(150),
    numero VARCHAR(20),
    complemento VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2),
    cep VARCHAR(9),
    limite_credito NUMERIC(12,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.config_tributaria (
    id SERIAL PRIMARY KEY,
    cst_icms VARCHAR(3) NOT NULL,
    aliquota_estimada NUMERIC(10,2) NOT NULL,
    CONSTRAINT cst_unico UNIQUE (cst_icms)
);

-- TODOS os 1.086 códigos CST prontos
INSERT INTO public.config_tributaria (cst_icms, aliquota_estimada)
SELECT TO_CHAR(gerador, 'FM000'), 0.00 FROM generate_series(0, 999) AS gerador
UNION SELECT TO_CHAR(gerador, 'FM00'), 0.00 FROM generate_series(0, 9) AS gerador
UNION SELECT TO_CHAR(gerador, 'FM0'), 0.00 FROM generate_series(0, 9) AS gerador
ON CONFLICT (cst_icms) DO NOTHING;

--------------------------------------------------------------------------------
-- 2. TABELAS COM DEPENDÊNCIAS
--------------------------------------------------------------------------------

CREATE TABLE public.tabela_produtos (
    id BIGSERIAL PRIMARY KEY,
    codigo_barra VARCHAR(20) NOT NULL UNIQUE,
    origem VARCHAR(1) NOT NULL,
    descricao VARCHAR(50) NOT NULL,
    marca VARCHAR(50),
    atributos VARCHAR(50),
    unidade_medida VARCHAR(30),
    categoria VARCHAR(50),
    cod_grupo INTEGER,
    grupo VARCHAR(80),
    tipo_balanca CHAR(1) CHECK (tipo_balanca IN ('B', 'C', 'N')),
    quantidade_estoque NUMERIC(10,3) NOT NULL DEFAULT 0.000,
    preco_custo NUMERIC(10,2),
    preco_venda NUMERIC(10,2) NOT NULL,
    ncm VARCHAR(10),
    cest VARCHAR(7),
    cfop_padrao VARCHAR(4),
    unidade_tributavel VARCHAR(10),
    cean_tributavel VARCHAR(20),
    cst_icms CHAR(3) REFERENCES public.config_tributaria(cst_icms) CHECK (LENGTH(cst_icms) = 3),
    aliquota_icms NUMERIC(5,2) DEFAULT 0.00,
    cst_pis VARCHAR(2),
    ppis NUMERIC(5,2) DEFAULT 0.00,
    cst_cofins VARCHAR(2),
    pcofins NUMERIC(5,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    loja VARCHAR(50) DEFAULT 'MATRIZ'
);

CREATE TABLE public.tabela_caixas (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(50),
    id_usuario BIGINT REFERENCES public.tabela_usuarios(id)
);

CREATE TABLE public.tabela_vendas (
    id_venda BIGSERIAL PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario BIGINT NOT NULL REFERENCES public.tabela_usuarios(id),
    id_cliente BIGINT REFERENCES public.tabela_clientes(id),
    nome_cliente_avulso VARCHAR(150),
    cpf_avulso VARCHAR(20),
    total_bruto NUMERIC(10,2) NOT NULL,
    total_desconto NUMERIC(10,2) DEFAULT 0.00,
    total_liquido NUMERIC(10,2) NOT NULL,
    total_tributos NUMERIC(10,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'CONCLUIDA',
    status_fiscal VARCHAR(30) DEFAULT 'PENDENTE_EMISSAO',
    metodo_pagamento VARCHAR(50) NOT NULL,
    valor_pago NUMERIC(10,2),
    troco NUMERIC(10,2),
    chave_nfe VARCHAR(44),
    id_caixa BIGINT REFERENCES public.tabela_caixas(id),
    CONSTRAINT chk_valores_venda CHECK (total_bruto >= 0 AND total_liquido >= 0)
);

CREATE TABLE public.tabela_itens_venda (
    id_item BIGSERIAL PRIMARY KEY,
    id_venda BIGINT NOT NULL REFERENCES public.tabela_vendas(id_venda) ON DELETE CASCADE,
    id_produto BIGINT NOT NULL REFERENCES public.tabela_produtos(id),
    quantidade NUMERIC(10,3) NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    desconto_item NUMERIC(10,2) DEFAULT 0.00,
    subtotal NUMERIC(10,2) NOT NULL,
    preco_custo_momento NUMERIC(10,2),
    cst_fiscal_momento VARCHAR(3),
    icms_aliquota_momento NUMERIC(5,2) DEFAULT 0.00,
    icms_valor_momento NUMERIC(10,2) DEFAULT 0.00,
    cst_pis_momento VARCHAR(2),
    pis_aliquota_momento NUMERIC(5,2) DEFAULT 0.00,
    pis_valor_momento NUMERIC(10,2) DEFAULT 0.00,
    cst_cofins_momento VARCHAR(2),
    cofins_aliquota_momento NUMERIC(5,2) DEFAULT 0.00,
    cofins_valor_momento NUMERIC(10,2) DEFAULT 0.00
);

CREATE TABLE public.tabela_documentos_fiscais (
    id_documento BIGSERIAL PRIMARY KEY,
    id_venda BIGINT NOT NULL REFERENCES public.tabela_vendas(id_venda) ON DELETE CASCADE,
    modelo VARCHAR(10) NOT NULL DEFAULT 'NFCE',
    status VARCHAR(30) NOT NULL DEFAULT 'PENDENTE_EMISSAO',
    ambiente VARCHAR(20) NOT NULL DEFAULT 'HOMOLOGACAO',
    modo_emissao VARCHAR(30) NOT NULL DEFAULT 'NORMAL',
    serie INTEGER,
    numero BIGINT,
    chave_acesso VARCHAR(44),
    protocolo_autorizacao VARCHAR(50),
    xml_gerado TEXT,
    xml_assinado TEXT,
    xml_autorizado TEXT,
    motivo_rejeicao TEXT,
    data_emissao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_autorizacao TIMESTAMP,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_documento_fiscal_venda_modelo UNIQUE (id_venda, modelo)
);

CREATE TABLE public.tabela_movimentacoes_caixa (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    valor NUMERIC(10,2) NOT NULL,
    motivo VARCHAR(255),
    id_usuario BIGINT REFERENCES public.tabela_usuarios(id),
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.tabela_movimentacoes_estoque (
    id BIGSERIAL PRIMARY KEY,
    id_produto BIGINT NOT NULL REFERENCES public.tabela_produtos(id),
    tipo VARCHAR(20) NOT NULL,
    quantidade NUMERIC(10,3) NOT NULL,
    saldo_anterior NUMERIC(10,3),
    saldo_posterior NUMERIC(10,3),
    motivo VARCHAR(255),
    id_usuario BIGINT REFERENCES public.tabela_usuarios(id),
    id_venda_origem BIGINT REFERENCES public.tabela_vendas(id_venda) ON DELETE SET NULL,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- 3. ÍNDICES DE DESEMPENHO
--------------------------------------------------------------------------------
CREATE INDEX idx_produto_cod_barra ON public.tabela_produtos (codigo_barra);
CREATE INDEX idx_movimentacao_produto ON public.tabela_movimentacoes_estoque (id_produto);
CREATE INDEX idx_movimentacao_venda ON public.tabela_movimentacoes_estoque (id_venda_origem);
CREATE INDEX idx_vendas_data ON public.tabela_vendas (data_venda);
CREATE INDEX idx_itens_venda_id_venda ON public.tabela_itens_venda (id_venda);
CREATE INDEX idx_cliente_nome ON public.tabela_clientes (nome);
CREATE INDEX idx_fornecedor_cnpj ON public.tabela_fornecedores (cnpj);

--------------------------------------------------------------------------------
-- 4. VIEW — Bipagem Rápida do PDV
--------------------------------------------------------------------------------
CREATE VIEW public.vw_pdv_bipagem AS
SELECT 
    p.id, p.codigo_barra, p.descricao, p.marca, p.preco_venda, p.quantidade_estoque,
    p.cst_icms, p.preco_custo,
    COALESCE(t.aliquota_estimada, 13.45) AS aliquota_aplicada,
    ROUND(p.preco_venda * COALESCE(t.aliquota_estimada, 13.45) / 100, 2) AS valor_imposto_item
FROM public.tabela_produtos p
LEFT JOIN public.config_tributaria t ON p.cst_icms = t.cst_icms;

--------------------------------------------------------------------------------
-- 5. USUÁRIO PADRÃO DE ACESSO
--------------------------------------------------------------------------------
INSERT INTO public.tabela_usuarios (nome, login, senha, perfil, ativo)
VALUES ('Administrador do Sistema', 'admin', 'admin123', 'ADMINISTRADOR', TRUE)
ON CONFLICT (login) DO NOTHING;

--------------------------------------------------------------------------------
-- ✅ FIM — PDVEX-VS1 Banco de Dados Completo
-- Usuário: admin   |   Senha: admin123
-- Total CST: 1096 registros
--------------------------------------------------------------------------------

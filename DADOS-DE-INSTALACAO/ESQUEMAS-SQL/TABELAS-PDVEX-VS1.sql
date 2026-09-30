-- =============================================================================
-- SCRIPT CONSOLIDADO ERP-PDVex-vs1 (VERSÃO INTEGRAL E REFINADA - CLIENTES UNIFICADOS)
-- Data: Atualizado com Tabela Única de Clientes
-- =============================================================================

-- CRIA O BANCO DE DADOS PADRÃO PARA CARACTERES E ACENTUAÇÃO BRASILEIROS
-- Execute manualmente apenas se o banco ainda não existir.
-- CREATE DATABASE bco_dados_mercado
-- WITH ENCODING 'UTF8'
-- LC_COLLATE = 'pt_BR.UTF-8'
-- LC_CTYPE = 'pt_BR.UTF-8';

--------------------------------------------------------------------------------
-- 1. TABELAS SEM DEPENDÊNCIAS (TABELAS PAI)
--------------------------------------------------------------------------------

-- Configuração da Empresa/Estabelecimento
CREATE TABLE IF NOT EXISTS public.tabela_estabelecimento (
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
    cep VARCHAR(8),
    codigo_municipio_ibge VARCHAR(7),
    regime_tributario INTEGER DEFAULT 1,
    aliq_ibpt NUMERIC(5,2) DEFAULT 13.45
);

ALTER TABLE public.tabela_estabelecimento
    ADD COLUMN IF NOT EXISTS cep VARCHAR(8);

ALTER TABLE public.tabela_estabelecimento
    ADD COLUMN IF NOT EXISTS codigo_municipio_ibge VARCHAR(7);

-- Gestão de Usuários e Acesso
CREATE TABLE IF NOT EXISTS public.tabela_usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) DEFAULT 'OPERADOR',
    ativo BOOLEAN DEFAULT TRUE
);

-- Cadastro Unificado de Clientes (Substitui as antigas tabelas PF e PJ)
CREATE TABLE IF NOT EXISTS public.tabela_clientes (
    id BIGSERIAL PRIMARY KEY,
    tipo_pessoa CHAR(1) NOT NULL CHECK (tipo_pessoa IN ('F', 'J')), -- 'F' = Física, 'J' = Jurídica
    nome_razao_social VARCHAR(150) NOT NULL, -- Nome ou Razão Social
    nome_fantasia VARCHAR(150),              -- Nome Fantasia (opcional, para PJ)
    documento VARCHAR(18) NOT NULL UNIQUE,   -- CPF ou CNPJ padronizados para aceitar máscaras
    rg_inscricao_estadual VARCHAR(30),       -- RG ou Inscrição Estadual
    telefone VARCHAR(20),                     -- Tamanho ampliado para suportar máscaras
    email VARCHAR(150),
    endereco VARCHAR(150),
    numero VARCHAR(20),                      -- Tamanho unificado baseado na tabela PJ antiga
    complemento VARCHAR(20),
    bairro VARCHAR(100),                     -- Tamanho unificado baseado na tabela PJ antiga
    cidade VARCHAR(100) NOT NULL,            -- Tamanho unificado baseado na tabela PJ antiga
    uf CHAR(2),
    cep VARCHAR(10),                         -- Tamanho ampliado para aceitar o hífen da máscara
    limite_credito NUMERIC(12,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cadastro de Fornecedores
CREATE TABLE IF NOT EXISTS public.tabela_fornecedores (
    id BIGSERIAL PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150),
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    ie VARCHAR(30),
    contato VARCHAR(120) NOT NULL,
    telefone VARCHAR(11),
    email VARCHAR(150),
    endereco VARCHAR(150),
    numero VARCHAR(20),
    complemento VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2),
    cep VARCHAR(8),
    limite_credito NUMERIC(12,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Configuração Tributária (Com a restrição UNIQUE integrada)
CREATE TABLE IF NOT EXISTS public.config_tributaria (
    id SERIAL PRIMARY KEY,
    cst_icms VARCHAR(3) NOT NULL,
    aliquota_estimada NUMERIC(10,2) NOT NULL,
    CONSTRAINT cst_unico UNIQUE (cst_icms)
);

--------------------------------------------------------------------------------
-- 2. TABELAS COM DEPENDÊNCIAS
--------------------------------------------------------------------------------

-- Cadastro de Produtos
CREATE TABLE IF NOT EXISTS public.tabela_produtos (
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
    cst_icms VARCHAR(3),
    aliquota_icms NUMERIC(5,2) DEFAULT 0.00,
    cst_pis VARCHAR(2),
    ppis NUMERIC(5,2) DEFAULT 0.00,
    cst_cofins VARCHAR(2),
    pcofins NUMERIC(5,2) DEFAULT 0.00,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    loja VARCHAR(50) DEFAULT 'MATRIZ'
);

-- Controle de Caixas
CREATE TABLE IF NOT EXISTS public.tabela_caixas (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(50),
    id_usuario BIGINT REFERENCES public.tabela_usuarios(id)
);

-- Tabela Principal de Vendas (ESTRUTURA ORIGINAL COMPLETA E PRESERVADA)
CREATE TABLE IF NOT EXISTS public.tabela_vendas (
    id_venda BIGSERIAL PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario BIGINT NOT NULL REFERENCES public.tabela_usuarios(id),
    id_cliente BIGINT REFERENCES public.tabela_clientes(id) ON DELETE RESTRICT, -- Atualizado para a tabela unificada
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

-- Itens da Venda
CREATE TABLE IF NOT EXISTS public.tabela_itens_venda (
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

-- Documentos Fiscais
CREATE TABLE IF NOT EXISTS public.tabela_documentos_fiscais (
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

ALTER TABLE public.tabela_documentos_fiscais
    ADD COLUMN IF NOT EXISTS xml_gerado TEXT;

ALTER TABLE public.tabela_documentos_fiscais
    ADD COLUMN IF NOT EXISTS data_autorizacao TIMESTAMP;

-- Compatibilidade para bancos criados por versões anteriores do sistema.
ALTER TABLE public.tabela_itens_venda
    ADD COLUMN IF NOT EXISTS icms_aliquota_momento NUMERIC(5,2) DEFAULT 0.00;

ALTER TABLE public.tabela_itens_venda
    ADD COLUMN IF NOT EXISTS icms_valor_momento NUMERIC(10,2) DEFAULT 0.00;

ALTER TABLE public.tabela_itens_venda
    ADD COLUMN IF NOT EXISTS cst_pis_momento VARCHAR(2);

ALTER TABLE public.tabela_itens_venda
    ADD COLUMN IF NOT EXISTS pis_aliquota_momento NUMERIC(5,2) DEFAULT 0.00;

ALTER TABLE public.tabela_itens_venda
    ADD COLUMN IF NOT EXISTS pis_valor_momento NUMERIC(10,2) DEFAULT 0.00;

ALTER TABLE public.tabela_itens_venda
    ADD COLUMN IF NOT EXISTS cst_cofins_momento VARCHAR(2);

ALTER TABLE public.tabela_itens_venda
    ADD COLUMN IF NOT EXISTS cofins_aliquota_momento NUMERIC(5,2) DEFAULT 0.00;

ALTER TABLE public.tabela_itens_venda
    ADD COLUMN IF NOT EXISTS cofins_valor_momento NUMERIC(10,2) DEFAULT 0.00;

-- Movimentações de Estoque (Mantendo saldos anterior/posterior)
CREATE TABLE IF NOT EXISTS public.tabela_movimentacoes_estoque (
    id BIGSERIAL PRIMARY KEY,
    id_produto BIGINT NOT NULL REFERENCES public.tabela_produtos(id),
    tipo VARCHAR(20) NOT NULL,
    quantidade NUMERIC(10,3) NOT NULL,
    saldo_anterior NUMERIC(10,3),
    saldo_posterior NUMERIC(10,3),
    motivo VARCHAR(255),
    id_usuario BIGINT REFERENCES public.tabela_usuarios(id),
    id_venda_origem BIGINT REFERENCES public.tabela_vendas(id_venda),
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--------------------------------------------------------------------------------
-- 3. ÍNDICES (UNIFICAÇÃO DAS DUAS VERSÕES)
--------------------------------------------------------------------------------

-- Clientes (Novos Índices para Busca Otimizada na Tabela Única)
CREATE INDEX IF NOT EXISTS idx_clientes_nome_razao ON public.tabela_clientes (nome_razao_social);
CREATE INDEX IF NOT EXISTS idx_clientes_documento ON public.tabela_clientes (documento);

-- Fornecedores
CREATE INDEX IF NOT EXISTS idx_fornecedor_razao ON public.tabela_fornecedores (razao_social);
CREATE INDEX IF NOT EXISTS idx_fornecedor_cidade ON public.tabela_fornecedores (cidade);
CREATE INDEX IF NOT EXISTS idx_fornecedor_contato ON public.tabela_fornecedores (contato);

-- Produtos e Estoque
CREATE INDEX IF NOT EXISTS idx_produto_cod_barra ON public.tabela_produtos (codigo_barra);
CREATE INDEX IF NOT EXISTS idx_movimentacao_produto ON public.tabela_movimentacoes_estoque (id_produto);
CREATE INDEX IF NOT EXISTS idx_movimentacao_venda ON public.tabela_movimentacoes_estoque (id_venda_origem);

-- Vendas e Itens (PDV)
CREATE INDEX IF NOT EXISTS idx_vendas_data ON public.tabela_vendas (data_venda);
CREATE INDEX IF NOT EXISTS idx_itens_venda_id_venda ON public.tabela_itens_venda (id_venda);

--------------------------------------------------------------------------------
-- 4. VIEWS E DADOS INICIAIS
--------------------------------------------------------------------------------

INSERT INTO public.config_tributaria (cst_icms, aliquota_estimada) 
VALUES ('000', 17.00), ('020', 12.00), ('040', 0.00)
ON CONFLICT (cst_icms) DO NOTHING;

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
    COALESCE(t.aliquota_estimada, 13.45) AS aliquota_aplicada,
    ROUND(p.preco_venda * COALESCE(t.aliquota_estimada, 13.45) / 100, 2) AS valor_imposto_item
FROM 
    public.tabela_produtos p
LEFT JOIN 
    public.config_tributaria t ON p.cst_icms = t.cst_icms;

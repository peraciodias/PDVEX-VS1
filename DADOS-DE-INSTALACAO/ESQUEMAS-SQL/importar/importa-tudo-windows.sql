BEGIN;

  -- 1. DESATIVA TEMPORARIAMENTE OS GATILHOS E VALIDAÇÕES DE CHAVES EM TODAS AS TABELAS
  ALTER TABLE public.tabela_usuarios DISABLE TRIGGER ALL;
  ALTER TABLE public.tabela_clientes DISABLE TRIGGER ALL;
  ALTER TABLE public.tabela_clientes_pj DISABLE TRIGGER ALL;
  ALTER TABLE public.tabela_fornecedores DISABLE TRIGGER ALL;
  ALTER TABLE public.tabela_produtos DISABLE TRIGGER ALL;

  -- 2. EXECUTA AS CARGAS DOS ARQUIVOS (ORDEM LOGICA COM ENCODING LATIN1 DEFINITIVO)
  
  -- Usuários
  COPY public.tabela_usuarios (nome, login, senha, perfil, ativo) 
  FROM 'C:\Users\Public\backup_usuarios.csv' 
  WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

  -- Clientes Pessoa Física
  COPY public.tabela_clientes (nome, cpf, rg, telefone, email, endereco, numero, bairro, cidade, uf, cep, limite_credito, data_cadastro) 
  FROM 'C:\Users\Public\backup_clientes_pf.csv' 
  WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

  -- Clientes Pessoa Jurídica
  COPY public.tabela_clientes_pj (razao_social, nome_fantasia, cnpj, ie, telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep, limite_credito, data_cadastro) 
  FROM 'C:\Users\Public\backup_clientes_pj.csv' 
  WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

  -- Fornecedores
  COPY public.tabela_fornecedores (razao_social, nome_fantasia, cnpj, ie, contato, telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep, limite_credito, data_cadastro) 
  FROM 'C:\Users\Public\backup_fornecedores.csv' 
  WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

  -- Produtos
  COPY public.tabela_produtos (codigo_barra, origem, descricao, marca, atributos, unidade_medida, categoria, cod_grupo, grupo, tipo_balanca, quantidade_estoque, preco_custo, preco_venda, ncm, cest, cfop_padrao, unidade_tributavel, cean_tributavel, cst_icms, aliquota_icms, cst_pis, ppis, cst_cofins, pcofins, data_cadastro, loja) 
  FROM 'C:\Users\Public\backup_produtos.csv' 
  WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

  -- 3. REATIVA TODAS AS TRAVAS DE SEGURANÇA E INTEGRIDADE DO BANCO
  ALTER TABLE public.tabela_usuarios ENABLE TRIGGER ALL;
  ALTER TABLE public.tabela_clientes ENABLE TRIGGER ALL;
  ALTER TABLE public.tabela_clientes_pj ENABLE TRIGGER ALL;
  ALTER TABLE public.tabela_fornecedores ENABLE TRIGGER ALL;
  ALTER TABLE public.tabela_produtos ENABLE TRIGGER ALL;

COMMIT;

--DADOS FICTICIOS PARA ESTABELECIMENTO PODEM SER INSERIDOS AQUI
--preenche colunas com '50.00'
UPDATE tabela_produtos SET quantidade_estoque = 50.00;

UPDATE tabela_produtos SET preco_custo = floor(random() * 50 + 1)::int;

UPDATE tabela_produtos SET preco_venda = preco_custo + (preco_custo * 0.50);




--REGIME TRIBUTARIO NESTE CASO = 3 
INSERT INTO public.tabela_estabelecimento (
    razao_social,
    nome_fantasia,
    cnpj,
    inscricao_estadual,
    logradouro,
    numero,
    bairro,
    cidade,
    estado,
    regime_tributario,
    aliq_ibpt
) VALUES 
(
    'Mercado PERA EIRELI',
    'Mercado PERA',
    '98.765.432/0001-10',
    '987654321',
    'Avenida Paraná',
    '2450',
    'Jardim Eliana',
    'Sorocaba',
    'SP',
    3,
    13.45
);



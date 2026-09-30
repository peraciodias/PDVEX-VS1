BEGIN;
-- 1. Desativa triggers
ALTER TABLE public.tabela_clientes DISABLE TRIGGER ALL;
ALTER TABLE public.tabela_clientes_pj DISABLE TRIGGER ALL;
ALTER TABLE public.tabela_fornecedores DISABLE TRIGGER ALL;
ALTER TABLE public.tabela_produtos DISABLE TRIGGER ALL;

-- 2. Carga de dados — cada comando numa linha só

\COPY public.tabela_clientes (nome, cpf, rg, telefone, email, endereco, numero, bairro, cidade, uf, cep, limite_credito, data_cadastro) FROM 'C:\temp\backup_clientes_pf.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

\COPY public.tabela_clientes_pj (razao_social, nome_fantasia, cnpj, ie, telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep, limite_credito, data_cadastro) FROM 'C:\temp\backup_clientes_pj.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

\COPY public.tabela_fornecedores (razao_social, nome_fantasia, cnpj, ie, contato, telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep, limite_credito, data_cadastro) FROM 'C:\temp\backup_fornecedores.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

\COPY public.tabela_produtos (codigo_barra, origem, descricao, marca, atributos, unidade_medida, categoria, cod_grupo, grupo, tipo_balanca, quantidade_estoque, preco_custo, preco_venda, ncm, cest, cfop_padrao, unidade_tributavel, cean_tributavel, cst_icms, aliquota_icms, cst_pis, ppis, cst_cofins, pcofins, data_cadastro, loja) FROM 'C:\temp\bkp_produtos_limpos.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'LATIN1');

-- 3. Reativa triggers
ALTER TABLE public.tabela_clientes ENABLE TRIGGER ALL;
ALTER TABLE public.tabela_clientes_pj ENABLE TRIGGER ALL;
ALTER TABLE public.tabela_fornecedores ENABLE TRIGGER ALL;
ALTER TABLE public.tabela_produtos ENABLE TRIGGER ALL;
COMMIT;
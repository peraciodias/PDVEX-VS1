COPY public.tabela_clientes (nome, cpf, rg, telefone, email, endereco, numero, bairro, cidade, uf, cep, limite_credito, data_cadastro) FROM '/tmp/backup_clientes_pf.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'UTF8');
COPY public.tabela_clientes_pj (razao_social, nome_fantasia, cnpj, ie, telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep, limite_credito, data_cadastro) FROM '/tmp/backup_clientes_pj.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'UTF8');
COPY public.tabela_fornecedores (razao_social, nome_fantasia, cnpj, ie, contato, telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep, limite_credito, data_cadastro) FROM '/tmp/backup_fornecedores.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'UTF8');
COPY public.tabela_usuarios (nome, login, senha, perfil, ativo) FROM '/tmp/backup_usuarios.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'UTF8');
COPY public.tabela_produtos (codigo_barra, origem, descricao, marca, atributos, unidade_medida, categoria, cod_grupo, grupo, tipo_balanca, quantidade_estoque, preco_custo, preco_venda, ncm, cest, cfop_padrao, unidade_tributavel, cean_tributavel, cst_icms, aliquota_icms, cst_pis, ppis, cst_cofins, pcofins, data_cadastro, loja) FROM '/tmp/backup_produtos.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'UTF8');

--insere dados ficticios em preco_custo até R$50,00, preco_venda=preço_custo+50%  e quantidade = 50
UPDATE tabela_produtos SET quantidade_estoque = 50.00;

UPDATE tabela_produtos SET preco_custo = floor(random() * 50 + 1)::int;
UPDATE tabela_produtos SET preco_venda = ROUND(preco_custo + (preco_custo * 0.50), 2);


--DADOS FICTICIOS PARA ESTABELECIMENTO PODEM SER INSERIDOS AQUI
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

COPY public.tabela_produtos (
    codigo_barra, origem, descricao, marca, atributos, unidade_medida, 
    categoria, cod_grupo, grupo, tipo_balanca, quantidade_estoque, 
    preco_custo, preco_venda, ncm, cest, cfop_padrao, unidade_tributavel, 
    cean_tributavel, cst_icms, aliquota_icms, cst_pis, ppis, 
    cst_cofins, pcofins, data_cadastro, loja
) 
TO '/tmp/backup_produtos_completo.csv' 
WITH (
    FORMAT CSV, 
    HEADER, 
    DELIMITER ';', 
    QUOTE '"', 
    FORCE_QUOTE (descricao, marca, atributos, categoria, grupo, unidade_medida), 
    ENCODING 'UTF8'
);

--ou em uma única linha para exportar
COPY public.tabela_produtos (codigo_barra, origem, descricao, marca, atributos, unidade_medida, categoria, cod_grupo, grupo, tipo_balanca, quantidade_estoque, preco_custo, preco_venda, ncm, cest, cfop_padrao, unidade_tributavel, cean_tributavel, cst_icms, aliquota_icms, cst_pis, ppis, cst_cofins, pcofins, data_cadastro, loja) TO '/tmp/backup_produtos.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', FORCE_QUOTE (descricao, marca, atributos, categoria, grupo, unidade_medida), ENCODING 'UTF8');
--para restaurar com import use 
COPY public.tabela_produtos (codigo_barra, origem, descricao, marca, atributos, unidade_medida, categoria, cod_grupo, grupo, tipo_balanca, quantidade_estoque, preco_custo, preco_venda, ncm, cest, cfop_padrao, unidade_tributavel, cean_tributavel, cst_icms, aliquota_icms, cst_pis, ppis, cst_cofins, pcofins, data_cadastro, loja) FROM '/tmp/backup_produtos.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', ENCODING 'UTF8');


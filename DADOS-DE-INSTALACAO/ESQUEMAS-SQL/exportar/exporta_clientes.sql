COPY public.tabela_clientes TO '/tmp/backup_clientes_pf.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', FORCE_QUOTE (nome, email, endereco, bairro, cidade), ENCODING 'UTF8');

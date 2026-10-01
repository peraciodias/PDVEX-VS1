COPY public.tabela_clientes_pj TO '/tmp/backup_clientes_pj.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', FORCE_QUOTE (razao_social, nome_fantasia, endereco), ENCODING 'UTF8');

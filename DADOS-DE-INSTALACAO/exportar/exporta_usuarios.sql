COPY public.tabela_usuarios TO '/tmp/backup_usuarios.csv' WITH (FORMAT CSV, HEADER, DELIMITER ';', QUOTE '"', FORCE_QUOTE (nome, login, senha, perfil), ENCODING 'UTF8');

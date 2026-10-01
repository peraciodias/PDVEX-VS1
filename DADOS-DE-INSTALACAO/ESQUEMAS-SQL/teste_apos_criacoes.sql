-- Deve retornar 1096
SELECT COUNT(*) FROM config_tributaria;

-- Deve retornar 14
SELECT character_maximum_length 
FROM information_schema.columns 
WHERE table_name='tabela_clientes' AND column_name='cpf';


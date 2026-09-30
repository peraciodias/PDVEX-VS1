--apaga tabelas e indices
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-- conecte no banco de dados
\c banco_dados_mercado

-- crie um novas tabelas

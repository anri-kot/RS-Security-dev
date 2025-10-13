-- Remover comandos MySQL
-- CREATE DATABASE e USE não são necessários
-- COLLATE, ENGINE, SQL_MODE também não existem no H2

DROP TABLE IF EXISTS item_venda;
DROP TABLE IF EXISTS item_compra;
DROP TABLE IF EXISTS venda;
DROP TABLE IF EXISTS compra;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS fornecedor;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS categoria;

CREATE TABLE categoria (
  id_categoria BIGINT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255) UNIQUE
);

CREATE TABLE fornecedor (
  id_fornecedor BIGINT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255),
  cnpj VARCHAR(14) UNIQUE,
  telefone VARCHAR(14) UNIQUE,
  email VARCHAR(255) UNIQUE
);

CREATE TABLE usuario (
  id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(20) UNIQUE,
  senha VARCHAR(255),
  nome VARCHAR(255),
  sobrenome VARCHAR(255),
  cpf VARCHAR(11) UNIQUE,
  email VARCHAR(255),
  endereco VARCHAR(255),
  telefone VARCHAR(14),
  salario DECIMAL(10, 0),
  admin BOOLEAN
);

CREATE TABLE produto (
  id_produto BIGINT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255),
  codigo_barras VARCHAR(50),
  preco_atual DECIMAL(10,2),
  descricao VARCHAR(150),
  estoque INT DEFAULT 0,
  id_categoria BIGINT,
  CONSTRAINT fk_categoria_produto FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE SET NULL
);

CREATE TABLE compra (
  id_compra BIGINT PRIMARY KEY AUTO_INCREMENT,
  data TIMESTAMP,
  observacao VARCHAR(255),
  id_fornecedor BIGINT,
  CONSTRAINT fk_fornecedor_compra FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id_fornecedor) ON DELETE SET NULL
);

CREATE TABLE item_compra (
  id_item BIGINT PRIMARY KEY AUTO_INCREMENT,
  quantidade SMALLINT,
  valor_unitario DECIMAL(10,2),
  id_compra BIGINT,
  id_produto BIGINT,
  CONSTRAINT fk_compra_itemcom FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON DELETE CASCADE,
  CONSTRAINT fk_produto_itemcom FOREIGN KEY (id_produto) REFERENCES produto(id_produto) ON DELETE SET NULL
);

CREATE TABLE venda (
  id_venda BIGINT PRIMARY KEY AUTO_INCREMENT,
  data TIMESTAMP,
  observacao VARCHAR(255) DEFAULT 'VENDA NO BALCÃO',
  metodo_pagamento VARCHAR(20),
  valor_recebido DECIMAL(10,2),
  troco DECIMAL(10,2),
  id_usuario BIGINT,
  CONSTRAINT fk_funcionario_venda FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE SET NULL
);

CREATE TABLE item_venda (
  id_item BIGINT PRIMARY KEY AUTO_INCREMENT,
  quantidade INT,
  valor_unitario DECIMAL(10,2),
  desconto DECIMAL(5,2),
  id_venda BIGINT,
  id_produto BIGINT,
  CONSTRAINT fk_venda_itemven FOREIGN KEY (id_venda) REFERENCES venda(id_venda) ON DELETE CASCADE,
  CONSTRAINT fk_produto_itemven FOREIGN KEY (id_produto) REFERENCES produto(id_produto) ON DELETE SET NULL
);

-- view simplificada só para teste
CREATE VIEW vw_lucro_produto AS
SELECT 
    p.id_produto,
    p.nome,
    SUM(iv.quantidade * iv.valor_unitario) AS receita,
    SUM(iv.quantidade * p.preco_atual) AS custo,
    SUM(iv.quantidade * iv.valor_unitario) - SUM(iv.quantidade * p.preco_atual) AS lucro
FROM item_venda iv
JOIN produto p ON p.id_produto = iv.id_produto
GROUP BY p.id_produto, p.nome;
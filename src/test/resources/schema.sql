-- Limpeza
DROP VIEW IF EXISTS vw_lucro_venda;
DROP VIEW IF EXISTS vw_custo_compra;

DROP TABLE IF EXISTS item_venda;
DROP TABLE IF EXISTS item_compra;
DROP TABLE IF EXISTS venda;
DROP TABLE IF EXISTS compra;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS fornecedor;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS categoria;

-- Tabelas
CREATE TABLE categoria (
  id_categoria BIGINT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL
);

CREATE TABLE fornecedor (
  id_fornecedor BIGINT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  cnpj VARCHAR(14) NOT NULL,
  telefone VARCHAR(14) NOT NULL,
  email VARCHAR(255) NOT NULL
);

CREATE TABLE compra (
  id_compra BIGINT PRIMARY KEY,
  data TIMESTAMP NOT NULL,
  observacao VARCHAR(255),
  id_fornecedor BIGINT,
  FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id_fornecedor)
);

CREATE TABLE produto (
  id_produto BIGINT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  codigo_barras VARCHAR(50),
  preco_atual DECIMAL(10,2),
  descricao VARCHAR(150),
  estoque INT DEFAULT 0 NOT NULL,
  id_categoria BIGINT,
  FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE item_compra (
  id_item BIGINT PRIMARY KEY,
  quantidade SMALLINT NOT NULL,
  valor_unitario DECIMAL(10,2) NOT NULL,
  id_compra BIGINT NOT NULL,
  id_produto BIGINT,
  FOREIGN KEY (id_compra) REFERENCES compra(id_compra),
  FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);

CREATE TABLE usuario (
  id_usuario BIGINT PRIMARY KEY,
  username VARCHAR(20) NOT NULL,
  senha VARCHAR(255) NOT NULL,
  nome VARCHAR(255) NOT NULL,
  sobrenome VARCHAR(255) NOT NULL,
  cpf VARCHAR(11) NOT NULL,
  email VARCHAR(255),
  endereco VARCHAR(255) NOT NULL,
  telefone VARCHAR(14),
  salario DECIMAL(10,2) NOT NULL,
  ativo BOOLEAN DEFAULT FALSE,
  admin BOOLEAN NOT NULL
);

CREATE TABLE venda (
  id_venda BIGINT PRIMARY KEY,
  data TIMESTAMP NOT NULL,
  observacao VARCHAR(255) DEFAULT 'VENDA NO BALCÃO',
  metodo_pagamento VARCHAR(20),
  valor_recebido DECIMAL(10,2),
  troco DECIMAL(10,2) DEFAULT 0.00 NOT NULL,
  id_usuario BIGINT,
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE item_venda (
  id_item BIGINT PRIMARY KEY,
  quantidade INT NOT NULL,
  valor_unitario DECIMAL(10,2) NOT NULL,
  desconto DECIMAL(5,2),
  id_venda BIGINT NOT NULL,
  id_produto BIGINT,
  FOREIGN KEY (id_venda) REFERENCES venda(id_venda),
  FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);

-- Views (simplificadas, compatíveis com H2)
CREATE VIEW vw_custo_compra AS
SELECT 
    c.id_compra,
    c.data,
    COALESCE(SUM(ic.quantidade * ic.valor_unitario), 0) AS custo_total
FROM compra c
JOIN item_compra ic ON ic.id_compra = c.id_compra
GROUP BY c.id_compra, c.data;

CREATE VIEW vw_lucro_venda AS
SELECT 
    v.id_venda,
    v.data,
    COALESCE(SUM(iv.quantidade * iv.valor_unitario * (1 - COALESCE(iv.desconto / 100, 0))), 0) AS receita_total,
    COALESCE(SUM(iv.quantidade * ic.avg_custo), 0) AS custo_total,
    COALESCE(SUM(iv.quantidade * iv.valor_unitario * (1 - COALESCE(iv.desconto / 100, 0))), 0)
      - COALESCE(SUM(iv.quantidade * ic.avg_custo), 0) AS lucro_total,
    CASE 
      WHEN COALESCE(SUM(iv.quantidade * ic.avg_custo), 0) = 0 THEN NULL
      ELSE ROUND(
        (
          COALESCE(SUM(iv.quantidade * iv.valor_unitario * (1 - COALESCE(iv.desconto / 100, 0))), 0)
          - COALESCE(SUM(iv.quantidade * ic.avg_custo), 0)
        ) / COALESCE(SUM(iv.quantidade * ic.avg_custo), 1) * 100, 2
      )
    END AS lucro_percentual
FROM venda v
JOIN item_venda iv ON iv.id_venda = v.id_venda
LEFT JOIN (
    SELECT id_produto, AVG(valor_unitario) AS avg_custo
    FROM item_compra
    GROUP BY id_produto
) ic ON ic.id_produto = iv.id_produto
GROUP BY v.id_venda, v.data;

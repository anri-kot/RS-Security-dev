-- Inserir vendas primeiro
INSERT INTO venda (id_venda) VALUES (1);
INSERT INTO venda (id_venda) VALUES (2);
INSERT INTO venda (id_venda) VALUES (3);

-- Produtos (id_produto, nome, preco_atual, estoque)
INSERT INTO produto (id_produto, nome, preco_atual, estoque)
VALUES (1, 'Camiseta', 50.00, 100);

INSERT INTO produto (id_produto, nome, preco_atual, estoque)
VALUES (2, 'Calça Jeans', 120.00, 50);

INSERT INTO produto (id_produto, nome, preco_atual, estoque)
VALUES (3, 'Tênis', 200.00, 30);

-- Vendas (id_produto, quantidade, valor_unitario, id_venda)
INSERT INTO item_venda (id_produto, quantidade, valor_unitario, id_venda)
VALUES (1, 2, 80.00, 1);

INSERT INTO item_venda (id_produto, quantidade, valor_unitario, id_venda)
VALUES (2, 1, 150.00, 2);

INSERT INTO item_venda (id_produto, quantidade, valor_unitario, id_venda)
VALUES (3, 1, 250.00, 3);

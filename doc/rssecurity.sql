-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: mysql:3306
-- Generation Time: Jul 02, 2025 at 08:23 PM
-- Server version: 9.3.0
-- PHP Version: 8.2.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rssecurity`
--
CREATE DATABASE IF NOT EXISTS `rssecurity` DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci;
USE `rssecurity`;

-- --------------------------------------------------------

--
-- Table structure for table `categoria`
--

CREATE TABLE `categoria` (
  `id_categoria` bigint NOT NULL,
  `nome` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `categoria`
--

INSERT INTO `categoria` (`id_categoria`, `nome`) VALUES
(7, 'alarme'),
(8, 'aparelho eletronico'),
(6, 'cabos e fios'),
(1, 'Camera'),
(3, 'Perifericos'),
(4, 'Redes'),
(5, 'sensor'),
(2, 'Vigilancia');

-- --------------------------------------------------------

--
-- Table structure for table `compra`
--

CREATE TABLE `compra` (
  `id_compra` bigint NOT NULL,
  `data` datetime NOT NULL,
  `observacao` varchar(255) DEFAULT NULL,
  `id_fornecedor` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `compra`
--

INSERT INTO `compra` (`id_compra`, `data`, `observacao`, `id_fornecedor`) VALUES
(5, '2025-04-17 14:30:00', 'Compra de câmeras para novo lote de segurança', 2),
(6, '2025-04-16 10:15:00', 'Reposição mensal de produtos de vigilância', 3),
(7, '2025-04-15 17:45:00', 'Compra emergencial para novo cliente', 4),
(8, '2025-04-24 12:00:00', NULL, 2),
(9, '2025-06-27 12:00:00', 'Estoque Mensal', 3);

-- --------------------------------------------------------

--
-- Table structure for table `fornecedor`
--

CREATE TABLE `fornecedor` (
  `id_fornecedor` bigint NOT NULL,
  `nome` varchar(255) NOT NULL,
  `cnpj` varchar(14) NOT NULL,
  `telefone` varchar(14) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `fornecedor`
--

INSERT INTO `fornecedor` (`id_fornecedor`, `nome`, `cnpj`, `telefone`, `email`) VALUES
(2, 'John Teste Tomas Marques', '85858585', '36363636', 'email@hotmal'),
(3, 'Amaterasu Doe Rance', '85252525', '9999999', 'race@email'),
(4, 'Kennedy Doe Lucas', '85246582', '8885545', 'djsakdjsak@email'),
(7, 'Ama Terasu Doe', '85246555', '88851111', '456789798ak@email');

-- --------------------------------------------------------

--
-- Table structure for table `item_compra`
--

CREATE TABLE `item_compra` (
  `id_item` bigint NOT NULL,
  `quantidade` smallint NOT NULL,
  `valor_unitario` decimal(10,2) NOT NULL,
  `id_compra` bigint NOT NULL,
  `id_produto` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `item_compra`
--

INSERT INTO `item_compra` (`id_item`, `quantidade`, `valor_unitario`, `id_compra`, `id_produto`) VALUES
(3, 100, 250.00, 5, 4),
(4, 100, 180.00, 5, 8),
(5, 100, 75.00, 6, 6),
(6, 100, 210.00, 7, 5),
(7, 100, 300.00, 7, 7),
(8, 100, 70.00, 8, 9),
(9, 100, 170.00, 8, 10),
(10, 100, 310.00, 8, 11),
(11, 50, 70.00, 9, 9);

-- --------------------------------------------------------

--
-- Table structure for table `item_venda`
--

CREATE TABLE `item_venda` (
  `id_item` bigint NOT NULL,
  `quantidade` int NOT NULL,
  `valor_unitario` decimal(10,2) NOT NULL,
  `desconto` decimal(5,2) DEFAULT NULL,
  `id_venda` bigint NOT NULL,
  `id_produto` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `item_venda`
--

INSERT INTO `item_venda` (`id_item`, `quantidade`, `valor_unitario`, `desconto`, `id_venda`, `id_produto`) VALUES
(3, 5, 75.00, 0.00, 3, 5),
(5, 2, 320.00, 0.00, 5, 11),
(6, 6, 275.00, 0.00, 6, 4),
(7, 2, 120.00, 0.00, 6, 12),
(8, 2, 70.00, 0.00, 7, 9),
(9, 1, 170.00, 15.00, 7, 10),
(10, 1, 200.00, 0.00, 9, 4),
(11, 1, 170.00, 0.00, 10, 10),
(12, 1, 69.99, 0.00, 10, 6),
(13, 5, 310.00, 0.00, 11, 11),
(14, 1, 170.00, 0.00, 11, 10),
(15, 1, 500.00, 0.00, 12, 7),
(16, 1, 69.99, 0.00, 12, 6),
(17, 1, 200.00, 0.00, 13, 8),
(18, 1, 170.00, 0.00, 14, 10),
(19, 1, 170.00, 0.00, 15, 10),
(20, 3, 200.00, 0.00, 16, 4),
(21, 1, 200.00, 0.00, 17, 4),
(22, 1, 170.00, 0.00, 18, 10),
(23, 1, 310.00, 0.00, 19, 11),
(24, 1, 170.00, 0.00, 20, 10),
(25, 1, 170.00, 0.00, 21, 10),
(26, 1, 200.00, 0.00, 22, 4),
(27, 1, 200.00, 15.00, 23, 4),
(28, 1, 310.00, 0.00, 24, 11),
(29, 3, 500.00, 0.00, 25, 7),
(30, 1, 310.00, 0.00, 25, 11),
(33, 1, 200.00, 0.00, 27, 4),
(34, 1, 310.00, 0.00, 27, 11),
(35, 2, 200.00, 0.00, 3, 4),
(36, 1, 200.00, 0.00, 28, 4),
(37, 1, 310.00, 0.00, 28, 11);

-- --------------------------------------------------------

--
-- Table structure for table `produto`
--

CREATE TABLE `produto` (
  `id_produto` bigint NOT NULL,
  `nome` varchar(255) NOT NULL,
  `preco_atual` decimal(10,2) DEFAULT NULL,
  `descricao` varchar(150) DEFAULT NULL,
  `estoque` int NOT NULL DEFAULT '0',
  `id_categoria` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `produto`
--

INSERT INTO `produto` (`id_produto`, `nome`, `preco_atual`, `descricao`, `estoque`, `id_categoria`) VALUES
(4, 'Camera SPX', 200.00, 'Camera Profissional', 200, 1),
(5, 'Produto Vigilancia', 200.00, 'Monitoramento', 200, 1),
(6, 'Camera 33', 69.99, 'Camera de baixa qualidade', 200, 2),
(7, 'Camera DSA', 500.00, 'Camera de alta qualidade', 200, 2),
(8, 'Camera Super HD', 200.00, 'Camera de media qualidade', 200, 1),
(9, 'Mouse sem fio', 70.00, 'Mouse silencioso com sensor óptico', 250, 3),
(10, 'Teclado Mecânico Redragon', 170.00, 'Teclado RGB com switches silenciosos', 200, 3),
(11, 'Roteador AX3600', 310.00, 'Roteador Wi-Fi 6 de alta performance', 200, 4),
(12, 'Gravador DVR', 110.00, 'Gravador digital para câmeras analógicas', 200, 2),
(85, 'Câmera de Segurança IP Wi-Fi', 0.00, 'Câmera IP com visão noturna e detecção de movimento', 50, 1),
(86, 'Notebook Dell Inspiron 15', 0.00, 'Notebook com processador Intel i5 8GB RAM e SSD de 256GB', 20, 8),
(87, '5 de Presença Infravermelho', 0.00, '5 para automação de iluminação com ajuste de sensibilidade', 100, 5),
(88, 'Fone de Ouvido Bluetooth JBL', 0.00, 'Fone sem fio com até 20h de bateria e microfone embutido', 35, 3),
(89, 'Cabo HDMI 2.0 2 Metros', 0.00, 'Cabo HDMI de alta velocidade com suporte a 4K', 200, 6),
(90, '7 Residencial Intelbras', 0.00, 'Central de 7 com teclado e controle remoto', 15, 7),
(91, 'Smartphone Samsung Galaxy A15', 0.00, 'Smartphone com 128GB de armazenamento e câmera tripla', 25, 8),
(92, 'Tomada Inteligente Wi-Fi', 0.00, 'Controle de energia via app com integração Alexa e Google', 60, 3),
(93, 'HD Externo 1TB Seagate', 0.00, 'Armazenamento portátil com conexão USB 3.0', 40, 3),
(94, 'Kit Vídeo Porteiro com Câmera', 0.00, 'Kit com câmera externa e monitor interno para interfone', 10, 1);

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` bigint NOT NULL,
  `username` varchar(20) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `sobrenome` varchar(255) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `endereco` varchar(255) NOT NULL,
  `telefone` varchar(14) DEFAULT NULL,
  `salario` decimal(10,0) NOT NULL,
  `admin` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `username`, `senha`, `nome`, `sobrenome`, `cpf`, `email`, `endereco`, `telefone`, `salario`, `admin`) VALUES
(2, 'admin2', '$2a$12$EFq50CGeYPJ6OfewFmu18e0gzsO419HROQ/dPtWBN1KHnA41fpaqC', 'Admin 2', 'Administrator', '11111111111', 'admin2@email.com', 'Admin 2 Residence', '11111111111111', 1000, 1),
(3, 'user', '$2a$12$OmjAQARhheggPgbAhxtxjemtbJU7k04dsVnAfYCKy/jkDRd3iorXK', 'John', 'Doe', '1122334455', 'johndoe@email', 'Brasil', '987123456', 3000, 0),
(4, 'teste', '$2a$10$Fe.rh8KU8ILhhQJAAq3iZepdoPdM.MqDvt/6Zf2FBi2MgJOceHk5K', 'Teste', 'Teste', '369369369', 'teste@email', 'Brasil', '369258369', 3000, 0);

-- --------------------------------------------------------

--
-- Table structure for table `venda`
--

CREATE TABLE `venda` (
  `id_venda` bigint NOT NULL,
  `data` datetime NOT NULL,
  `observacao` varchar(255) NOT NULL DEFAULT 'VENDA NO BALCÃO',
  `metodo_pagamento` varchar(20) DEFAULT NULL,
  `valor_recebido` decimal(10,2) DEFAULT NULL,
  `troco` decimal(10,2) DEFAULT NULL,
  `id_usuario` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `venda`
--

INSERT INTO `venda` (`id_venda`, `data`, `observacao`, `metodo_pagamento`, `valor_recebido`, `troco`, `id_usuario`) VALUES
(3, '2025-04-10 16:45:00', 'VENDA NO BALCÃO', 'DINHEIRO', 785.00, 10.00, 3),
(5, '2025-04-18 18:00:00', 'Venda de equipamentos de rede para filial norte', 'CARTAO_CREDITO', 640.00, NULL, 2),
(6, '2025-04-20 20:15:00', 'Venda de kit completo para instalação de segurança', 'CARTAO_CREDITO', 1890.00, NULL, 4),
(7, '2025-04-24 16:45:00', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 284.50, NULL, 2),
(9, '2025-05-02 18:10:50', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 200.00, NULL, 3),
(10, '2025-05-02 15:21:41', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 239.99, NULL, 3),
(11, '2025-05-02 15:27:16', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 1720.00, NULL, 3),
(12, '2025-05-02 15:29:19', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 569.99, NULL, 3),
(13, '2025-05-02 15:45:14', 'VENDA NO BALCÃO', 'PIX', 200.00, NULL, 3),
(14, '2025-05-02 15:47:53', 'VENDA NO BALCÃO', 'PIX', 170.00, NULL, 3),
(15, '2025-05-02 15:47:59', 'VENDA NO BALCÃO', 'PIX', 170.00, NULL, 3),
(16, '2025-05-05 16:18:54', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 600.00, NULL, 3),
(17, '2025-05-06 16:52:46', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 200.00, NULL, 2),
(18, '2025-05-06 16:54:26', 'teclado', 'PIX', 170.00, NULL, 2),
(19, '2025-05-06 16:59:37', 'teste', 'CARTAO_CREDITO', 310.00, NULL, 2),
(20, '2025-05-09 15:08:05', 'VENDA NO BALCÃO', 'DINHEIRO', 180.00, 10.00, 3),
(21, '2025-05-09 15:08:36', 'dsadsadsa', 'CARTAO_CREDITO', 170.00, NULL, 3),
(22, '2025-05-12 16:40:36', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 200.00, NULL, 3),
(23, '2025-05-13 14:41:59', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 170.00, NULL, 3),
(24, '2025-05-22 16:42:12', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 310.00, NULL, 3),
(25, '2025-05-26 22:00:23', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 1810.00, NULL, 3),
(27, '2025-05-27 17:27:00', 'BURY THE LIGHT DEEP WITHIIIN', 'CARTAO_CREDITO', 510.00, NULL, 3),
(28, '2025-05-27 20:26:42', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 510.00, NULL, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`id_categoria`),
  ADD UNIQUE KEY `nome` (`nome`);

--
-- Indexes for table `compra`
--
ALTER TABLE `compra`
  ADD PRIMARY KEY (`id_compra`),
  ADD KEY `fk_fornecedor_compra` (`id_fornecedor`);

--
-- Indexes for table `fornecedor`
--
ALTER TABLE `fornecedor`
  ADD PRIMARY KEY (`id_fornecedor`),
  ADD UNIQUE KEY `CNPJ` (`cnpj`),
  ADD UNIQUE KEY `EMAIL_FORNECEDOR` (`email`),
  ADD UNIQUE KEY `CNPJ_FORNECEDOR` (`cnpj`),
  ADD UNIQUE KEY `telefone` (`telefone`);

--
-- Indexes for table `item_compra`
--
ALTER TABLE `item_compra`
  ADD PRIMARY KEY (`id_item`),
  ADD KEY `fk_compra_itemcom` (`id_compra`),
  ADD KEY `fk_produto_itemcom` (`id_produto`);

--
-- Indexes for table `item_venda`
--
ALTER TABLE `item_venda`
  ADD PRIMARY KEY (`id_item`),
  ADD KEY `fk_produto_itemven` (`id_produto`),
  ADD KEY `fk_venda_itemven` (`id_venda`);

--
-- Indexes for table `produto`
--
ALTER TABLE `produto`
  ADD PRIMARY KEY (`id_produto`),
  ADD KEY `fk_categoria_produto` (`id_categoria`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `cpf` (`cpf`) USING BTREE,
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `telefone` (`telefone`);

--
-- Indexes for table `venda`
--
ALTER TABLE `venda`
  ADD PRIMARY KEY (`id_venda`),
  ADD KEY `fk_funcionario_venda` (`id_usuario`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categoria`
--
ALTER TABLE `categoria`
  MODIFY `id_categoria` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `compra`
--
ALTER TABLE `compra`
  MODIFY `id_compra` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `fornecedor`
--
ALTER TABLE `fornecedor`
  MODIFY `id_fornecedor` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `item_compra`
--
ALTER TABLE `item_compra`
  MODIFY `id_item` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `item_venda`
--
ALTER TABLE `item_venda`
  MODIFY `id_item` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `produto`
--
ALTER TABLE `produto`
  MODIFY `id_produto` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=95;

--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `venda`
--
ALTER TABLE `venda`
  MODIFY `id_venda` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `compra`
--
ALTER TABLE `compra`
  ADD CONSTRAINT `fk_fornecedor_compra` FOREIGN KEY (`id_fornecedor`) REFERENCES `fornecedor` (`id_fornecedor`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `item_compra`
--
ALTER TABLE `item_compra`
  ADD CONSTRAINT `fk_compra_itemcom` FOREIGN KEY (`id_compra`) REFERENCES `compra` (`id_compra`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_produto_itemcom` FOREIGN KEY (`id_produto`) REFERENCES `produto` (`id_produto`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `item_venda`
--
ALTER TABLE `item_venda`
  ADD CONSTRAINT `fk_produto_itemven` FOREIGN KEY (`id_produto`) REFERENCES `produto` (`id_produto`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_venda_itemven` FOREIGN KEY (`id_venda`) REFERENCES `venda` (`id_venda`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `produto`
--
ALTER TABLE `produto`
  ADD CONSTRAINT `fk_categoria_produto` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `venda`
--
ALTER TABLE `venda`
  ADD CONSTRAINT `fk_funcionario_venda` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

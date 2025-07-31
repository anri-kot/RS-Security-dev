-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 29, 2025 at 04:11 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

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
CREATE DATABASE IF NOT EXISTS `rssecurity` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `rssecurity`;

-- --------------------------------------------------------

--
-- Table structure for table `categoria`
--

CREATE TABLE `categoria` (
  `id_categoria` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `categoria`
--

INSERT INTO `categoria` (`id_categoria`, `nome`) VALUES
(7, 'alarme'),
(8, 'aparelho eletronico'),
(6, 'cabos e fios'),
(1, 'Camera'),
(13, 'LOBOTOMY CORP'),
(3, 'Perifericos'),
(12, 'PRODUTO TESTE'),
(4, 'Redes'),
(5, 'sensor'),
(11, 'TESTE'),
(2, 'Vigilancia');

-- --------------------------------------------------------

--
-- Table structure for table `compra`
--

CREATE TABLE `compra` (
  `id_compra` bigint(20) NOT NULL,
  `data` datetime NOT NULL,
  `observacao` varchar(255) DEFAULT NULL,
  `id_fornecedor` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `compra`
--

INSERT INTO `compra` (`id_compra`, `data`, `observacao`, `id_fornecedor`) VALUES
(5, '2025-04-17 14:30:00', 'Compra de câmeras para novo lote de segurança', 2),
(6, '2025-04-16 10:15:00', 'Reposição mensal de produtos de vigilância', 3),
(7, '2025-04-15 17:45:00', 'Compra emergencial para novo cliente', 4),
(8, '2025-04-24 12:00:00', NULL, 2),
(9, '2025-06-27 12:00:00', 'Estoque Mensal', 3),
(11, '2025-02-19 14:30:00', 'Primeira', 3),
(12, '2025-05-18 10:15:01', 'Segunda', 7),
(13, '2025-07-19 12:00:00', 'OVERCOME', 2),
(14, '2025-07-19 12:39:00', 'KARADA HA', 7),
(15, '2025-07-19 12:00:00', 'COMPRA TESTE', 10),
(16, '2025-07-28 12:00:00', 'COMPRA TESTE', 10),
(17, '2025-07-16 12:00:00', 'dklaskdlsa', 7);

-- --------------------------------------------------------

--
-- Table structure for table `fornecedor`
--

CREATE TABLE `fornecedor` (
  `id_fornecedor` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `cnpj` varchar(14) NOT NULL,
  `telefone` varchar(14) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `fornecedor`
--

INSERT INTO `fornecedor` (`id_fornecedor`, `nome`, `cnpj`, `telefone`, `email`) VALUES
(2, 'John Teste Tomas Marques', '85858585', '36363636', 'email@hotmal'),
(3, 'Amaterasu Doe Rance', '85252525', '9999999', 'race@email'),
(4, 'Kennedy Doe Lucas', '85246582', '8885545', 'djsakdjsak@email'),
(7, 'Ama Terasu Doe', '85246555', '88851111', '456789798ak@email'),
(10, 'John Stuart Mill', '198524562', '852456917', 'johnstuartmill@yahoo.com'),
(11, 'JOHN TESTE', '12345678912', '61223324564', 'johnteste@testemail.com');

-- --------------------------------------------------------

--
-- Table structure for table `item_compra`
--

CREATE TABLE `item_compra` (
  `id_item` bigint(20) NOT NULL,
  `quantidade` smallint(6) NOT NULL,
  `valor_unitario` decimal(10,2) NOT NULL,
  `id_compra` bigint(20) NOT NULL,
  `id_produto` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `item_compra`
--

INSERT INTO `item_compra` (`id_item`, `quantidade`, `valor_unitario`, `id_compra`, `id_produto`) VALUES
(1, 100, 100.00, 14, 6),
(2, 12, 5000.00, 15, 3),
(3, 12, 439.99, 16, 3),
(4, 1, 20.00, 17, NULL),
(5, 1, 20.00, 16, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `item_venda`
--

CREATE TABLE `item_venda` (
  `id_item` bigint(20) NOT NULL,
  `quantidade` int(11) NOT NULL,
  `valor_unitario` decimal(10,2) NOT NULL,
  `desconto` decimal(5,2) DEFAULT NULL,
  `id_venda` bigint(20) NOT NULL,
  `id_produto` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `item_venda`
--

INSERT INTO `item_venda` (`id_item`, `quantidade`, `valor_unitario`, `desconto`, `id_venda`, `id_produto`) VALUES
(1, 1, 123.45, 0.00, 1, 1),
(2, 1, 33000.00, 0.00, 2, 2),
(3, 5, 100.00, 15.00, 3, 6),
(36, 5, 5.00, 0.00, 28, 7),
(37, 2, 10.00, 0.00, 29, NULL),
(38, 4, 5.00, 0.00, 30, 7),
(39, 3, 10.00, 0.00, 30, NULL),
(40, 1, 10.00, 0.00, 31, NULL),
(41, 1, 20.00, 0.00, 31, NULL),
(42, 1, 123.45, 0.00, 31, 1);

-- --------------------------------------------------------

--
-- Table structure for table `produto`
--

CREATE TABLE `produto` (
  `id_produto` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `codigo_barras` varchar(50) DEFAULT NULL,
  `preco_atual` decimal(10,2) DEFAULT NULL,
  `descricao` varchar(150) DEFAULT NULL,
  `estoque` int(11) NOT NULL DEFAULT 0,
  `id_categoria` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `produto`
--

INSERT INTO `produto` (`id_produto`, `nome`, `codigo_barras`, `preco_atual`, `descricao`, `estoque`, `id_categoria`) VALUES
(1, 'MILI IS AWESOME', '37747747712', 123.45, 'city', 14, 11),
(2, 'Children of the city', '333333556', 33000.00, 'children of the', 11, 11),
(3, 'poems of a machine', '52461346', 12000.00, 'night', 26, 12),
(4, 'in hell we live', '6466652', 1100.00, 'lament', 5, 12),
(6, 'NEON GENESIS EVA', '121212121212', 100.00, 'CEM PILA', 115, 11),
(7, 'Produto 5 reais', '8080808080', 5.00, 'produto de 5 reais', 59, 11),
(25, 'Câmera de Segurança AHD', '789456123001', 249.90, 'Câmera infravermelha com visão noturna', 200, 1),
(26, 'Sensor de Movimento PIR', '789456123002', 89.50, 'Sensor para detecção de movimento em ambientes fechados', 200, 5),
(27, 'HD 1TB SATA Vigilância', '789456123003', 399.99, 'Disco rígido específico para sistemas de CFTV', 200, 2),
(28, 'Cabo coaxial 100m', '789456123004', 150.00, 'Cabo coaxial RG59 com malha dupla', 200, 6),
(29, 'Mouse Óptico USB', '789456123005', 35.90, 'Mouse com 1000 DPI ambidestro', 200, 3),
(30, 'Switch 8 portas Gigabit', '789456123006', 249.00, 'Switch de rede para escritório com 8 portas', 200, 4),
(31, 'Central de Alarme X4', '789456123007', 320.00, 'Central com 4 zonas e discadora', 200, 7),
(32, 'NVR 16 canais', '789456123008', 980.00, 'Gravador de vídeo para 16 câmeras IP', 200, 2),
(33, 'Adaptador Wireless USB', '789456123009', 49.99, 'Adaptador Wi-Fi 150Mbps para PC e notebook', 200, 4),
(34, 'Controle Remoto para Alarme', '789456123010', 29.90, 'Controle de ativação/desativação sem fio', 200, 7),
(35, 'Disco SSD 256GB', '789456123011', 199.90, 'SSD para sistema operacional e programas', 200, 3),
(36, 'Monitor 22\" Full HD', '789456123012', 699.00, 'Monitor com entrada HDMI e VGA', 200, 3),
(37, 'Produto Teste 1', '789456123013', 9.99, 'Apenas para testes de importação', 200, 12),
(38, 'Cabo de Rede Cat6', '789456123014', 1.90, 'Cabo de rede por metro', 200, 6),
(39, 'Lobotomy Corp USB Drive', '789456123015', 666.66, 'Dispositivo misterioso e potencialmente perigoso', 200, 13);

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `username`, `senha`, `nome`, `sobrenome`, `cpf`, `email`, `endereco`, `telefone`, `salario`, `admin`) VALUES
(2, 'admin', '$2a$12$aX5VsXRRknFJTq.oO4YFvOmOuiF1PY182Ft1i.342YJIvifRYYlpW', 'Admin', 'Administrator', '11111111111', 'admin@email.com', 'Admin Residence', '11111111111111', 1000, 1),
(3, 'user', '$2a$12$OmjAQARhheggPgbAhxtxjemtbJU7k04dsVnAfYCKy/jkDRd3iorXK', 'John', 'Doe', '1122334455', 'johndoe@email', 'Brasil', '987123456', 3000, 0),
(4, 'teste', '$2a$10$Fe.rh8KU8ILhhQJAAq3iZepdoPdM.MqDvt/6Zf2FBi2MgJOceHk5K', 'Teste', 'Teste', '369369369', 'teste@email', 'Brasil', '369258369', 3000, 0),
(6, 'johnmill', '$2a$12$e2gfS.1cIgtvy/NRdatqg.PU4ITGLcA0j43nKP66qOOUN3ZTlwSK2', 'John', 'Stuart Mill', '798546123', 'email@john', 'America', '134679852', 50000, 1),
(7, 'verybigusername', '$2a$12$NHTJbmgJdMmdUy/8iM93eOwIvJumwfNHEtZ1/vHLjn8vOyyF/kSl6', 'Big', 'Username', '79461385246', 'email@noreplay', 'Addddddd', '146325879', 12000, 1);

-- --------------------------------------------------------

--
-- Table structure for table `venda`
--

CREATE TABLE `venda` (
  `id_venda` bigint(20) NOT NULL,
  `data` datetime NOT NULL,
  `observacao` varchar(255) NOT NULL DEFAULT 'VENDA NO BALCÃO',
  `metodo_pagamento` varchar(20) DEFAULT NULL,
  `valor_recebido` decimal(10,2) DEFAULT NULL,
  `troco` decimal(10,2) NOT NULL DEFAULT 0.00,
  `id_usuario` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `venda`
--

INSERT INTO `venda` (`id_venda`, `data`, `observacao`, `metodo_pagamento`, `valor_recebido`, `troco`, `id_usuario`) VALUES
(1, '2025-07-18 18:53:18', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 123.45, 0.00, 2),
(2, '2025-07-19 12:17:57', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 33000.00, 0.00, 2),
(3, '2025-07-19 12:45:48', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 425.00, 0.00, 2),
(28, '2025-07-21 14:30:00', 'VENDA NO BALCÃO', 'DINHEIRO', 30.00, 5.00, 2),
(29, '2025-07-21 15:00:00', 'VENDA NO BALCÃO', 'PIX', 20.00, 0.00, 2),
(30, '2025-07-21 15:15:00', 'VENDA NO BALCÃO', 'PIX', 50.00, 0.00, 2),
(31, '2025-07-28 21:48:03', 'VENDA NO BALCÃO', 'CARTAO_CREDITO', 153.45, 0.00, 2);

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
  MODIFY `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `compra`
--
ALTER TABLE `compra`
  MODIFY `id_compra` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `fornecedor`
--
ALTER TABLE `fornecedor`
  MODIFY `id_fornecedor` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `item_compra`
--
ALTER TABLE `item_compra`
  MODIFY `id_item` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `item_venda`
--
ALTER TABLE `item_venda`
  MODIFY `id_item` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `produto`
--
ALTER TABLE `produto`
  MODIFY `id_produto` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `venda`
--
ALTER TABLE `venda`
  MODIFY `id_venda` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

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

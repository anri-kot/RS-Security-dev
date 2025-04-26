-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 26, 2025 at 02:22 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

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
(1, 'Camera'),
(2, 'Vigilancia'),
(3, 'Periféricos'),
(4, 'Redes');

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
(8, '2025-04-24 12:00:00', NULL, 2);

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
(2, 'John Doe Teste TM', '85858585', '36363636', 'email@hotmal'),
(3, 'Amaterasu Rance', '8525252', '9999999', 'race@email'),
(4, 'Kennedy Doe Lucas', '85246582', '8885545', 'djsakdjsak@email'),
(7, 'Ama Terasu Doe', '85246555', '8885111', '456789798ak@email');

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
(3, 10, 250.00, 5, 4),
(4, 5, 180.00, 5, 8),
(5, 15, 75.00, 6, 6),
(6, 8, 210.00, 7, 5),
(7, 3, 300.00, 7, 7),
(8, 5, 70.00, 8, 9),
(9, 3, 170.00, 8, 10),
(10, 2, 310.00, 8, 11);

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
(3, 9, 75.00, 15.00, 3, 5),
(4, 2, 180.00, NULL, 3, 10),
(5, 4, 320.00, NULL, 5, 11),
(6, 6, 275.00, NULL, 6, 4),
(7, 2, 120.00, NULL, 6, 12),
(8, 2, 70.00, 0.00, 7, 9),
(9, 1, 170.00, 10.00, 7, 10);

-- --------------------------------------------------------

--
-- Table structure for table `produto`
--

CREATE TABLE `produto` (
  `id_produto` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `preco_atual` decimal(10,2) DEFAULT NULL,
  `descricao` varchar(150) DEFAULT NULL,
  `estoque_min` smallint(6) DEFAULT NULL,
  `id_categoria` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `produto`
--

INSERT INTO `produto` (`id_produto`, `nome`, `preco_atual`, `descricao`, `estoque_min`, `id_categoria`) VALUES
(4, 'Camera SPX', 200.00, 'Camera Profissional', 30, 2),
(5, 'Produto Vigilancia', 200.00, 'Monitoramento', 20, 1),
(6, 'Camera 33', 69.99, 'Camera de baixa qualidade', 10, 2),
(7, 'Camera ASD', 500.00, 'Camera de alta qualidade', 12, 2),
(8, 'Camera K', 200.00, 'Camera de media qualidade', 12, 1),
(9, 'Mouse sem fio', 70.00, 'Mouse silencioso com sensor óptico', 15, 3),
(10, 'Teclado Mecânico', 170.00, 'Teclado RGB com switches silenciosos', 10, 3),
(11, 'Roteador AX3600', 310.00, 'Roteador Wi-Fi 6 de alta performance', 8, 4),
(12, 'Gravador DVR', 110.00, 'Gravador digital para câmeras analógicas', 5, 2);

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
(2, 'admin2', '$2a$10$8u8skbTkKGzUy5bzmDdc1OvvF40l3lwZYglSoijU.pY19pNgcUEX2', 'Admin 2', 'Administrator', '11111111111', 'admin2@email.com', 'Admin 2 Residence', '11111111111111', 1000, 1),
(3, 'user', '$2a$10$nF5IvBalqyWsN17EiRInt.oSjy4TFDUICBAU6xU0/CdfTyDkOmb6u', 'John', 'Doe', '1122334455', 'johndoe@email', 'Brasil', '987123456', 3000, 0),
(4, 'teste', '$2a$10$Fe.rh8KU8ILhhQJAAq3iZepdoPdM.MqDvt/6Zf2FBi2MgJOceHk5K', 'Teste', 'Teste', '369369369', 'teste@email', 'Brasil', '369258369', 3000, 0);

-- --------------------------------------------------------

--
-- Table structure for table `venda`
--

CREATE TABLE `venda` (
  `id_venda` bigint(20) NOT NULL,
  `data` datetime NOT NULL,
  `observacao` varchar(255) NOT NULL DEFAULT 'VENDA NO BALCÃO',
  `id_usuario` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `venda`
--

INSERT INTO `venda` (`id_venda`, `data`, `observacao`, `id_usuario`) VALUES
(3, '2025-04-10 16:45:00', 'Venda de acessórios de informática para escritório central', 2),
(5, '2025-04-15 18:00:00', 'Venda de equipamentos de rede para filial norte', 2),
(6, '2025-04-20 20:15:00', 'Venda de kit completo para instalação de segurança', 4),
(7, '2025-04-24 16:45:00', 'VENDA NO BALCÃO', 2),
(8, '2025-04-24 16:45:00', 'VENDA NO BALCÃO', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`id_categoria`);

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
  MODIFY `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `compra`
--
ALTER TABLE `compra`
  MODIFY `id_compra` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `fornecedor`
--
ALTER TABLE `fornecedor`
  MODIFY `id_fornecedor` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `item_compra`
--
ALTER TABLE `item_compra`
  MODIFY `id_item` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `item_venda`
--
ALTER TABLE `item_venda`
  MODIFY `id_item` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `produto`
--
ALTER TABLE `produto`
  MODIFY `id_produto` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `venda`
--
ALTER TABLE `venda`
  MODIFY `id_venda` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

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

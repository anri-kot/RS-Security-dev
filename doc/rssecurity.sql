-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 17, 2025 at 11:48 PM
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

DROP TABLE IF EXISTS `categoria`;
CREATE TABLE `categoria` (
  `id_categoria` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `compra`
--

DROP TABLE IF EXISTS `compra`;
CREATE TABLE `compra` (
  `id_compra` bigint(20) NOT NULL,
  `data` datetime NOT NULL,
  `observacao` varchar(255) DEFAULT NULL,
  `id_fornecedor` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `fornecedor`
--

DROP TABLE IF EXISTS `fornecedor`;
CREATE TABLE `fornecedor` (
  `id_fornecedor` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `cnpj` varchar(14) NOT NULL,
  `telefone` varchar(14) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `item_compra`
--

DROP TABLE IF EXISTS `item_compra`;
CREATE TABLE `item_compra` (
  `id_item` bigint(20) NOT NULL,
  `quantidade` smallint(6) NOT NULL,
  `valor_unitario` decimal(10,2) NOT NULL,
  `id_compra` bigint(20) NOT NULL,
  `id_produto` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `item_venda`
--

DROP TABLE IF EXISTS `item_venda`;
CREATE TABLE `item_venda` (
  `id_item` bigint(20) NOT NULL,
  `quantidade` int(11) NOT NULL,
  `valor_unitario` decimal(10,2) NOT NULL,
  `desconto` decimal(5,2) DEFAULT NULL,
  `id_venda` bigint(20) NOT NULL,
  `id_produto` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
CREATE TABLE `produto` (
  `id_produto` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `codigo_barras` varchar(50) DEFAULT NULL,
  `preco_atual` decimal(10,2) DEFAULT NULL,
  `descricao` varchar(150) DEFAULT NULL,
  `estoque` int(11) NOT NULL DEFAULT 0,
  `id_categoria` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
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
(1, 'admin', '$2a$12$aX5VsXRRknFJTq.oO4YFvOmOuiF1PY182Ft1i.342YJIvifRYYlpW', 'Admin', 'Administrator', '11111111111', 'admin@email.com', 'Admin Residence', '11111111111111', 1000, 1);

-- --------------------------------------------------------

--
-- Table structure for table `venda`
--

DROP TABLE IF EXISTS `venda`;
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
  MODIFY `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `compra`
--
ALTER TABLE `compra`
  MODIFY `id_compra` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `fornecedor`
--
ALTER TABLE `fornecedor`
  MODIFY `id_fornecedor` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `item_compra`
--
ALTER TABLE `item_compra`
  MODIFY `id_item` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `item_venda`
--
ALTER TABLE `item_venda`
  MODIFY `id_item` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `produto`
--
ALTER TABLE `produto`
  MODIFY `id_produto` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `venda`
--
ALTER TABLE `venda`
  MODIFY `id_venda` bigint(20) NOT NULL AUTO_INCREMENT;

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

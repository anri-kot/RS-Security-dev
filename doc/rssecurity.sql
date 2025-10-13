-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 13, 2025 at 11:19 PM
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

-- --------------------------------------------------------

--
-- Table structure for table `categoria`
--

CREATE TABLE `categoria` (
  `id_categoria` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

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
  `ativo` tinyint(1) NOT NULL DEFAULT 0,
  `admin` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

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

-- --------------------------------------------------------

--
-- Stand-in structure for view `vw_custo_compra`
-- (See below for the actual view)
--
CREATE TABLE `vw_custo_compra` (
`id_compra` bigint(20)
,`data` datetime
,`custo_total` decimal(37,2)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `vw_lucro_venda`
-- (See below for the actual view)
--
CREATE TABLE `vw_lucro_venda` (
`id_venda` bigint(20)
,`data` datetime
,`receita_total` decimal(52,8)
,`custo_total` decimal(46,6)
,`lucro_total` decimal(53,8)
,`lucro_percentual` decimal(56,2)
);

-- --------------------------------------------------------

--
-- Structure for view `vw_custo_compra`
--
DROP TABLE IF EXISTS `vw_custo_compra`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_custo_compra`  AS SELECT `c`.`id_compra` AS `id_compra`, `c`.`data` AS `data`, coalesce(sum(`ic`.`quantidade` * `ic`.`valor_unitario`),0) AS `custo_total` FROM (`compra` `c` join `item_compra` `ic` on(`ic`.`id_compra` = `c`.`id_compra`)) GROUP BY `c`.`id_compra`, `c`.`data` ;

-- --------------------------------------------------------

--
-- Structure for view `vw_lucro_venda`
--
DROP TABLE IF EXISTS `vw_lucro_venda`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vw_lucro_venda`  AS SELECT `v`.`id_venda` AS `id_venda`, `v`.`data` AS `data`, coalesce(sum(`iv`.`quantidade` * `iv`.`valor_unitario` * (1 - coalesce(`iv`.`desconto` / 100,0))),0) AS `receita_total`, coalesce(sum(`iv`.`quantidade` * `ic`.`avg_custo`),0) AS `custo_total`, coalesce(sum(`iv`.`quantidade` * `iv`.`valor_unitario` * (1 - coalesce(`iv`.`desconto` / 100,0))),0) - coalesce(sum(`iv`.`quantidade` * `ic`.`avg_custo`),0) AS `lucro_total`, CASE WHEN coalesce(sum(`iv`.`quantidade` * `ic`.`avg_custo`),0) = 0 THEN NULL ELSE round((coalesce(sum(`iv`.`quantidade` * `iv`.`valor_unitario` * (1 - coalesce(`iv`.`desconto` / 100,0))),0) - coalesce(sum(`iv`.`quantidade` * `ic`.`avg_custo`),0)) / coalesce(sum(`iv`.`quantidade` * `ic`.`avg_custo`),1) * 100,2) END AS `lucro_percentual` FROM ((`venda` `v` join `item_venda` `iv` on(`iv`.`id_venda` = `v`.`id_venda`)) left join (select `item_compra`.`id_produto` AS `id_produto`,avg(`item_compra`.`valor_unitario`) AS `avg_custo` from `item_compra` group by `item_compra`.`id_produto`) `ic` on(`ic`.`id_produto` = `iv`.`id_produto`)) GROUP BY `v`.`id_venda`, `v`.`data` ;

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
  ADD UNIQUE KEY `codigo_barras` (`codigo_barras`),
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
  MODIFY `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT;

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

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 24, 2025 at 06:32 PM
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
-- Database: `finance_portal`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `account_id` int(11) NOT NULL,
  `account_number` varchar(20) NOT NULL,
  `account_holder_id` int(11) NOT NULL,
  `account_type` varchar(20) NOT NULL,
  `balance` decimal(15,2) DEFAULT 0.00,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` varchar(20) DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`account_id`, `account_number`, `account_holder_id`, `account_type`, `balance`, `created_at`, `status`) VALUES
(1, 'AC1755038196104', 1, 'SAVINGS', 2631977144.00, '2025-11-01 09:26:11', 'ACTIVE'),
(2, 'AC1755038196105', 1, 'CHECKING', 50000.00, '2025-11-01 09:26:11', 'ACTIVE'),
(3, 'AC1755038196106', 2, 'SAVINGS', 75000.00, '2025-11-01 09:26:11', 'ACTIVE'),
(4, 'AC1755038196107', 3, 'BUSINESS', 250000.00, '2025-11-01 09:26:11', 'ACTIVE'),
(5, 'AC1755038196108', 4, 'SAVINGS', 150000.00, '2025-11-01 09:26:11', 'ACTIVE'),
(6, 'ACC849770', 5, 'SAVINGS', 12000.11, '2025-11-03 14:44:28', 'ACTIVE'),
(17, 'RW0010001', 12, 'SAVINGS', 500000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(18, 'RW0010002', 13, 'CURRENT', 1200000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(19, 'RW0010003', 14, 'SAVINGS', 300000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(20, 'RW0010004', 15, 'CURRENT', 2500000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(21, 'RW0010005', 16, 'SAVINGS', 150000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(22, 'RW0010006', 17, 'SAVINGS', 800000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(23, 'RW0010007', 18, 'CURRENT', 950000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(24, 'RW0010008', 19, 'CURRENT', 4000000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(25, 'RW0010009', 20, 'SAVINGS', 200000.00, '2025-12-24 17:28:21', 'ACTIVE'),
(26, 'RW0010010', 21, 'SAVINGS', 600000.00, '2025-12-24 17:28:21', 'ACTIVE');

-- --------------------------------------------------------

--
-- Table structure for table `account_holder`
--

CREATE TABLE `account_holder` (
  `account_holder_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role` varchar(20) DEFAULT 'USER',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_login` timestamp NULL DEFAULT NULL,
  `status` varchar(20) DEFAULT 'ACTIVE',
  `phone` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `account_holder`
--

INSERT INTO `account_holder` (`account_holder_id`, `username`, `password`, `email`, `full_name`, `role`, `created_at`, `last_login`, `status`, `phone`, `address`) VALUES
(1, 'john_doe', 'password123', 'john.doe@email.com', 'tuyisenge jean', 'USER', '2025-11-01 09:26:11', '2025-11-01 11:18:40', 'ACTIVE', '+250788123456', 'Kigali, Rwanda'),
(2, 'jane_smith', 'password123', 'jane.smith@email.com', 'kanani joseph', 'USER', '2025-11-01 09:26:11', NULL, 'ACTIVE', '+250788654321', 'Kigali, Rwanda'),
(3, 'manager1', 'manager123', 'manager@financeportal.com', 'Branch Manager', 'MANAGER', '2025-11-01 09:26:11', '2025-12-12 00:24:06', 'ACTIVE', '+250788111111', 'Kigali, Rwanda'),
(4, 'admin_user', 'admin123', 'admin@financeportal.com', 'niyikiza', 'ADMIN', '2025-11-01 09:26:11', '2025-12-24 08:53:11', 'ACTIVE', '+250788999999', 'Kigali, Rwanda'),
(5, 'emmanuel', 'emmanuel', 'emmanuelniyikiza6@gmail.com', 'emmanuel Niyikiza', 'USER', '2025-11-01 06:28:22', '2025-12-24 08:18:06', 'ACTIVE', NULL, NULL),
(12, 'jmugisha', 'pass123', 'jmugisha@gmail.com', 'Jean Mugisha', 'USER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200001', 'Kigali, Kicukiro'),
(13, 'auwimana', 'pass123', 'auwimana@gmail.com', 'Alice Uwimana', 'USER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200002', 'Kigali, Remera'),
(14, 'enday', 'pass123', 'enday@gmail.com', 'Eric Ndayishimiye', 'USER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200003', 'Huye'),
(15, 'cmukamana', 'pass123', 'cmukamana@gmail.com', 'Chantal Mukamana', 'MANAGER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200004', 'Musanze'),
(16, 'phabimana', 'pass123', 'phabimana@gmail.com', 'Patrick Habimana', 'USER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200005', 'Rubavu'),
(17, 'bnyira', 'pass123', 'bnyira@gmail.com', 'Beata Nyirahabimana', 'USER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200006', 'Ngoma'),
(18, 'dbizimana', 'pass123', 'dbizimana@gmail.com', 'Damien Bizimana', 'USER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200007', 'Rusizi'),
(19, 'suwamariya', 'pass123', 'suwamariya@gmail.com', 'Solange Uwamariya', 'ADMIN', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200008', 'Nyagatare'),
(20, 'erukundo', 'pass123', 'erukundo@gmail.com', 'Emmanuel Rukundo', 'USER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200009', 'Gicumbi'),
(21, 'omukashema', 'pass123', 'omukashema@gmail.com', 'Olivia Mukashema', 'USER', '2025-12-24 17:24:50', NULL, 'ACTIVE', '+250788200010', 'Kayonza');

-- --------------------------------------------------------

--
-- Table structure for table `branch`
--

CREATE TABLE `branch` (
  `branch_id` int(11) NOT NULL,
  `branch_code` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` text NOT NULL,
  `city` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `manager` varchar(100) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `opening_time` timestamp NULL DEFAULT NULL,
  `closing_time` timestamp NULL DEFAULT NULL,
  `status` varchar(20) DEFAULT 'ACTIVE',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `branch`
--

INSERT INTO `branch` (`branch_id`, `branch_code`, `name`, `address`, `city`, `phone`, `email`, `manager`, `capacity`, `opening_time`, `closing_time`, `status`, `created_at`, `updated_at`) VALUES
(1, 'KGL001', 'Kigali Main Branch', 'KN 4 Ave, Kigali', 'Kigali', '+250788100100', 'main@financeportal.com', 'Alice Johnson', 50, '2024-01-01 05:00:00', '2024-01-01 14:00:00', 'ACTIVE', '2025-11-01 09:26:11', '2025-11-01 09:26:11'),
(2, 'GSY001', 'Gisenyi Branch', 'Gisenyi Town', 'Gisenyi', '+250788200200', 'gisenyi@financeportal.com', 'Bob Brown', 30, '2024-01-01 05:30:00', '2024-01-01 13:30:00', 'ACTIVE', '2025-11-01 09:26:11', '2025-11-01 09:26:11'),
(3, 'BT001', 'Butare Branch', 'Butare Town', 'Butare', '+250788300300', 'butare@financeportal.com', 'Charlie Wilson', 25, '2024-01-01 06:00:00', '2024-01-01 13:00:00', 'ACTIVE', '2025-11-01 09:26:11', '2025-11-01 09:26:11'),
(4, 'BR-KGL-01', 'Kigali City Branch', 'KN 4 Ave, Nyarugenge', 'Kigali', '+250788111001', 'kigali@bank.rw', 'Jean Bosco Mugisha', 500, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(5, 'BR-KGL-02', 'Remera Branch', 'KG 11 Ave, Remera', 'Kigali', '+250788111002', 'remera@bank.rw', 'Alice Uwimana', 300, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(6, 'BR-HUY-01', 'Huye Branch', 'Huye Town Center', 'Huye', '+250788111003', 'huye@bank.rw', 'Eric Ndayishimiye', 200, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(7, 'BR-MUS-01', 'Musanze Branch', 'Musanze Main Road', 'Musanze', '+250788111004', 'musanze@bank.rw', 'Chantal Mukamana', 250, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(8, 'BR-RUB-01', 'Rubavu Branch', 'Gisenyi Lake Road', 'Rubavu', '+250788111005', 'rubavu@bank.rw', 'Patrick Habimana', 220, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(9, 'BR-NGO-01', 'Ngoma Branch', 'Ngoma Market Area', 'Ngoma', '+250788111006', 'ngoma@bank.rw', 'Beata Nyirahabimana', 180, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(10, 'BR-RUS-01', 'Rusizi Branch', 'Kamembe Town', 'Rusizi', '+250788111007', 'rusizi@bank.rw', 'Damien Bizimana', 170, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(11, 'BR-NYA-01', 'Nyagatare Branch', 'Nyagatare Center', 'Nyagatare', '+250788111008', 'nyagatare@bank.rw', 'Solange Uwamariya', 160, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(12, 'BR-GIC-01', 'Gicumbi Branch', 'Gicumbi Main Street', 'Gicumbi', '+250788111009', 'gicumbi@bank.rw', 'Emmanuel Rukundo', 140, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28'),
(13, 'BR-KAY-01', 'Kayonza Branch', 'Kayonza Bus Park', 'Kayonza', '+250788111010', 'kayonza@bank.rw', 'Olivia Mukashema', 150, '2024-01-01 00:00:00', '2024-01-01 09:00:00', 'ACTIVE', '2025-12-24 17:24:28', '2025-12-24 17:24:28');

-- --------------------------------------------------------

--
-- Table structure for table `card`
--

CREATE TABLE `card` (
  `card_id` int(11) NOT NULL,
  `card_number` varchar(16) NOT NULL,
  `account_holder_id` int(11) NOT NULL,
  `expiry` timestamp NULL DEFAULT NULL,
  `status` varchar(20) DEFAULT 'ACTIVE',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `card`
--

INSERT INTO `card` (`card_id`, `card_number`, `account_holder_id`, `expiry`, `status`, `created_at`) VALUES
(1, '4111111111111111', 1, '2027-12-31 20:59:59', 'ACTIVE', '2025-11-01 09:26:11'),
(2, '5500000000000004', 2, '2026-10-31 20:59:59', 'ACTIVE', '2025-11-01 09:26:11'),
(3, '340000000000009', 3, '2025-08-31 20:59:59', 'BLOCKED', '2025-11-01 09:26:11'),
(14, '4111111111110001', 12, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(15, '4111111111110002', 13, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(16, '4111111111110003', 14, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(17, '4111111111110004', 15, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(18, '4111111111110005', 16, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(19, '4111111111110006', 17, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(20, '4111111111110007', 18, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(21, '4111111111110008', 19, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(22, '4111111111110009', 20, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34'),
(23, '4111111111110010', 21, '2027-12-30 16:00:00', 'ACTIVE', '2025-12-24 17:28:34');

-- --------------------------------------------------------

--
-- Table structure for table `dashboard`
--

CREATE TABLE `dashboard` (
  `dashboard_id` int(11) NOT NULL,
  `account_holder_id` int(11) NOT NULL,
  `preferences` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`preferences`)),
  `last_accessed` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dashboard`
--

INSERT INTO `dashboard` (`dashboard_id`, `account_holder_id`, `preferences`, `last_accessed`) VALUES
(1, 1, '{\"theme\": \"dark\", \"default_view\": \"accounts\", \"notifications\": true}', '2025-11-01 09:26:12'),
(12, 4, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(13, 13, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(14, 17, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(15, 15, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(16, 18, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(17, 5, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(18, 14, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(19, 20, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(20, 2, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(21, 12, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(22, 1, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(23, 3, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(24, 21, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(25, 16, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50'),
(26, 19, '{\"theme\":\"light\",\"language\":\"en\"}', '2025-12-24 17:28:50');

-- --------------------------------------------------------

--
-- Table structure for table `loans`
--

CREATE TABLE `loans` (
  `loan_id` int(11) NOT NULL,
  `account_holder_id` int(11) NOT NULL,
  `principal` decimal(15,2) NOT NULL,
  `interest_rate` decimal(5,2) NOT NULL,
  `term_months` int(11) NOT NULL,
  `status` varchar(20) DEFAULT 'APPLIED',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `loans`
--

INSERT INTO `loans` (`loan_id`, `account_holder_id`, `principal`, `interest_rate`, `term_months`, `status`, `created_at`) VALUES
(1, 1, 5000.00, 5.50, 24, 'APPROVED', '2025-11-01 09:26:11'),
(2, 2, 100000.00, 3.50, 240, 'PENDING', '2025-11-01 09:26:11'),
(3, 3, 25000.00, 4.00, 36, 'REJECTED', '2025-11-01 09:26:11'),
(4, 1, 5000.00, 0.06, 24, 'REJECTED', '2025-11-01 10:02:44'),
(5, 2, 15000.00, 0.07, 36, 'APPROVED', '2025-11-01 10:02:44'),
(6, 3, 25000.00, 0.08, 48, 'ACTIVE', '2025-11-01 10:02:44'),
(7, 4, 10000.00, 0.09, 12, 'REJECTED', '2025-11-01 10:02:44'),
(28, 12, 1000000.00, 12.50, 24, 'APPROVED', '2025-12-24 17:29:05'),
(29, 13, 2000000.00, 11.00, 36, 'APPROVED', '2025-12-24 17:29:05'),
(30, 14, 500000.00, 13.00, 12, 'PAID', '2025-12-24 17:29:05'),
(31, 15, 3000000.00, 10.50, 48, 'APPROVED', '2025-12-24 17:29:05'),
(32, 16, 800000.00, 14.00, 18, 'APPLIED', '2025-12-24 17:29:05'),
(33, 17, 1200000.00, 12.00, 24, 'APPROVED', '2025-12-24 17:29:05'),
(34, 18, 1500000.00, 11.75, 36, 'APPROVED', '2025-12-24 17:29:05'),
(35, 19, 5000000.00, 9.50, 60, 'APPROVED', '2025-12-24 17:29:05'),
(36, 20, 600000.00, 13.50, 12, 'PAID', '2025-12-24 17:29:05'),
(37, 21, 900000.00, 14.25, 18, 'APPLIED', '2025-12-24 17:29:05');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` int(11) NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `account_id` int(11) NOT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `transaction_type` varchar(20) NOT NULL,
  `status` varchar(20) DEFAULT 'COMPLETED',
  `amount` decimal(15,2) NOT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `balance_after` decimal(15,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `order_number`, `account_id`, `transaction_date`, `transaction_type`, `status`, `amount`, `payment_method`, `notes`, `balance_after`) VALUES
(1, 'ORD001', 1, '2025-11-01 09:26:11', 'DEPOSIT', 'COMPLETED', 100000.00, 'BANK_TRANSFER', 'Initial deposit', 100000.00),
(2, 'ORD002', 1, '2025-11-01 09:26:11', 'WITHDRAWAL', 'COMPLETED', 5000.00, 'ATM', 'ATM withdrawal', 95000.00),
(3, 'ORD003', 2, '2025-11-01 09:26:11', 'DEPOSIT', 'COMPLETED', 50000.00, 'CASH', 'Salary deposit', 50000.00),
(4, 'ORD004', 1, '2025-11-01 09:26:11', 'DEPOSIT', 'COMPLETED', 122222342.00, 'TRANSFER', 'Large deposit', 122222342.00),
(15, 'ORD-RW-001', 17, '2025-12-24 17:29:20', 'DEPOSIT', 'COMPLETED', 200000.00, 'CASH', 'Initial deposit', 500000.00);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`account_id`),
  ADD UNIQUE KEY `account_number` (`account_number`),
  ADD KEY `account_holder_id` (`account_holder_id`);

--
-- Indexes for table `account_holder`
--
ALTER TABLE `account_holder`
  ADD PRIMARY KEY (`account_holder_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `branch`
--
ALTER TABLE `branch`
  ADD PRIMARY KEY (`branch_id`),
  ADD UNIQUE KEY `branch_code` (`branch_code`);

--
-- Indexes for table `card`
--
ALTER TABLE `card`
  ADD PRIMARY KEY (`card_id`),
  ADD UNIQUE KEY `card_number` (`card_number`),
  ADD KEY `account_holder_id` (`account_holder_id`);

--
-- Indexes for table `dashboard`
--
ALTER TABLE `dashboard`
  ADD PRIMARY KEY (`dashboard_id`),
  ADD KEY `account_holder_id` (`account_holder_id`);

--
-- Indexes for table `loans`
--
ALTER TABLE `loans`
  ADD PRIMARY KEY (`loan_id`),
  ADD KEY `account_holder_id` (`account_holder_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `account_id` (`account_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `account_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `account_holder`
--
ALTER TABLE `account_holder`
  MODIFY `account_holder_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `branch`
--
ALTER TABLE `branch`
  MODIFY `branch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `card`
--
ALTER TABLE `card`
  MODIFY `card_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `dashboard`
--
ALTER TABLE `dashboard`
  MODIFY `dashboard_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `loans`
--
ALTER TABLE `loans`
  MODIFY `loan_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`account_holder_id`) REFERENCES `account_holder` (`account_holder_id`);

--
-- Constraints for table `card`
--
ALTER TABLE `card`
  ADD CONSTRAINT `card_ibfk_1` FOREIGN KEY (`account_holder_id`) REFERENCES `account_holder` (`account_holder_id`);

--
-- Constraints for table `dashboard`
--
ALTER TABLE `dashboard`
  ADD CONSTRAINT `dashboard_ibfk_1` FOREIGN KEY (`account_holder_id`) REFERENCES `account_holder` (`account_holder_id`);

--
-- Constraints for table `loans`
--
ALTER TABLE `loans`
  ADD CONSTRAINT `loans_ibfk_1` FOREIGN KEY (`account_holder_id`) REFERENCES `account_holder` (`account_holder_id`);

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

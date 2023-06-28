-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.3.14-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for trader_buddy
CREATE DATABASE IF NOT EXISTS `trader_buddy` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `trader_buddy`;

-- Dumping structure for table trader_buddy.account
CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` varchar(255) DEFAULT NULL,
  `account_open_time` datetime(6) DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `trading_plan_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `skills_id` bigint(20) DEFAULT NULL,
  `rank_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `account_number` bigint(20) DEFAULT NULL,
  `currency` int(11) DEFAULT NULL,
  `account_type` int(11) DEFAULT NULL,
  `broker` int(11) DEFAULT NULL,
  `daily_stop_limit` double DEFAULT NULL,
  `daily_stop_limit_type` int(11) DEFAULT NULL,
  `default_account` bit(1) DEFAULT NULL,
  `trade_platform` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk8mim6eq849fvhk528y9pv7lj` (`trading_plan_id`),
  KEY `FKra7xoi9wtlcq07tmoxxe5jrh4` (`user_id`),
  KEY `FKetf0myrpvhqfh8rj0yixc1f73` (`skills_id`),
  KEY `FKjd9gn0jtaygwwnrfergi7f2n5` (`rank_id`),
  CONSTRAINT `FKetf0myrpvhqfh8rj0yixc1f73` FOREIGN KEY (`skills_id`) REFERENCES `skills` (`id`),
  CONSTRAINT `FKjd9gn0jtaygwwnrfergi7f2n5` FOREIGN KEY (`rank_id`) REFERENCES `ranks` (`id`),
  CONSTRAINT `FKk8mim6eq849fvhk528y9pv7lj` FOREIGN KEY (`trading_plan_id`) REFERENCES `trading_plans` (`id`),
  CONSTRAINT `FKra7xoi9wtlcq07tmoxxe5jrh4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.account: ~1 rows (approximately)
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
REPLACE INTO `account` (`id`, `account_id`, `account_open_time`, `active`, `balance`, `trading_plan_id`, `user_id`, `skills_id`, `rank_id`, `name`, `account_number`, `currency`, `account_type`, `broker`, `daily_stop_limit`, `daily_stop_limit_type`, `default_account`, `trade_platform`) VALUES
	(7, '7', '2023-06-26 16:52:53.590075', b'1', 30666.47, NULL, 5, 7, 60, 'FTMO Challenge ($30,000)', 2091237706, 5, 2, 1, 55, 1, b'1', 1);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.account_balance_modifications
CREATE TABLE IF NOT EXISTS `account_balance_modifications` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `date_time` datetime(6) DEFAULT NULL,
  `modification_type` int(11) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `processed` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdifbvxvm6eissuh3at4i60uw7` (`account_id`),
  CONSTRAINT `FKdifbvxvm6eissuh3at4i60uw7` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.account_balance_modifications: ~0 rows (approximately)
/*!40000 ALTER TABLE `account_balance_modifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_balance_modifications` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.account_trading_plan
CREATE TABLE IF NOT EXISTS `account_trading_plan` (
  `account_id` bigint(20) NOT NULL,
  `trading_plan_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_h6qsufrqxqveqwrit0moxv06e` (`trading_plan_id`),
  KEY `FK2tvhb1adnawvfghuq3cf8v2uo` (`account_id`),
  CONSTRAINT `FK2tvhb1adnawvfghuq3cf8v2uo` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKnlg1d43reog7exmmiq2ul8oog` FOREIGN KEY (`trading_plan_id`) REFERENCES `trading_plans` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.account_trading_plan: ~0 rows (approximately)
/*!40000 ALTER TABLE `account_trading_plan` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_trading_plan` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.base_ranks
CREATE TABLE IF NOT EXISTS `base_ranks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `multiplier` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.base_ranks: ~12 rows (approximately)
/*!40000 ALTER TABLE `base_ranks` DISABLE KEYS */;
REPLACE INTO `base_ranks` (`id`, `multiplier`, `name`, `priority`) VALUES
	(1, 0, 'Unranked', 0),
	(2, 1, 'Bronze', 1),
	(3, 2, 'Garnet', 2),
	(4, 5, 'Silver', 3),
	(5, 10, 'Amethyst', 4),
	(6, 25, 'Gold', 5),
	(7, 50, 'Sapphire', 6),
	(8, 100, 'Platinum', 7),
	(9, 500, 'Diamond', 9),
	(10, 225, 'Ruby', 8),
	(11, 1000, 'Emerald', 10),
	(12, 10000, 'Obsidian', 11);
/*!40000 ALTER TABLE `base_ranks` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.deposit_plans
CREATE TABLE IF NOT EXISTS `deposit_plans` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `frequency` int(11) DEFAULT NULL,
  `absolute` bit(1) DEFAULT NULL,
  `aggregate_interval` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.deposit_plans: ~2 rows (approximately)
/*!40000 ALTER TABLE `deposit_plans` DISABLE KEYS */;
REPLACE INTO `deposit_plans` (`id`, `amount`, `frequency`, `absolute`, `aggregate_interval`) VALUES
	(1, 350, 5, b'1', NULL),
	(2, 350, 5, b'1', NULL);
/*!40000 ALTER TABLE `deposit_plans` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.excluded_periods
CREATE TABLE IF NOT EXISTS `excluded_periods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aggregate_interval` int(11) DEFAULT NULL,
  `end` date DEFAULT NULL,
  `reason_for_exclusion` longtext DEFAULT NULL,
  `start` date DEFAULT NULL,
  `trading_plan_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsw9fgy0ygwlq8q6itdb0ugnbq` (`trading_plan_id`),
  CONSTRAINT `FKsw9fgy0ygwlq8q6itdb0ugnbq` FOREIGN KEY (`trading_plan_id`) REFERENCES `trading_plans` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.excluded_periods: ~0 rows (approximately)
/*!40000 ALTER TABLE `excluded_periods` DISABLE KEYS */;
/*!40000 ALTER TABLE `excluded_periods` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.goals
CREATE TABLE IF NOT EXISTS `goals` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `profit_target` double DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueNameAndStartDateAndEndDate` (`name`,`start_date`,`end_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.goals: ~0 rows (approximately)
/*!40000 ALTER TABLE `goals` DISABLE KEYS */;
/*!40000 ALTER TABLE `goals` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.market_news
CREATE TABLE IF NOT EXISTS `market_news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news: ~29 rows (approximately)
/*!40000 ALTER TABLE `market_news` DISABLE KEYS */;
REPLACE INTO `market_news` (`id`, `date`) VALUES
	(82, '2023-05-29'),
	(83, '2023-05-30'),
	(84, '2023-05-31'),
	(85, '2023-06-01'),
	(86, '2023-06-02'),
	(87, '2023-06-04'),
	(88, '2023-06-05'),
	(89, '2023-06-06'),
	(90, '2023-06-07'),
	(91, '2023-06-08'),
	(92, '2023-06-09'),
	(93, '2023-06-11'),
	(94, '2023-06-12'),
	(95, '2023-06-13'),
	(96, '2023-06-14'),
	(97, '2023-06-15'),
	(98, '2023-06-16'),
	(99, '2023-06-18'),
	(100, '2023-06-19'),
	(101, '2023-06-20'),
	(102, '2023-06-21'),
	(103, '2023-06-22'),
	(104, '2023-06-23'),
	(105, '2023-06-25'),
	(106, '2023-06-26'),
	(107, '2023-06-27'),
	(108, '2023-06-28'),
	(109, '2023-06-29'),
	(110, '2023-06-30');
/*!40000 ALTER TABLE `market_news` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.market_news_entries
CREATE TABLE IF NOT EXISTS `market_news_entries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `severity` int(11) DEFAULT NULL,
  `slot_id` bigint(20) DEFAULT NULL,
  `country` int(11) DEFAULT NULL,
  `forecast` varchar(255) DEFAULT NULL,
  `previous` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjdd4wlxxdlnyipg8je835o13w` (`slot_id`),
  CONSTRAINT `FKjdd4wlxxdlnyipg8je835o13w` FOREIGN KEY (`slot_id`) REFERENCES `market_news_slots` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=838 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news_entries: ~455 rows (approximately)
/*!40000 ALTER TABLE `market_news_entries` DISABLE KEYS */;
REPLACE INTO `market_news_entries` (`id`, `content`, `severity`, `slot_id`, `country`, `forecast`, `previous`) VALUES
	(383, 'French Bank Holiday', 4, 414, 2, '', ''),
	(384, 'Building Consents m/m', 3, 415, 23, '', '7.0%'),
	(385, 'Bank Holiday', 4, 416, 23, '', ''),
	(386, 'Building Approvals m/m', 3, 417, 1, '2.3%', '-0.1%'),
	(387, 'German Bank Holiday', 4, 418, 2, '', ''),
	(388, 'BRC Shop Price Index y/y', 3, 419, 13, '', '8.8%'),
	(389, 'Unemployment Rate', 3, 420, 17, '2.7%', '2.8%'),
	(390, 'Bank Holiday', 4, 421, 13, '', ''),
	(391, 'Bank Holiday', 4, 422, 23, '', ''),
	(392, 'Italian 10-y Bond Auction', 3, 423, 2, '', '4.42|1.3'),
	(393, 'Prelim Industrial Production m/m', 3, 424, 17, '1.4%', '0.8%'),
	(394, 'Retail Sales y/y', 3, 424, 17, '7.1%', '7.2%'),
	(395, 'ANZ Business Confidence', 3, 425, 23, '', '-43.8'),
	(396, 'Construction Work Done q/q', 3, 426, 1, '0.6%', '-0.4%'),
	(397, 'Private Sector Credit m/m', 3, 426, 1, '0.3%', '0.3%'),
	(398, 'Manufacturing PMI', 2, 426, 6, '49.5', '49.2'),
	(399, 'Non-Manufacturing PMI', 2, 426, 6, '55.1', '56.4'),
	(400, 'CPI y/y', 1, 426, 1, '6.4%', '6.3%'),
	(401, 'Current Account', 3, 427, 5, '-9.9B', '-10.6B'),
	(402, 'RBA Gov Lowe Speaks', 1, 428, 1, '', ''),
	(403, 'M3 Money Supply y/y', 3, 429, 2, '2.1%', '2.5%'),
	(404, 'Private Loans y/y', 3, 429, 2, '2.7%', '2.9%'),
	(405, 'HPI m/m', 3, 430, 23, '0.2%', '0.5%'),
	(406, 'S&P/CS Composite-20 HPI y/y', 3, 430, 23, '-1.7%', '0.4%'),
	(407, 'Spanish Flash CPI y/y', 2, 431, 2, '3.6%', '4.1%'),
	(408, 'KOF Economic Barometer', 3, 431, 23, '95.7', '96.4'),
	(409, 'GDP q/q', 2, 431, 23, '0.1%', '0.0%'),
	(410, 'CB Consumer Confidence', 1, 432, 23, '99.1', '101.3'),
	(411, '10-y Bond Auction', 3, 433, 17, '', '0.43|3.6'),
	(412, 'Retail Sales y/y', 3, 434, 23, '-1.4%', '-1.9%'),
	(413, 'MPC Member Mann Speaks', 3, 435, 13, '', ''),
	(414, 'Chicago PMI', 3, 436, 23, '47.1', '48.6'),
	(415, 'Private Capital Expenditure q/q', 3, 437, 1, '1.1%', '2.2%'),
	(416, 'Retail Sales m/m', 3, 437, 1, '0.0%', '0.0%'),
	(417, 'Italian Prelim CPI m/m', 3, 438, 2, '-0.1%', '0.5%'),
	(418, 'German Prelim CPI m/m', 1, 439, 2, '0.2%', '0.4%'),
	(419, 'JOLTS Job Openings', 1, 440, 23, '9.41M', '9.59M'),
	(420, 'FOMC Member Harker Speaks', 2, 441, 23, '', ''),
	(421, 'Beige Book', 3, 442, 23, '', ''),
	(422, 'German Unemployment Change', 3, 443, 2, '14K', '24K'),
	(423, 'FOMC Member Jefferson Speaks', 3, 444, 23, '', ''),
	(424, 'Capital Spending q/y', 3, 445, 17, '6.1%', '7.7%'),
	(425, 'GDP m/m', 1, 446, 5, '-0.1%', '0.1%'),
	(426, 'Final Manufacturing PMI', 3, 447, 17, '50.8', '50.8'),
	(427, 'FOMC Member Bowman Speaks', 3, 448, 23, '', ''),
	(428, 'Caixin Manufacturing PMI', 3, 449, 6, '49.5', '49.5'),
	(429, 'French Prelim CPI m/m', 3, 450, 2, '0.3%', '0.6%'),
	(430, 'French Consumer Spending m/m', 3, 450, 2, '0.3%', '-1.3%'),
	(431, 'French Prelim GDP q/q', 3, 450, 2, '0.2%', '0.2%'),
	(432, 'Credit Suisse Economic Expectations', 3, 451, 23, '', '-33.3'),
	(433, 'ECB Financial Stability Review', 2, 451, 2, '', ''),
	(434, 'SNB Chairman Jordan Speaks', 2, 452, 23, '', ''),
	(435, 'German Import Prices m/m', 3, 453, 2, '-0.6%', '-1.1%'),
	(436, 'Housing Starts y/y', 3, 454, 17, '-0.9%', '-3.2%'),
	(437, 'Consumer Confidence', 3, 454, 17, '36.1', '35.4'),
	(438, 'Manufacturing PMI', 3, 455, 5, '', '50.2'),
	(439, 'Final Manufacturing PMI', 3, 456, 13, '46.9', '46.9'),
	(440, 'M4 Money Supply m/m', 3, 456, 13, '-0.2%', '-0.6%'),
	(441, 'Net Lending to Individuals m/m', 3, 456, 13, '2.2B', '2.0B'),
	(442, 'Mortgage Approvals', 3, 456, 13, '54K', '52K'),
	(443, 'Core CPI Flash Estimate y/y', 2, 457, 2, '5.5%', '5.6%'),
	(444, 'CPI Flash Estimate y/y', 1, 457, 2, '6.3%', '7.0%'),
	(445, 'Unemployment Rate', 3, 457, 2, '6.5%', '6.5%'),
	(446, 'Trade Balance', 3, 458, 23, '3.73B', '4.53B'),
	(447, 'Construction Spending m/m', 3, 459, 23, '0.2%', '0.3%'),
	(448, 'ISM Manufacturing Prices', 2, 459, 23, '52.4', '53.2'),
	(449, 'ISM Manufacturing PMI', 1, 459, 23, '47.0', '47.1'),
	(450, 'Monetary Base y/y', 3, 460, 17, '-1.4%', '-1.7%'),
	(451, 'French Final Manufacturing PMI', 3, 461, 2, '46.1', '46.1'),
	(452, 'Overseas Trade Index q/q', 3, 462, 23, '-1.5%', '1.8%'),
	(453, 'Commodity Prices y/y', 3, 463, 1, '', '-19.2%'),
	(454, 'Final Manufacturing PMI', 3, 464, 23, '48.5', '48.5'),
	(455, 'Challenger Job Cuts y/y', 3, 465, 23, '', '175.9%'),
	(456, 'ECB Monetary Policy Meeting Accounts', 3, 465, 2, '', ''),
	(457, 'Crude Oil Inventories', 3, 466, 23, '-1.4M', '-12.5M'),
	(458, 'Wards Total Vehicle Sales', 3, 467, 23, '15.3M', '15.9M'),
	(459, 'German Final Manufacturing PMI', 3, 468, 2, '42.9', '42.9'),
	(460, 'ADP Non-Farm Employment Change', 1, 469, 23, '173K', '296K'),
	(461, 'FOMC Member Harker Speaks', 2, 470, 23, '', ''),
	(462, 'Natural Gas Storage', 3, 471, 23, '106B', '96B'),
	(463, 'Manufacturing PMI', 3, 472, 23, '44.5', '45.3'),
	(464, 'Gov Board Member Schlegel Speaks', 3, 472, 23, '', ''),
	(465, 'Italian Manufacturing PMI', 3, 473, 2, '45.8', '46.8'),
	(466, 'ECB President Lagarde Speaks', 2, 474, 2, '', ''),
	(467, 'Revised Unit Labor Costs q/q', 3, 475, 23, '6.2%', '6.3%'),
	(468, 'Revised Nonfarm Productivity q/q', 3, 475, 23, '-2.6%', '-2.7%'),
	(469, 'Unemployment Claims', 1, 475, 23, '236K', '229K'),
	(470, 'French 10-y Bond Auction', 3, 476, 2, '', '2.83|1.9'),
	(471, 'French Gov Budget Balance', 3, 477, 2, '', '-54.7B'),
	(472, 'Final Manufacturing PMI', 3, 478, 2, '44.6', '44.6'),
	(473, 'Italian Monthly Unemployment Rate', 3, 478, 2, '7.8%', '7.8%'),
	(474, 'Spanish Manufacturing PMI', 3, 479, 2, '47.9', '49.0'),
	(475, 'German Retail Sales m/m', 3, 480, 2, '1.0%', '-2.4%'),
	(476, 'Nationwide HPI m/m', 3, 480, 13, '-0.5%', '0.5%'),
	(477, 'Italian Bank Holiday', 4, 481, 2, '', ''),
	(478, 'Average Hourly Earnings m/m', 1, 482, 23, '0.3%', '0.5%'),
	(479, 'Unemployment Rate', 1, 482, 23, '3.5%', '3.4%'),
	(480, 'Non-Farm Employment Change', 1, 482, 23, '193K', '253K'),
	(481, 'French Industrial Production m/m', 3, 483, 2, '0.3%', '-1.1%'),
	(482, 'Spanish Unemployment Change', 3, 484, 2, '-40.1K', '-73.9K'),
	(483, 'French 10-y Bond Auction', 3, 485, 2, '', '2.83|1.9'),
	(484, 'Trade Balance', 3, 480, 23, '3.73B', '4.53B'),
	(485, 'MI Inflation Gauge m/m', 3, 486, 1, '', '0.2%'),
	(486, 'ANZ Job Advertisements m/m', 3, 487, 1, '', '-0.3%'),
	(487, 'Company Operating Profits q/q', 3, 487, 1, '2.1%', '10.6%'),
	(488, 'OPEC-JMMC Meetings', 1, 488, 23, '', ''),
	(489, 'Caixin Services PMI', 3, 489, 6, '55.2', '56.4'),
	(490, 'Bank Holiday', 4, 490, 23, '', ''),
	(491, '30-y Bond Auction', 3, 491, 17, '', '1.25|3.5'),
	(492, 'CPI m/m', 1, 492, 23, '0.3%', '0.0%'),
	(493, 'Final Services PMI', 3, 493, 23, '55.1', '55.1'),
	(494, 'ANZ Commodity Prices m/m', 3, 494, 23, '', '-1.7%'),
	(495, 'Current Account', 3, 495, 1, '15.0B', '14.1B'),
	(496, 'Final Services PMI', 3, 496, 13, '55.1', '55.1'),
	(497, 'Sentix Investor Confidence', 3, 496, 2, '-15.2', '-13.1'),
	(498, 'PPI m/m', 3, 497, 2, '-3.0%', '-1.6%'),
	(499, 'Average Cash Earnings y/y', 3, 498, 17, '1.7%', '0.8%'),
	(500, 'Household Spending y/y', 3, 498, 17, '-2.2%', '-1.9%'),
	(501, 'ECB President Lagarde Speaks', 2, 499, 2, '', ''),
	(502, 'Factory Orders m/m', 3, 500, 23, '0.8%', '0.9%'),
	(503, 'ISM Services PMI', 1, 500, 23, '52.6', '51.9'),
	(504, 'German Buba President Nagel Speaks', 3, 500, 2, '', ''),
	(505, 'German Final Services PMI', 3, 501, 2, '57.8', '57.8'),
	(506, 'Italian Services PMI', 3, 502, 2, '57.0', '57.6'),
	(507, 'French Final Services PMI', 3, 503, 2, '52.8', '52.8'),
	(508, 'BRC Retail Sales Monitor y/y', 3, 504, 13, '5.2%', '5.2%'),
	(509, 'Final Services PMI', 3, 505, 2, '55.9', '55.9'),
	(510, 'Spanish Services PMI', 3, 506, 2, '56.9', '57.9'),
	(511, 'German Trade Balance', 3, 507, 2, '16.1B', '16.7B'),
	(512, 'GDT Price Index', 3, 508, 23, '', '-0.9%'),
	(513, 'GDP q/q', 1, 509, 1, '0.3%', '0.5%'),
	(514, 'Construction PMI', 3, 510, 13, '50.9', '51.1'),
	(515, 'Retail Sales m/m', 3, 511, 2, '0.2%', '-1.2%'),
	(516, 'Ivey PMI', 2, 512, 5, '57.2', '56.8'),
	(517, 'IBD/TIPP Economic Optimism', 3, 513, 23, '45.2', '41.6'),
	(518, 'RBA Gov Lowe Speaks', 1, 514, 1, '', ''),
	(519, 'RBA Deputy Gov Bullock Speaks', 3, 515, 1, '', ''),
	(520, 'Trade Balance', 3, 516, 6, '676B', '618B'),
	(521, 'Building Permits m/m', 3, 517, 5, '-4.3%', '11.3%'),
	(522, 'USD-Denominated Trade Balance', 3, 518, 6, '95.2B', '90.2B'),
	(523, 'German Factory Orders m/m', 3, 519, 2, '2.7%', '-10.7%'),
	(524, '30-y Bond Auction', 3, 520, 13, '', '4.08|2.5'),
	(525, 'Cash Rate', 1, 521, 1, '3.85%', '3.85%'),
	(526, 'RBA Rate Statement', 1, 521, 1, '', ''),
	(527, 'Manufacturing Sales q/q', 3, 522, 23, '', '-0.4%'),
	(528, 'Trade Balance', 3, 523, 1, '13.65B', '15.27B'),
	(529, 'Overnight Rate', 1, 524, 5, '4.50%', '4.50%'),
	(530, 'BOC Rate Statement', 1, 524, 5, '', ''),
	(531, 'Consumer Credit m/m', 3, 525, 23, '22.2B', '26.5B'),
	(532, 'Crude Oil Inventories', 3, 526, 23, '', '4.5M'),
	(533, 'Bank Lending y/y', 3, 527, 17, '3.1%', '3.2%'),
	(534, 'Current Account', 3, 527, 17, '1.42T', '1.01T'),
	(535, 'Final GDP q/q', 3, 527, 17, '0.5%', '0.4%'),
	(536, 'Final GDP Price Index y/y', 3, 527, 17, '2.0%', '2.0%'),
	(537, 'Trade Balance', 3, 528, 23, '-75.8B', '-64.2B'),
	(538, 'Trade Balance', 3, 528, 5, '0.4B', '1.0B'),
	(539, 'Labor Productivity q/q', 3, 528, 5, '0.1%', '-0.5%'),
	(540, 'RICS House Price Balance', 3, 529, 13, '-39%', '-39%'),
	(541, 'Unemployment Rate', 3, 530, 23, '1.9%', '1.9%'),
	(542, 'French Trade Balance', 3, 531, 2, '-7.7B', '-8.0B'),
	(543, 'Italian Retail Sales m/m', 3, 532, 2, '0.3%', '0.0%'),
	(544, 'Foreign Currency Reserves', 2, 533, 23, '', '732B'),
	(545, 'German Industrial Production m/m', 3, 534, 2, '0.7%', '-3.4%'),
	(546, 'Halifax HPI m/m', 3, 534, 13, '0.2%', '-0.3%'),
	(547, 'Leading Indicators', 3, 535, 17, '98.3%', '97.5%'),
	(548, 'Natural Gas Storage', 3, 536, 23, '', '110B'),
	(549, 'Gov Council Member Beaudry Speaks', 3, 537, 5, '', ''),
	(550, 'M2 Money Stock y/y', 3, 538, 17, '2.7%', '2.5%'),
	(551, 'CPI y/y', 2, 539, 6, '0.2%', '0.1%'),
	(552, 'PPI y/y', 2, 539, 6, '-4.4%', '-3.6%'),
	(553, 'Revised GDP q/q', 3, 540, 2, '0.0%', '0.1%'),
	(554, 'Final Employment Change q/q', 3, 540, 2, '0.6%', '0.6%'),
	(555, 'Unemployment Claims', 1, 541, 23, '236K', '232K'),
	(556, 'Final Wholesale Inventories m/m', 3, 542, 23, '-0.2%', '-0.2%'),
	(557, 'French Final Private Payrolls q/q', 3, 543, 2, '0.2%', '0.2%'),
	(558, 'Economy Watchers Sentiment', 3, 544, 17, '55.1', '54.6'),
	(559, 'SNB Chairman Jordan Speaks', 2, 545, 23, '', ''),
	(560, 'Unemployment Rate', 1, 546, 5, '5.1%', '5.0%'),
	(561, 'Employment Change', 1, 546, 5, '24.5K', '41.4K'),
	(562, 'Capacity Utilization Rate', 3, 546, 5, '82.0%', '81.7%'),
	(563, 'M2 Money Supply y/y', 3, 547, 6, '12.0%', '12.4%'),
	(564, 'New Loans', 2, 547, 6, '1510B', '719B'),
	(565, 'Italian Industrial Production m/m', 3, 548, 2, '0.2%', '-0.6%'),
	(566, 'PPI y/y', 3, 549, 17, '5.6%', '5.8%'),
	(567, 'Bank Holiday', 4, 550, 1, '', ''),
	(568, 'Visitor Arrivals m/m', 3, 551, 23, '', '-2.9%'),
	(569, 'BSI Manufacturing Index', 3, 552, 17, '-4.2', '-10.5'),
	(570, 'NAB Business Confidence', 2, 553, 1, '', '0'),
	(571, 'Westpac Consumer Sentiment', 3, 554, 1, '', '-7.9%'),
	(572, 'M2 Money Supply y/y', 3, 555, 6, '12.0%', '12.4%'),
	(573, 'New Loans', 2, 555, 6, '1530B', '719B'),
	(574, '10-y Bond Auction', 2, 556, 23, '', '3.45|2.4'),
	(575, 'MPC Member Mann Speaks', 3, 557, 13, '', ''),
	(576, 'Prelim Machine Tool Orders y/y', 3, 558, 17, '', '-14.4%'),
	(577, 'Federal Budget Balance', 3, 559, 23, '-205.0B', '176.2B'),
	(578, 'Current Account', 3, 560, 23, '-6.95B', '-9.46B'),
	(579, 'FPI m/m', 3, 560, 23, '', '0.5%'),
	(580, 'CB Leading Index m/m', 3, 561, 13, '', '-0.9%'),
	(581, 'ZEW Economic Sentiment', 3, 562, 2, '-11.9', '-9.4'),
	(582, 'German ZEW Economic Sentiment', 2, 562, 2, '-13.2', '-10.7'),
	(583, 'MPC Member Dhingra Speaks', 3, 563, 13, '', ''),
	(584, 'BOE Gov Bailey Speaks', 1, 564, 13, '', ''),
	(585, 'NIESR GDP Estimate', 3, 565, 13, '', '0.1%'),
	(586, '10-y Bond Auction', 3, 566, 13, '', '4.24|3.0'),
	(587, 'CPI m/m', 1, 567, 23, '0.2%', '0.4%'),
	(588, 'Core CPI m/m', 1, 567, 23, '0.4%', '0.4%'),
	(589, 'CPI y/y', 1, 567, 23, '4.1%', '4.9%'),
	(590, 'Foreign Direct Investment ytd/y', 3, 568, 6, '', '2.2%'),
	(591, 'NFIB Small Business Index', 3, 569, 23, '88.5', '89.0'),
	(592, '30-y Bond Auction', 3, 570, 23, '', '3.74|2.4'),
	(593, 'Italian Quarterly Unemployment Rate', 3, 571, 2, '', '7.8%'),
	(594, 'German Final CPI m/m', 3, 572, 2, '-0.1%', '-0.1%'),
	(595, 'Average Earnings Index 3m/y', 2, 572, 13, '6.1%', '5.8%'),
	(596, 'Unemployment Rate', 3, 572, 13, '4.0%', '3.9%'),
	(597, 'Claimant Count Change', 1, 572, 13, '21.4K', '46.7K'),
	(598, 'GDP q/q', 1, 573, 23, '-0.1%', '-0.6%'),
	(599, 'Crude Oil Inventories', 3, 574, 23, '', '-0.5M'),
	(600, 'Core Machinery Orders m/m', 3, 575, 17, '3.1%', '-3.9%'),
	(601, 'Trade Balance', 3, 575, 17, '-0.78T', '-1.02T'),
	(602, 'MI Inflation Expectations', 3, 576, 1, '', '5.0%'),
	(603, 'RBA Bulletin', 3, 577, 1, '', ''),
	(604, 'Unemployment Rate', 1, 577, 1, '3.7%', '3.7%'),
	(605, 'Employment Change', 1, 577, 1, '17.1K', '-4.3K'),
	(606, 'Industrial Production m/m', 3, 578, 2, '0.8%', '-4.1%'),
	(607, 'Core PPI m/m', 1, 579, 23, '0.2%', '0.2%'),
	(608, 'PPI m/m', 1, 579, 23, '-0.1%', '0.2%'),
	(609, 'Fixed Asset Investment ytd/y', 3, 580, 6, '4.4%', '4.7%'),
	(610, 'Industrial Production y/y', 1, 580, 6, '3.5%', '5.6%'),
	(611, 'NBS Press Conference', 3, 580, 6, '', ''),
	(612, 'Unemployment Rate', 3, 580, 6, '5.2%', '5.2%'),
	(613, 'Retail Sales y/y', 2, 580, 6, '13.7%', '18.4%'),
	(614, 'German 10-y Bond Auction', 3, 581, 2, '', '2.31|2.3'),
	(615, 'FOMC Press Conference', 1, 582, 23, '', ''),
	(616, 'German WPI m/m', 3, 583, 2, '', '-0.4%'),
	(617, 'GDP m/m', 1, 583, 13, '0.2%', '-0.3%'),
	(618, 'Index of Services 3m/3m', 3, 583, 13, '-0.1%', '0.1%'),
	(619, 'Industrial Production m/m', 3, 583, 13, '-0.2%', '0.7%'),
	(620, 'Manufacturing Production m/m', 3, 583, 13, '-0.3%', '0.7%'),
	(621, 'Goods Trade Balance', 3, 583, 13, '-16.5B', '-16.4B'),
	(622, 'Construction Output m/m', 3, 583, 13, '0.0%', '0.2%'),
	(623, 'Federal Funds Rate', 1, 584, 23, '5.25%', '5.25%'),
	(624, 'FOMC Statement', 1, 584, 23, '', ''),
	(625, 'FOMC Economic Projections', 1, 584, 23, '', ''),
	(626, 'PPI m/m', 3, 585, 23, '0.1%', '0.2%'),
	(627, 'Industrial Production m/m', 2, 586, 23, '0.1%', '0.5%'),
	(628, 'Capacity Utilization Rate', 3, 586, 23, '79.7%', '79.7%'),
	(629, 'MPC Member Cunliffe Speaks', 3, 587, 13, '', ''),
	(630, 'BOJ Policy Rate', 2, 588, 17, '-0.10%', '-0.10%'),
	(631, 'Monetary Policy Statement', 1, 588, 17, '', ''),
	(632, 'Trade Balance', 3, 589, 2, '5.7B', '17.0B'),
	(633, 'BusinessNZ Manufacturing Index', 3, 590, 23, '', '49.1'),
	(634, 'Spanish 10-y Bond Auction', 3, 591, 2, '', '3.51|1.4'),
	(635, 'ECB Press Conference', 1, 592, 2, '', ''),
	(636, 'TIC Long-Term Purchases', 3, 593, 23, '', '133.3B'),
	(637, 'Business Inventories m/m', 3, 594, 23, '0.2%', '-0.1%'),
	(638, 'Main Refinancing Rate', 1, 595, 2, '4.00%', '3.75%'),
	(639, 'Monetary Policy Statement', 1, 595, 2, '', ''),
	(640, 'Housing Starts', 3, 595, 5, '', '262K'),
	(641, 'Natural Gas Storage', 3, 596, 23, '', '104B'),
	(642, 'Import Prices m/m', 3, 597, 23, '-0.6%', '0.4%'),
	(643, 'Core Retail Sales m/m', 1, 597, 23, '0.1%', '0.4%'),
	(644, 'Retail Sales m/m', 1, 597, 23, '-0.1%', '0.4%'),
	(645, 'Empire State Manufacturing Index', 1, 597, 23, '-14.5', '-31.8'),
	(646, 'Philly Fed Manufacturing Index', 2, 597, 23, '-12.1', '-10.4'),
	(647, 'Unemployment Claims', 1, 597, 23, '248K', '261K'),
	(648, 'Manufacturing Sales m/m', 3, 597, 5, '', '0.7%'),
	(649, 'Eurogroup Meetings', 3, 598, 2, '', ''),
	(650, 'French Final CPI m/m', 3, 599, 2, '-0.1%', '-0.1%'),
	(651, 'SECO Economic Forecasts', 3, 600, 23, '', ''),
	(652, 'German Buba President Nagel Speaks', 3, 600, 2, '', ''),
	(653, 'Tertiary Industry Activity m/m', 3, 601, 17, '0.5%', '-1.7%'),
	(654, 'BOJ Press Conference', 1, 602, 17, '', ''),
	(655, 'Consumer Inflation Expectations', 3, 603, 13, '', '3.9%'),
	(656, 'Italian Trade Balance', 3, 604, 2, '3.29B', '7.54B'),
	(657, 'Final CPI y/y', 3, 604, 2, '6.1%', '6.1%'),
	(658, 'Final Core CPI y/y', 3, 604, 2, '5.3%', '5.3%'),
	(659, 'Wholesale Sales m/m', 3, 605, 5, '', '46.0%'),
	(660, 'Foreign Securities Purchases', 3, 605, 5, '', '-19.07B'),
	(661, 'ECOFIN Meetings', 3, 606, 2, '', ''),
	(662, 'FOMC Member Waller Speaks', 2, 607, 23, '', ''),
	(663, 'German Buba President Nagel Speaks', 3, 608, 2, '', ''),
	(664, 'Prelim UoM Consumer Sentiment', 1, 609, 23, '60.2', '57.7'),
	(665, 'Prelim UoM Inflation Expectations', 2, 609, 23, '', '4.5%'),
	(666, 'BusinessNZ Services Index', 3, 610, 23, '', '49.8'),
	(667, 'Rightmove HPI m/m', 3, 611, 13, '', '1.8%'),
	(668, 'Bank Holiday', 4, 612, 23, '', ''),
	(669, 'Westpac Consumer Sentiment', 3, 613, 23, '', '77.7'),
	(670, 'Monetary Policy Meeting Minutes', 2, 614, 1, '', ''),
	(671, 'RMPI m/m', 3, 615, 5, '1.7%', '2.9%'),
	(672, 'IPPI m/m', 3, 615, 5, '-0.3%', '-0.2%'),
	(673, 'NAHB Housing Market Index', 3, 616, 23, '51', '50'),
	(674, 'RBA Deputy Gov Bullock Speaks', 3, 617, 1, '', ''),
	(675, 'RBA Assist Gov Kent Speaks', 3, 618, 1, '', ''),
	(676, 'FOMC Member Williams Speaks', 3, 619, 23, '', ''),
	(677, 'CB Leading Index m/m', 3, 620, 1, '', '0.0%'),
	(678, 'GDT Price Index', 3, 621, 23, '', '-0.9%'),
	(679, 'Monetary Policy Meeting Minutes', 3, 622, 17, '', ''),
	(680, 'Housing Starts', 3, 623, 23, '1.40M', '1.40M'),
	(681, 'Building Permits', 3, 623, 23, '1.42M', '1.42M'),
	(682, 'MI Leading Index m/m', 3, 624, 1, '', '0.0%'),
	(683, 'Current Account', 3, 625, 2, '27.3B', '31.2B'),
	(684, 'Trade Balance', 3, 626, 23, '3.45B', '2.60B'),
	(685, 'German PPI m/m', 3, 626, 2, '-0.7%', '0.3%'),
	(686, 'Revised Industrial Production m/m', 3, 627, 17, '-0.4%', '-0.4%'),
	(687, 'Trade Balance', 3, 628, 23, '350M', '427M'),
	(688, 'German Buba President Nagel Speaks', 3, 629, 2, '', ''),
	(689, 'HPI y/y', 3, 630, 13, '2.6%', '4.1%'),
	(690, 'Retail Sales m/m', 2, 631, 5, '0.3%', '-1.4%'),
	(691, 'Core Retail Sales m/m', 2, 631, 5, '0.3%', '-0.3%'),
	(692, 'NHPI m/m', 3, 631, 5, '0.0%', '-0.1%'),
	(693, 'CBI Industrial Order Expectations', 3, 632, 13, '-18', '-17'),
	(694, 'FOMC Member Goolsbee Speaks', 3, 633, 23, '', ''),
	(695, 'Credit Card Spending y/y', 3, 634, 23, '', '11.4%'),
	(696, 'Bank Holiday', 4, 635, 6, '', ''),
	(697, 'Fed Chair Powell Testifies', 1, 636, 23, '', ''),
	(698, 'FOMC Member Cook Speaks', 3, 636, 23, '', ''),
	(699, 'FOMC Member Jefferson Speaks', 3, 636, 23, '', ''),
	(700, 'Core CPI y/y', 3, 637, 13, '6.8%', '6.8%'),
	(701, 'CPI y/y', 1, 637, 13, '8.4%', '8.7%'),
	(702, 'PPI Input m/m', 3, 637, 13, '-0.4%', '-0.3%'),
	(703, 'PPI Output m/m', 3, 637, 13, '-0.1%', '0.0%'),
	(704, 'RPI y/y', 3, 637, 13, '11.2%', '11.4%'),
	(705, 'Public Sector Net Borrowing', 3, 637, 13, '19.9B', '24.7B'),
	(706, 'German 30-y Bond Auction', 3, 638, 2, '', '2.53|2.0'),
	(707, 'BOC Summary of Deliberations', 3, 639, 5, '', ''),
	(708, 'SNB Press Conference', 1, 640, 23, '', ''),
	(709, 'Flash Manufacturing PMI', 3, 641, 1, '', '48.0'),
	(710, 'Flash Services PMI', 3, 641, 1, '', '51.8'),
	(711, 'Monetary Policy Summary', 1, 642, 13, '', ''),
	(712, 'Official Bank Rate', 1, 642, 13, '4.75%', '4.50%'),
	(713, 'MPC Official Bank Rate Votes', 1, 642, 13, '7-0-2', '7-0-2'),
	(714, 'National Core CPI y/y', 3, 643, 17, '3.1%', '3.4%'),
	(715, 'FOMC Member Bowman Speaks', 3, 644, 23, '', ''),
	(716, 'Crude Oil Inventories', 3, 645, 23, '', '7.9M'),
	(717, 'Existing Home Sales', 2, 646, 23, '4.25M', '4.28M'),
	(718, 'CB Leading Index m/m', 3, 646, 23, '-0.8%', '-0.6%'),
	(719, 'Consumer Confidence', 3, 646, 2, '-17', '-17'),
	(720, 'Fed Chair Powell Testifies', 1, 646, 23, '', ''),
	(721, 'Natural Gas Storage', 3, 647, 23, '', '84B'),
	(722, 'SNB Monetary Policy Assessment', 1, 648, 23, '', ''),
	(723, 'SNB Policy Rate', 1, 648, 23, '1.75%', '1.50%'),
	(724, 'Current Account', 3, 649, 23, '-219B', '-207B'),
	(725, 'Unemployment Claims', 1, 649, 23, '256K', '262K'),
	(726, 'Flash Manufacturing PMI', 3, 650, 17, '50.2', '50.8'),
	(727, 'BOE Inflation Letter', 3, 651, 13, '', ''),
	(728, 'GfK Consumer Confidence', 3, 652, 13, '-26', '-27'),
	(729, 'FOMC Member Waller Speaks', 2, 653, 23, '', ''),
	(730, 'Bank Holiday', 4, 654, 6, '', ''),
	(731, 'German Buba President Nagel Speaks', 3, 655, 2, '', ''),
	(732, 'SNB Financial Stability Report', 3, 656, 23, '', ''),
	(733, 'German Flash Manufacturing PMI', 1, 657, 2, '43.6', '42.9'),
	(734, 'German Flash Services PMI', 1, 657, 2, '56.3', '57.8'),
	(735, 'Flash Manufacturing PMI', 1, 658, 23, '48.5', '48.5'),
	(736, 'Flash Services PMI', 1, 658, 23, '54.0', '55.1'),
	(737, 'Flash Manufacturing PMI', 1, 659, 13, '46.8', '46.9'),
	(738, 'Flash Services PMI', 1, 659, 13, '54.9', '55.1'),
	(739, 'Flash Manufacturing PMI', 2, 660, 2, '44.9', '44.6'),
	(740, 'Flash Services PMI', 2, 660, 2, '54.5', '55.9'),
	(741, 'Belgian NBB Business Climate', 3, 661, 2, '-10.3', '-9.2'),
	(742, 'French Flash Manufacturing PMI', 1, 662, 2, '45.2', '46.1'),
	(743, 'French Flash Services PMI', 1, 662, 2, '52.1', '52.8'),
	(744, 'Retail Sales m/m', 2, 663, 13, '-0.2%', '0.5%'),
	(745, 'BOJ Summary of Opinions', 3, 664, 17, '', ''),
	(746, 'SPPI y/y', 3, 664, 17, '1.8%', '1.6%'),
	(747, 'FOMC Member Williams Speaks', 3, 665, 23, '', ''),
	(748, 'SNB Chairman Jordan Speaks', 2, 666, 23, '', ''),
	(749, 'German Buba Monthly Report', 3, 667, 2, '', ''),
	(750, 'CBI Realized Sales', 3, 667, 13, '-6', '-10'),
	(751, 'Gov Board Member Maechler Speaks', 3, 668, 23, '', ''),
	(752, 'BRC Shop Price Index y/y', 3, 669, 13, '', '9.0%'),
	(753, 'German ifo Business Climate', 2, 670, 2, '90.7', '91.7'),
	(754, 'MPC Member Dhingra Speaks', 3, 671, 13, '', ''),
	(755, 'ECB President Lagarde Speaks', 2, 672, 2, '', ''),
	(756, 'CPI y/y', 1, 673, 1, '', '6.8%'),
	(757, 'MPC Member Tenreyro Speaks', 3, 674, 13, '', ''),
	(758, 'Gov Council Member Kozicki Speaks', 3, 675, 5, '', ''),
	(759, 'Core Durable Goods Orders m/m', 2, 676, 23, '-0.2%', '-0.2%'),
	(760, 'Durable Goods Orders m/m', 2, 676, 23, '-0.8%', '1.1%'),
	(761, 'Median CPI y/y', 1, 676, 5, '4.0%', '4.2%'),
	(762, 'Trimmed CPI y/y', 1, 676, 5, '4.0%', '4.2%'),
	(763, 'Common CPI y/y', 2, 676, 5, '5.4%', '5.7%'),
	(764, 'Core CPI m/m', 3, 676, 5, '', '0.5%'),
	(765, 'CPI m/m', 1, 676, 5, '0.5%', '0.7%'),
	(766, 'HPI m/m', 3, 677, 23, '0.5%', '0.6%'),
	(767, 'S&P/CS Composite-20 HPI y/y', 3, 677, 23, '-0.2%', '-1.1%'),
	(768, 'CB Leading Index m/m', 3, 677, 6, '', '-0.6%'),
	(769, 'New Home Sales', 2, 678, 23, '676K', '683K'),
	(770, 'Richmond Manufacturing Index', 2, 678, 23, '-12', '-15'),
	(771, 'CB Consumer Confidence', 1, 678, 23, '104.0', '102.3'),
	(772, 'BOJ Core CPI y/y', 3, 679, 17, '3.1%', '3.0%'),
	(773, 'Bank Stress Test Results', 3, 680, 23, '', ''),
	(774, 'BOJ Gov Ueda Speaks', 1, 681, 17, '', ''),
	(775, 'ECB President Lagarde Speaks', 2, 681, 2, '', ''),
	(776, 'BOE Gov Bailey Speaks', 1, 681, 13, '', ''),
	(777, 'Fed Chair Powell Speaks', 1, 681, 23, '', ''),
	(778, 'ANZ Business Confidence', 3, 682, 23, '', '-31.1'),
	(779, 'Retail Sales m/m', 3, 683, 1, '0.1%', '0.0%'),
	(780, 'Italian Prelim CPI m/m', 3, 684, 2, '0.3%', '0.3%'),
	(781, 'SNB Quarterly Bulletin', 3, 685, 23, '', ''),
	(782, 'ECB President Lagarde Speaks', 2, 686, 2, '', ''),
	(783, 'Crude Oil Inventories', 3, 687, 23, '', '-3.8M'),
	(784, 'Retail Sales y/y', 3, 688, 17, '5.2%', '5.0%'),
	(785, 'Prelim Wholesale Inventories m/m', 3, 689, 23, '-0.1%', '-0.2%'),
	(786, 'Goods Trade Balance', 3, 689, 23, '-92.9B', '-96.8B'),
	(787, 'MPC Member Pill Speaks', 3, 690, 13, '', ''),
	(788, 'Credit Suisse Economic Expectations', 3, 691, 23, '', '-32.2'),
	(789, 'Private Loans y/y', 3, 691, 2, '2.3%', '2.5%'),
	(790, 'M3 Money Supply y/y', 3, 691, 2, '1.5%', '1.9%'),
	(791, 'German GfK Consumer Climate', 3, 692, 2, '-22.9', '-24.2'),
	(792, 'Italian Bank Holiday', 4, 693, 2, '', ''),
	(793, 'Private Sector Credit m/m', 3, 694, 1, '0.4%', '0.6%'),
	(794, 'Manufacturing PMI', 2, 694, 6, '49.0', '48.8'),
	(795, 'Non-Manufacturing PMI', 2, 694, 6, '53.7', '54.5'),
	(796, 'M4 Money Supply m/m', 3, 695, 13, '0.2%', '0.0%'),
	(797, 'Mortgage Approvals', 3, 695, 13, '49K', '49K'),
	(798, 'Net Lending to Individuals m/m', 3, 695, 13, '-0.6B', '0.2B'),
	(799, 'German Prelim CPI m/m', 1, 696, 2, '0.2%', '-0.1%'),
	(800, 'Tokyo Core CPI y/y', 2, 697, 17, '3.4%', '3.2%'),
	(801, 'Unemployment Rate', 3, 697, 17, '2.6%', '2.6%'),
	(802, 'Pending Home Sales m/m', 2, 698, 23, '-0.5%', '0.0%'),
	(803, 'MPC Member Tenreyro Speaks', 3, 699, 13, '', ''),
	(804, 'Natural Gas Storage', 3, 700, 23, '', ''),
	(805, 'Prelim Industrial Production m/m', 3, 701, 17, '-0.9%', '-0.4%'),
	(806, 'Final GDP q/q', 1, 702, 23, '1.4%', '1.3%'),
	(807, 'Final GDP Price Index q/q', 2, 702, 23, '4.2%', '4.2%'),
	(808, 'Unemployment Claims', 1, 702, 23, '264K', '264K'),
	(809, 'RBNZ Statement of Intent', 2, 703, 23, '', ''),
	(810, 'ECB Economic Bulletin', 3, 704, 2, '', ''),
	(811, 'Spanish Flash CPI y/y', 2, 705, 2, '1.7%', '3.2%'),
	(812, 'Nationwide HPI m/m', 3, 706, 13, '-0.3%', '-0.1%'),
	(813, 'Consumer Confidence', 3, 707, 17, '36.2', '36.0'),
	(814, 'Retail Sales y/y', 3, 708, 23, '-2.5%', '-3.7%'),
	(815, 'Chicago PMI', 3, 709, 23, '44.0', '40.4'),
	(816, 'Core CPI Flash Estimate y/y', 2, 710, 2, '5.5%', '5.3%'),
	(817, 'CPI Flash Estimate y/y', 2, 710, 2, '5.6%', '6.1%'),
	(818, 'Unemployment Rate', 3, 710, 2, '6.5%', '6.5%'),
	(819, 'Revised UoM Inflation Expectations', 3, 711, 23, '', '3.3%'),
	(820, 'Revised UoM Consumer Sentiment', 2, 711, 23, '63.9', '63.9'),
	(821, 'German Unemployment Change', 3, 712, 2, '15K', '9K'),
	(822, 'BOC Business Outlook Survey', 2, 713, 5, '', ''),
	(823, 'Italian 10-y Bond Auction', 3, 714, 2, '', '4.24|1.5'),
	(824, 'Core PCE Price Index m/m', 1, 715, 23, '0.4%', '0.4%'),
	(825, 'Personal Spending m/m', 3, 715, 23, '0.2%', '0.8%'),
	(826, 'Personal Income m/m', 3, 715, 23, '0.3%', '0.4%'),
	(827, 'GDP m/m', 1, 715, 5, '0.2%', '0.0%'),
	(828, 'French Prelim CPI m/m', 3, 716, 2, '0.2%', '-0.1%'),
	(829, 'French Consumer Spending m/m', 3, 716, 2, '0.7%', '-1.0%'),
	(830, 'Italian Monthly Unemployment Rate', 3, 717, 2, '7.9%', '7.8%'),
	(831, 'KOF Economic Barometer', 3, 718, 23, '89.1', '90.2'),
	(832, 'German Import Prices m/m', 3, 719, 2, '-1.5%', '-1.7%'),
	(833, 'German Retail Sales m/m', 3, 719, 2, '0.0%', '0.8%'),
	(834, 'Final GDP q/q', 3, 719, 13, '0.1%', '0.1%'),
	(835, 'Current Account', 3, 719, 13, '-9.8B', '-2.5B'),
	(836, 'Revised Business Investment q/q', 3, 719, 13, '0.7%', '0.7%'),
	(837, 'Housing Starts y/y', 3, 720, 17, '-2.5%', '-11.9%');
/*!40000 ALTER TABLE `market_news_entries` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.market_news_slots
CREATE TABLE IF NOT EXISTS `market_news_slots` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` time DEFAULT NULL,
  `news_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgx55cd7fhbmhyfm2jig419vpp` (`news_id`),
  CONSTRAINT `FKgx55cd7fhbmhyfm2jig419vpp` FOREIGN KEY (`news_id`) REFERENCES `market_news` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=721 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news_slots: ~307 rows (approximately)
/*!40000 ALTER TABLE `market_news_slots` DISABLE KEYS */;
REPLACE INTO `market_news_slots` (`id`, `time`, `news_id`) VALUES
	(414, '03:01:00', 82),
	(415, '18:45:00', 82),
	(416, '08:00:00', 82),
	(417, '21:30:00', 82),
	(418, '03:02:00', 82),
	(419, '19:01:00', 82),
	(420, '19:30:00', 82),
	(421, '03:00:00', 82),
	(422, '02:00:00', 82),
	(423, '05:14:00', 83),
	(424, '19:50:00', 83),
	(425, '21:00:00', 83),
	(426, '21:30:00', 83),
	(427, '08:30:00', 83),
	(428, '19:00:00', 83),
	(429, '04:00:00', 83),
	(430, '09:00:00', 83),
	(431, '03:00:00', 83),
	(432, '10:00:00', 83),
	(433, '23:35:00', 84),
	(434, '02:30:00', 84),
	(435, '09:15:00', 84),
	(436, '09:45:00', 84),
	(437, '21:30:00', 84),
	(438, '05:00:00', 84),
	(439, '02:29:00', 84),
	(440, '10:00:00', 84),
	(441, '12:30:00', 84),
	(442, '14:00:00', 84),
	(443, '03:55:00', 84),
	(444, '13:30:00', 84),
	(445, '19:50:00', 84),
	(446, '08:30:00', 84),
	(447, '20:33:00', 84),
	(448, '08:50:00', 84),
	(449, '21:45:00', 84),
	(450, '02:45:00', 84),
	(451, '04:00:00', 84),
	(452, '11:05:00', 84),
	(453, '02:00:00', 84),
	(454, '01:00:00', 84),
	(455, '09:30:00', 85),
	(456, '04:30:00', 85),
	(457, '05:00:00', 85),
	(458, '02:05:00', 85),
	(459, '10:00:00', 85),
	(460, '19:50:00', 85),
	(461, '03:50:00', 85),
	(462, '18:45:00', 85),
	(463, '02:30:00', 85),
	(464, '09:45:00', 85),
	(465, '07:30:00', 85),
	(466, '11:00:00', 85),
	(467, '10:15:00', 85),
	(468, '03:55:00', 85),
	(469, '08:15:00', 85),
	(470, '13:00:00', 85),
	(471, '10:30:00', 85),
	(472, '03:30:00', 85),
	(473, '03:45:00', 85),
	(474, '05:30:00', 85),
	(475, '08:30:00', 85),
	(476, '06:18:00', 85),
	(477, '02:45:00', 85),
	(478, '04:00:00', 85),
	(479, '03:15:00', 85),
	(480, '02:00:00', 85),
	(481, '03:03:00', 86),
	(482, '08:30:00', 86),
	(483, '02:45:00', 86),
	(484, '03:00:00', 86),
	(485, '04:59:00', 85),
	(486, '21:00:00', 87),
	(487, '21:30:00', 87),
	(488, '06:15:00', 87),
	(489, '21:45:00', 87),
	(490, '17:00:00', 87),
	(491, '23:35:00', 88),
	(492, '02:30:00', 88),
	(493, '09:45:00', 88),
	(494, '21:00:00', 88),
	(495, '21:30:00', 88),
	(496, '04:30:00', 88),
	(497, '05:00:00', 88),
	(498, '19:30:00', 88),
	(499, '09:00:00', 88),
	(500, '10:00:00', 88),
	(501, '03:55:00', 88),
	(502, '03:45:00', 88),
	(503, '03:50:00', 88),
	(504, '19:01:00', 88),
	(505, '04:00:00', 88),
	(506, '03:15:00', 88),
	(507, '02:00:00', 88),
	(508, '10:33:00', 89),
	(509, '21:30:00', 89),
	(510, '04:30:00', 89),
	(511, '05:00:00', 89),
	(512, '10:00:00', 89),
	(513, '10:03:00', 89),
	(514, '19:20:00', 89),
	(515, '19:50:00', 89),
	(516, '23:03:00', 89),
	(517, '08:30:00', 89),
	(518, '23:04:00', 89),
	(519, '02:00:00', 89),
	(520, '07:32:00', 89),
	(521, '00:30:00', 89),
	(522, '18:45:00', 90),
	(523, '21:30:00', 90),
	(524, '10:00:00', 90),
	(525, '15:00:00', 90),
	(526, '10:30:00', 90),
	(527, '19:50:00', 90),
	(528, '08:30:00', 90),
	(529, '19:01:00', 90),
	(530, '01:45:00', 90),
	(531, '02:45:00', 90),
	(532, '04:00:00', 90),
	(533, '03:00:00', 90),
	(534, '02:00:00', 90),
	(535, '01:00:00', 90),
	(536, '10:30:00', 91),
	(537, '15:10:00', 91),
	(538, '19:50:00', 91),
	(539, '21:30:00', 91),
	(540, '05:00:00', 91),
	(541, '08:30:00', 91),
	(542, '10:00:00', 91),
	(543, '01:30:00', 91),
	(544, '01:00:00', 91),
	(545, '08:05:00', 91),
	(546, '08:30:00', 92),
	(547, '05:03:00', 92),
	(548, '04:00:00', 92),
	(549, '19:50:00', 93),
	(550, '18:00:00', 93),
	(551, '18:45:00', 94),
	(552, '19:50:00', 94),
	(553, '21:30:00', 94),
	(554, '20:30:00', 94),
	(555, '05:20:00', 94),
	(556, '13:01:00', 94),
	(557, '10:00:00', 94),
	(558, '02:00:00', 94),
	(559, '14:00:00', 94),
	(560, '18:45:00', 95),
	(561, '09:30:00', 95),
	(562, '05:00:00', 95),
	(563, '11:00:00', 95),
	(564, '10:00:00', 95),
	(565, '09:03:00', 95),
	(566, '06:33:00', 95),
	(567, '08:30:00', 95),
	(568, '03:02:00', 95),
	(569, '06:00:00', 95),
	(570, '13:01:00', 95),
	(571, '04:00:00', 95),
	(572, '02:00:00', 95),
	(573, '18:45:00', 96),
	(574, '10:30:00', 96),
	(575, '19:50:00', 96),
	(576, '21:00:00', 96),
	(577, '21:30:00', 96),
	(578, '05:00:00', 96),
	(579, '08:30:00', 96),
	(580, '22:00:00', 96),
	(581, '06:42:00', 96),
	(582, '14:30:00', 96),
	(583, '02:00:00', 96),
	(584, '14:00:00', 96),
	(585, '02:30:00', 97),
	(586, '09:15:00', 97),
	(587, '11:35:00', 97),
	(588, '23:30:00', 97),
	(589, '05:00:00', 97),
	(590, '18:30:00', 97),
	(591, '06:03:00', 97),
	(592, '08:45:00', 97),
	(593, '16:00:00', 97),
	(594, '10:00:00', 97),
	(595, '08:15:00', 97),
	(596, '10:30:00', 97),
	(597, '08:30:00', 97),
	(598, '05:15:00', 97),
	(599, '02:45:00', 97),
	(600, '03:00:00', 97),
	(601, '00:30:00', 97),
	(602, '02:30:00', 98),
	(603, '04:30:00', 98),
	(604, '05:00:00', 98),
	(605, '08:30:00', 98),
	(606, '05:15:00', 98),
	(607, '07:45:00', 98),
	(608, '03:00:00', 98),
	(609, '10:00:00', 98),
	(610, '18:30:00', 99),
	(611, '19:01:00', 99),
	(612, '08:00:00', 100),
	(613, '15:23:00', 100),
	(614, '21:30:00', 100),
	(615, '08:30:00', 100),
	(616, '10:00:00', 100),
	(617, '23:15:00', 100),
	(618, '21:35:00', 100),
	(619, '11:45:00', 101),
	(620, '10:30:00', 101),
	(621, '10:33:00', 101),
	(622, '19:50:00', 101),
	(623, '08:30:00', 101),
	(624, '20:30:00', 101),
	(625, '04:00:00', 101),
	(626, '02:00:00', 101),
	(627, '00:30:00', 101),
	(628, '18:45:00', 102),
	(629, '09:45:00', 102),
	(630, '04:30:00', 102),
	(631, '08:30:00', 102),
	(632, '06:00:00', 102),
	(633, '11:40:00', 102),
	(634, '23:00:00', 102),
	(635, '20:01:00', 102),
	(636, '10:00:00', 102),
	(637, '02:00:00', 102),
	(638, '06:34:00', 102),
	(639, '13:30:00', 102),
	(640, '04:30:00', 103),
	(641, '19:00:00', 103),
	(642, '07:00:00', 103),
	(643, '19:30:00', 103),
	(644, '09:55:00', 103),
	(645, '11:00:00', 103),
	(646, '10:00:00', 103),
	(647, '10:30:00', 103),
	(648, '03:30:00', 103),
	(649, '08:30:00', 103),
	(650, '20:30:00', 103),
	(651, '07:03:00', 103),
	(652, '19:01:00', 103),
	(653, '04:00:00', 103),
	(654, '20:01:00', 103),
	(655, '08:05:00', 103),
	(656, '00:30:00', 103),
	(657, '03:30:00', 104),
	(658, '09:45:00', 104),
	(659, '04:30:00', 104),
	(660, '04:00:00', 104),
	(661, '09:00:00', 104),
	(662, '03:15:00', 104),
	(663, '02:00:00', 104),
	(664, '19:50:00', 105),
	(665, '09:45:00', 105),
	(666, '04:50:00', 106),
	(667, '06:00:00', 106),
	(668, '10:25:00', 106),
	(669, '19:01:00', 106),
	(670, '04:00:00', 106),
	(671, '04:15:00', 106),
	(672, '13:30:00', 106),
	(673, '21:30:00', 107),
	(674, '04:30:00', 107),
	(675, '05:30:00', 107),
	(676, '08:30:00', 107),
	(677, '09:00:00', 107),
	(678, '10:00:00', 107),
	(679, '01:00:00', 107),
	(680, '16:30:00', 108),
	(681, '09:30:00', 108),
	(682, '21:00:00', 108),
	(683, '21:30:00', 108),
	(684, '05:00:00', 108),
	(685, '09:00:00', 108),
	(686, '11:00:00', 108),
	(687, '10:30:00', 108),
	(688, '19:50:00', 108),
	(689, '08:30:00', 108),
	(690, '06:30:00', 108),
	(691, '04:00:00', 108),
	(692, '02:00:00', 108),
	(693, '03:03:00', 109),
	(694, '21:30:00', 109),
	(695, '04:30:00', 109),
	(696, '02:29:00', 109),
	(697, '19:30:00', 109),
	(698, '10:00:00', 109),
	(699, '12:30:00', 109),
	(700, '10:30:00', 109),
	(701, '19:50:00', 109),
	(702, '08:30:00', 109),
	(703, '22:00:00', 109),
	(704, '04:00:00', 109),
	(705, '03:00:00', 109),
	(706, '02:00:00', 109),
	(707, '01:00:00', 109),
	(708, '02:30:00', 110),
	(709, '09:45:00', 110),
	(710, '05:00:00', 110),
	(711, '10:00:00', 110),
	(712, '03:55:00', 110),
	(713, '10:30:00', 110),
	(714, '06:33:00', 110),
	(715, '08:30:00', 110),
	(716, '02:45:00', 110),
	(717, '04:00:00', 110),
	(718, '03:00:00', 110),
	(719, '02:00:00', 110),
	(720, '01:00:00', 110);
/*!40000 ALTER TABLE `market_news_slots` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.phone_numbers
CREATE TABLE IF NOT EXISTS `phone_numbers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country_code` smallint(6) DEFAULT NULL,
  `phone_type` int(11) DEFAULT NULL,
  `telephone_number` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.phone_numbers: ~0 rows (approximately)
/*!40000 ALTER TABLE `phone_numbers` DISABLE KEYS */;
REPLACE INTO `phone_numbers` (`id`, `country_code`, `phone_type`, `telephone_number`) VALUES
	(1, 1, 0, 5149411025);
/*!40000 ALTER TABLE `phone_numbers` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.ranks
CREATE TABLE IF NOT EXISTS `ranks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` int(11) DEFAULT NULL,
  `base_rank_id` bigint(20) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc5o5po1vvs7hdli3is527k9s` (`base_rank_id`),
  CONSTRAINT `FKc5o5po1vvs7hdli3is527k9s` FOREIGN KEY (`base_rank_id`) REFERENCES `base_ranks` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.ranks: ~52 rows (approximately)
/*!40000 ALTER TABLE `ranks` DISABLE KEYS */;
REPLACE INTO `ranks` (`id`, `level`, `base_rank_id`, `image_url`) VALUES
	(9, 2, 3, '/ranks/garnet/garnet_2.png'),
	(10, 5, 3, '/ranks/garnet/garnet_5.png'),
	(14, 3, 4, '/ranks/silver/silver_3.png'),
	(15, 5, 4, '/ranks/silver/silver_5.png'),
	(18, 1, 5, '/ranks/amethyst/amethyst_1.png'),
	(19, 2, 5, '/ranks/amethyst/amethyst_2.png'),
	(20, 4, 5, '/ranks/amethyst/amethyst_4.png'),
	(23, 1, 6, '/ranks/gold/gold_1.png'),
	(24, 2, 6, '/ranks/gold/gold_2.png'),
	(25, 5, 6, '/ranks/gold/gold_5.png'),
	(28, 1, 7, '/ranks/sapphire/sapphire_1.png'),
	(29, 3, 7, '/ranks/sapphire/sapphire_3.png'),
	(30, 4, 7, '/ranks/sapphire/sapphire_4.png'),
	(33, 1, 8, '/ranks/platinum/platinum_1.png'),
	(34, 2, 8, '/ranks/platinum/platinum_2.png'),
	(35, 4, 8, '/ranks/platinum/platinum_4.png'),
	(39, 3, 9, '/ranks/diamond/diamond_3.png'),
	(40, 5, 9, '/ranks/diamond/diamond_5.png'),
	(44, 2, 10, '/ranks/ruby/ruby_2.png'),
	(45, 5, 10, '/ranks/ruby/ruby_5.png'),
	(48, 1, 11, '/ranks/emerald/emerald_1.png'),
	(49, 3, 11, '/ranks/emerald/emerald_3.png'),
	(50, 5, 11, '/ranks/emerald/emerald_5.png'),
	(53, 1, 12, '/ranks/obsidian/obsidian_1.png'),
	(56, 1, 2, '/ranks/bronze/bronze_1.png'),
	(57, 2, 2, '/ranks/bronze/bronze_2.png'),
	(58, 3, 2, '/ranks/bronze/bronze_3.png'),
	(59, 4, 2, '/ranks/bronze/bronze_4.png'),
	(60, 5, 2, '/ranks/bronze/bronze_5.png'),
	(63, 1, 3, '/ranks/garnet/garnet_1.png'),
	(64, 3, 3, '/ranks/garnet/garnet_3.png'),
	(65, 4, 3, '/ranks/garnet/garnet_4.png'),
	(68, 1, 4, '/ranks/silver/silver_1.png'),
	(69, 2, 4, '/ranks/silver/silver_2.png'),
	(70, 4, 4, '/ranks/silver/silver_4.png'),
	(74, 3, 5, '/ranks/amethyst/amethyst_3.png'),
	(75, 5, 5, '/ranks/amethyst/amethyst_5.png'),
	(79, 3, 6, '/ranks/gold/gold_3.png'),
	(80, 4, 6, '/ranks/gold/gold_4.png'),
	(84, 2, 7, '/ranks/sapphire/sapphire_2.png'),
	(85, 5, 7, '/ranks/sapphire/sapphire_5.png'),
	(89, 3, 8, '/ranks/platinum/platinum_3.png'),
	(90, 5, 8, '/ranks/platinum/platinum_5.png'),
	(93, 1, 9, '/ranks/diamond/diamond_1.png'),
	(94, 2, 9, '/ranks/diamond/diamond_2.png'),
	(95, 4, 9, '/ranks/diamond/diamond_4.png'),
	(98, 1, 10, '/ranks/ruby/ruby_1.png'),
	(99, 3, 10, '/ranks/ruby/ruby_3.png'),
	(100, 4, 10, '/ranks/ruby/ruby_4.png'),
	(104, 2, 11, '/ranks/emerald/emerald_2.png'),
	(105, 4, 11, '/ranks/emerald/emerald_4.png'),
	(106, 1, 1, '/ranks/unranked/unranked_1.png');
/*!40000 ALTER TABLE `ranks` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.retrospectives
CREATE TABLE IF NOT EXISTS `retrospectives` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `interval_frequency` int(11) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `media_path` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `retrospective_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueIntervalAndStartDateAndEndDate` (`interval_frequency`,`start_date`,`end_date`),
  KEY `FK45ox2nc7as2dui59tb1uyfgqb` (`account_id`),
  CONSTRAINT `FK45ox2nc7as2dui59tb1uyfgqb` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.retrospectives: ~0 rows (approximately)
/*!40000 ALTER TABLE `retrospectives` DISABLE KEYS */;
/*!40000 ALTER TABLE `retrospectives` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.retrospective_entries
CREATE TABLE IF NOT EXISTS `retrospective_entries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entry_text` longtext DEFAULT NULL,
  `key_point` bit(1) DEFAULT NULL,
  `line_number` int(11) DEFAULT NULL,
  `retrospective_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKepdlvjuqm0ejb712uio01et63` (`retrospective_id`),
  CONSTRAINT `FKepdlvjuqm0ejb712uio01et63` FOREIGN KEY (`retrospective_id`) REFERENCES `retrospectives` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=288 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.retrospective_entries: ~0 rows (approximately)
/*!40000 ALTER TABLE `retrospective_entries` DISABLE KEYS */;
/*!40000 ALTER TABLE `retrospective_entries` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.skills
CREATE TABLE IF NOT EXISTS `skills` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `delta` int(11) DEFAULT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `points` int(11) DEFAULT NULL,
  `step_increment` int(11) DEFAULT NULL,
  `remaining` int(11) DEFAULT NULL,
  `previous_level` int(11) DEFAULT NULL,
  `previous_points` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.skills: ~2 rows (approximately)
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
REPLACE INTO `skills` (`id`, `delta`, `last_updated`, `level`, `points`, `step_increment`, `remaining`, `previous_level`, `previous_points`) VALUES
	(7, -8, '2023-06-28 10:27:18.247097', 1, 0, 100, 100, NULL, NULL);
/*!40000 ALTER TABLE `skills` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.trades
CREATE TABLE IF NOT EXISTS `trades` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `close_price` double DEFAULT NULL,
  `lot_size` double DEFAULT NULL,
  `net_profit` double DEFAULT NULL,
  `open_price` double DEFAULT NULL,
  `reason_for_entrance` varchar(255) DEFAULT NULL,
  `result_of_trade` varchar(255) DEFAULT NULL,
  `trade_close_time` datetime DEFAULT NULL,
  `trade_id` varchar(255) DEFAULT NULL,
  `trade_open_time` datetime DEFAULT NULL,
  `trade_type` int(11) DEFAULT NULL,
  `trading_platform` int(11) DEFAULT NULL,
  `product` varchar(255) DEFAULT NULL,
  `relevant` bit(1) DEFAULT NULL,
  `processed` bit(1) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `trade_platform` int(11) DEFAULT NULL,
  `stop_loss` double DEFAULT NULL,
  `take_profit` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueTradeIdAndAccount` (`trade_id`,`account_id`),
  KEY `FKhrx1ya4wn13vvty5h2t85970t` (`account_id`),
  CONSTRAINT `FKhrx1ya4wn13vvty5h2t85970t` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10118 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trades: ~108 rows (approximately)
/*!40000 ALTER TABLE `trades` DISABLE KEYS */;
REPLACE INTO `trades` (`id`, `close_price`, `lot_size`, `net_profit`, `open_price`, `reason_for_entrance`, `result_of_trade`, `trade_close_time`, `trade_id`, `trade_open_time`, `trade_type`, `trading_platform`, `product`, `relevant`, `processed`, `account_id`, `trade_platform`, `stop_loss`, `take_profit`) VALUES
	(10010, 14728.5, 20, -137.16, 14723.3, NULL, NULL, '2023-06-27 17:13:07', '104350469', '2023-06-27 17:13:02', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10011, 14768.1, 8, 14.75, 14769.5, NULL, NULL, '2023-06-27 16:41:35', '104347113', '2023-06-27 16:40:16', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14767.24, 14729.42),
	(10012, 14766.9, 8, -139.21, 14780.1, NULL, NULL, '2023-06-27 16:47:57', '104347673', '2023-06-27 16:47:27', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10013, 14767.9, 8, -77.99, 14775.3, NULL, NULL, '2023-06-27 16:46:27', '104347496', '2023-06-27 16:45:29', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14768.13, 14813.99),
	(10014, 14729.8, 8, -8.44, 14729, NULL, NULL, '2023-06-27 17:12:34', '104350367', '2023-06-27 17:12:06', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10015, 14765.8, 15, 286.57, 14751.3, NULL, NULL, '2023-06-27 17:25:29', '104351530', '2023-06-27 17:23:35', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14755.94, 14784.91),
	(10016, 14752, 8, -131.72, 14739.5, NULL, NULL, '2023-06-27 17:23:15', '104351475', '2023-06-27 17:23:01', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10017, 14774.1, 8, -109.63, 14763.7, NULL, NULL, '2023-06-27 16:46:52', '104347599', '2023-06-27 16:46:34', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10018, 14771.9, 4, -60.04, 14783.3, NULL, NULL, '2023-06-27 16:43:51', '104347319', '2023-06-27 16:42:56', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14772.6, 14806.84),
	(10019, 14759.1, 8, -86.41, 14767.3, NULL, NULL, '2023-06-27 17:30:15', '104352021', '2023-06-27 17:29:48', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14755.31, 0),
	(10020, 14752, 30, 406.93, 14762.3, NULL, NULL, '2023-06-27 17:32:54', '104352240', '2023-06-27 17:32:37', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10021, 14774.8, 8, -83.32, 14782.7, NULL, NULL, '2023-06-27 17:06:16', '104349651', '2023-06-27 17:05:34', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14775.11, 14831.99),
	(10022, 14744.5, 30, 7.91, 14744.3, NULL, NULL, '2023-06-27 17:17:20', '104350913', '2023-06-27 17:17:08', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10023, 14786.9, 8, 16.86, 14785.3, NULL, NULL, '2023-06-27 16:51:40', '104348044', '2023-06-27 16:50:48', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14779.15, 14803.57),
	(10024, 14783.1, 8, -20.03, 14785, NULL, NULL, '2023-06-27 16:57:14', '104348341', '2023-06-27 16:55:20', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14772.01, 14817.56),
	(10025, 14781.9, 8, 76.93, 14774.6, NULL, NULL, '2023-06-27 16:35:19', '104346454', '2023-06-27 16:35:02', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14770.27, 0),
	(10026, 14788.4, 8, 108.56, 14778.1, NULL, NULL, '2023-06-27 16:51:20', '104347762', '2023-06-27 16:48:36', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14811.61),
	(10027, 14732, 8, 52.74, 14737, NULL, NULL, '2023-06-27 17:12:36', '104350357', '2023-06-27 17:12:01', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10028, 14750.5, 20, 39.55, 14749, NULL, NULL, '2023-06-27 17:15:29', '104350732', '2023-06-27 17:15:16', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10029, 14797.3, 18, 73.55, 14794.2, NULL, NULL, '2023-06-27 17:03:52', '104349344', '2023-06-27 17:03:08', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10030, 14769.8, 30, 82.96, 14767.7, NULL, NULL, '2023-06-27 17:30:57', '104352138', '2023-06-27 17:30:48', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10031, 14741.5, 20, 73.85, 14744.3, NULL, NULL, '2023-06-27 17:15:07', '104350631', '2023-06-27 17:14:24', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10032, 14749, 30, 177.89, 14753.5, NULL, NULL, '2023-06-27 17:20:53', '104351288', '2023-06-27 17:20:43', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10033, 14789.1, 8, -244.7, 14812.3, NULL, NULL, '2023-06-27 17:01:25', '104348996', '2023-06-27 17:00:33', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14826.2),
	(10034, 14761.8, 30, 177.88, 14757.3, NULL, NULL, '2023-06-27 17:20:26', '104351242', '2023-06-27 17:20:09', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10035, 14760.1, 20, -252.83, 14769.7, NULL, NULL, '2023-06-27 17:31:43', '104352178', '2023-06-27 17:31:34', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10036, 14761.7, 30, 94.82, 14764.1, NULL, NULL, '2023-06-27 17:31:20', '104352156', '2023-06-27 17:31:08', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10037, 14783.6, 8, 7.38, 14782.9, NULL, NULL, '2023-06-27 16:38:08', '104346616', '2023-06-27 16:37:09', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14769.92, 14797.32),
	(10038, 14755, 12, 30.05, 14756.9, NULL, NULL, '2023-06-27 17:08:31', '104349929', '2023-06-27 17:08:23', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10039, 14748.5, 8, 18.97, 14750.3, NULL, NULL, '2023-06-27 17:23:13', '104351342', '2023-06-27 17:21:21', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14748.98, 14721.78),
	(10040, 14757.1, 8, 105.32, 14767.1, NULL, NULL, '2023-06-27 16:31:33', '104345766', '2023-06-27 16:30:46', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14765.6, 0),
	(10041, 14785.8, 8, -107.59, 14796, NULL, NULL, '2023-06-27 17:01:22', '104348538', '2023-06-27 16:58:29', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14786.3, 14840.19),
	(10042, 14770.1, 8, -133.92, 14757.4, NULL, NULL, '2023-06-27 16:48:25', '104347724', '2023-06-27 16:48:02', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10043, 14750.3, 8, -47.42, 14745.8, NULL, NULL, '2023-06-27 17:22:42', '104351362', '2023-06-27 17:21:41', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14757.08, 14739.49),
	(10044, 14760.3, 20, -136.94, 14765.5, NULL, NULL, '2023-06-27 17:32:20', '104352195', '2023-06-27 17:31:51', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14775.93),
	(10045, 14743.3, 20, 237.26, 14752.3, NULL, NULL, '2023-06-27 17:10:42', '104350172', '2023-06-27 17:10:23', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14750.03, 0),
	(10046, 14766.7, 8, -99.06, 14757.3, NULL, NULL, '2023-06-27 17:30:43', '104352096', '2023-06-27 17:30:17', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14769.1, 14740),
	(10047, 14740.2, 8, 32.65, 14737.1, NULL, NULL, '2023-06-27 16:33:07', '104345748', '2023-06-27 16:32:54', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10048, 14741.8, 8, 477.7, 14787.1, NULL, NULL, '2023-06-27 17:10:54', '104349805', '2023-06-27 17:07:04', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14766.08, 14694.74),
	(10049, 14774.8, 8, -208.83, 14794.6, NULL, NULL, '2023-06-27 17:06:16', '104349560', '2023-06-27 17:05:01', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14776.33, 14842.54),
	(10050, 14778.8, 25, -435.16, 14792, NULL, NULL, '2023-06-27 17:02:46', '104349188', '2023-06-27 17:01:48', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10051, 14804, 18, -246.74, 14793.6, NULL, NULL, '2023-06-27 17:04:37', '104349480', '2023-06-27 17:04:13', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10052, 14763.2, 15, 21.74, 14762.1, NULL, NULL, '2023-06-27 17:25:03', '104351638', '2023-06-27 17:24:47', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10053, 14785.9, 16, 345.72, 14769.5, NULL, NULL, '2023-06-27 17:28:23', '104351737', '2023-06-27 17:26:06', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14776.18, 14794.4),
	(10054, 14903, 20, 44.82, 14901.3, NULL, NULL, '2023-06-27 20:20:09', '104359381', '2023-06-27 20:19:09', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14916.2),
	(10055, 14876.9, 8, -99.14, 14886.3, NULL, NULL, '2023-06-27 20:15:32', '104359184', '2023-06-27 20:11:12', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14894.35),
	(10056, 14846.8, 8, -51.68, 14841.9, NULL, NULL, '2023-06-27 19:08:51', '104357182', '2023-06-27 19:08:12', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14846.81, 14834.57),
	(10057, 14916.5, 25, 125.18, 14912.7, NULL, NULL, '2023-06-27 20:30:59', '104359660', '2023-06-27 20:30:30', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10058, 14863.9, 35, 36.91, 14863.1, NULL, NULL, '2023-06-27 19:35:17', '104358151', '2023-06-27 19:32:38', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14854.15, 14877.29),
	(10059, 14889, 35, 350.67, 14881.4, NULL, NULL, '2023-06-27 20:17:09', '104359301', '2023-06-27 20:15:45', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14879.78, 14897.12),
	(10060, 14839.9, 20, 39.56, 14841.4, NULL, NULL, '2023-06-27 19:09:45', '104357221', '2023-06-27 19:09:34', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10061, 14833.2, 8, 11.59, 14832.1, NULL, NULL, '2023-06-27 18:47:17', '104356277', '2023-06-27 18:44:11', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14827.74, 14861.86),
	(10062, 14877.2, 8, -56.95, 14882.6, NULL, NULL, '2023-06-27 20:15:30', '104359002', '2023-06-27 20:02:10', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14896.43),
	(10063, 14841.6, 20, 39.56, 14840.1, NULL, NULL, '2023-06-27 19:10:39', '104357266', '2023-06-27 19:10:30', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10064, 14876.3, 8, -121.29, 14887.8, NULL, NULL, '2023-06-27 20:15:31', '104359025', '2023-06-27 20:03:27', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14900.59),
	(10065, 14910.8, 8, -4.22, 14911.2, NULL, NULL, '2023-06-27 20:23:32', '104359421', '2023-06-27 20:20:52', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14901.98, 14923.83),
	(10066, 14841.8, 8, -92.85, 14850.6, NULL, NULL, '2023-06-27 19:18:01', '104357445', '2023-06-27 19:15:23', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14841.84, 14860.06),
	(10067, 14838.4, 30, -253.18, 14844.8, NULL, NULL, '2023-06-27 19:20:34', '104357588', '2023-06-27 19:20:09', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10068, 14856.9, 35, 378.32, 14848.7, NULL, NULL, '2023-06-27 19:31:47', '104357720', '2023-06-27 19:24:19', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14857.35, 14872.12),
	(10069, 14882.6, 25, 3.3, 14882.5, NULL, NULL, '2023-06-27 20:01:53', '104358975', '2023-06-27 20:01:15', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10070, 14906.1, 35, 96.86, 14904, NULL, NULL, '2023-06-27 20:29:49', '104359625', '2023-06-27 20:29:42', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10071, 14839.9, 20, 18.46, 14840.6, NULL, NULL, '2023-06-27 19:18:49', '104357521', '2023-06-27 19:18:02', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14826.58),
	(10072, 14894.2, 25, -260.32, 14902.1, NULL, NULL, '2023-06-27 19:50:20', '104358615', '2023-06-27 19:48:11', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14888.39, 14908.17),
	(10073, 14844.9, 30, 59.33, 14843.4, NULL, NULL, '2023-06-27 19:22:46', '104357605', '2023-06-27 19:20:44', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14871.13),
	(10074, 14910.5, 35, 73.79, 14908.9, NULL, NULL, '2023-06-27 20:30:15', '104359649', '2023-06-27 20:30:05', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10075, 14850.6, 8, -105.48, 14840.6, NULL, NULL, '2023-06-27 19:15:22', '104357351', '2023-06-27 19:13:05', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14849.1, 14827.54),
	(10076, 14912.8, 8, 70.64, 14906.1, NULL, NULL, '2023-06-27 20:23:24', '104359411', '2023-06-27 20:20:18', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14903.37, 14918.28),
	(10077, 14884.6, 25, 85.69, 14887.2, NULL, NULL, '2023-06-27 20:00:39', '104358961', '2023-06-27 20:00:27', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10078, 14873.2, 15, 156.17, 14865.3, NULL, NULL, '2023-06-27 19:42:41', '104358301', '2023-06-27 19:37:42', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14873.33, 14887.51),
	(10079, 14844.4, 30, -296.72, 14836.9, NULL, NULL, '2023-06-27 19:20:06', '104357553', '2023-06-27 19:19:04', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14835.2),
	(10080, 14840.7, 8, 11.6, 14839.6, NULL, NULL, '2023-06-27 19:07:37', '104356400', '2023-06-27 18:52:01', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14840.78, 14859.25),
	(10081, 14847.1, 30, -138.42, 14843.6, NULL, NULL, '2023-06-27 19:24:09', '104357698', '2023-06-27 19:23:24', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14826.83),
	(10082, 14841.8, 8, -120.28, 14853.2, NULL, NULL, '2023-06-27 19:18:01', '104357455', '2023-06-27 19:15:44', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14841.84, 14868.18),
	(10083, 14900.6, 25, -197.68, 14894.6, NULL, NULL, '2023-06-27 19:47:57', '104358547', '2023-06-27 19:45:40', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14900.5, 14888.1),
	(10084, 14893.6, 8, 15.82, 14892.1, NULL, NULL, '2023-06-27 20:18:08', '104359337', '2023-06-27 20:17:33', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14880.48, 14910.65),
	(10085, 14910.1, 16, -120.17, 14915.8, NULL, NULL, '2023-06-27 20:28:18', '104359513', '2023-06-27 20:24:00', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14910.17, 14921.4),
	(10086, 14885.8, 35, 406.06, 14894.6, NULL, NULL, '2023-06-27 19:58:13', '104358689', '2023-06-27 19:50:32', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14894, 14878.94),
	(10087, 14906.1, 35, -575.44, 14893.7, NULL, NULL, '2023-06-28 17:10:52', '104391370', '2023-06-28 17:10:12', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14885.04),
	(10088, 14940.2, 8, 93.37, 14931.4, NULL, NULL, '2023-06-28 17:18:48', '104392161', '2023-06-28 17:18:34', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14939.03),
	(10089, 14907.9, 35, -566.34, 14895.7, NULL, NULL, '2023-06-28 16:56:31', '104390100', '2023-06-28 16:56:11', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10090, 14897.4, 35, -9.29, 14897.2, NULL, NULL, '2023-06-28 17:15:09', '104391597', '2023-06-28 17:12:49', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14879.42),
	(10091, 14900.7, 35, -352.91, 14908.3, NULL, NULL, '2023-06-28 17:12:40', '104391552', '2023-06-28 17:12:24', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10092, 14903.9, 8, -99.72, 14894.5, NULL, NULL, '2023-06-28 17:07:07', '104391079', '2023-06-28 17:06:54', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10093, 14919.3, 8, 113.54, 14908.6, NULL, NULL, '2023-06-28 16:38:54', '104387712', '2023-06-28 16:36:45', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14909.65, 14931.1),
	(10094, 14900.6, 8, 9.55, 14901.5, NULL, NULL, '2023-06-28 17:06:13', '104390956', '2023-06-28 17:05:06', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14900.71, 14870.98),
	(10095, 14861.9, 8, -158.08, 14876.8, NULL, NULL, '2023-06-28 16:31:31', '104386924', '2023-06-28 16:31:10', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14862.65, 0),
	(10096, 14886.4, 8, 77.5, 14893.7, NULL, NULL, '2023-06-28 16:45:26', '104388645', '2023-06-28 16:45:15', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10097, 14933.5, 35, 1141.78, 14908.9, NULL, NULL, '2023-06-28 16:57:39', '104390138', '2023-06-28 16:56:33', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14955.68),
	(10098, 14908.7, 35, 129.94, 14905.9, NULL, NULL, '2023-06-28 17:07:25', '104391105', '2023-06-28 17:07:16', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10099, 14920.4, 35, 1021.34, 14898.4, NULL, NULL, '2023-06-28 17:16:25', '104391886', '2023-06-28 17:15:15', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14928.04),
	(10100, 14904.1, 8, -107.17, 14894, NULL, NULL, '2023-06-28 16:55:07', '104389974', '2023-06-28 16:54:45', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10101, 14904.6, 8, -48.81, 14900, NULL, NULL, '2023-06-28 16:55:06', '104389892', '2023-06-28 16:53:38', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14875.12),
	(10102, 14898.9, 8, 18.04, 14897.2, NULL, NULL, '2023-06-28 16:34:32', '104387331', '2023-06-28 16:33:53', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10103, 14902.2, 8, -13.81, 14903.5, NULL, NULL, '2023-06-28 16:48:28', '104389041', '2023-06-28 16:48:05', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10104, 14897.5, 8, -133.66, 14910.1, NULL, NULL, '2023-06-28 17:10:08', '104391201', '2023-06-28 17:08:14', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14890.53, 14924.83),
	(10105, 14939.2, 8, 244.03, 14916.2, NULL, NULL, '2023-06-28 17:18:51', '104392055', '2023-06-28 17:17:06', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14917.32, 14942.11),
	(10106, 14898.1, 8, -228.1, 14919.6, NULL, NULL, '2023-06-28 16:53:34', '104389827', '2023-06-28 16:52:34', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10107, 14904, 35, -41.78, 14904.9, NULL, NULL, '2023-06-28 17:11:40', '104391439', '2023-06-28 17:11:00', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 14925.09),
	(10108, 14910.5, 8, -32.91, 14913.6, NULL, NULL, '2023-06-28 16:49:35', '104389227', '2023-06-28 16:49:09', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10109, 14908, 8, -15.92, 14906.5, NULL, NULL, '2023-06-28 16:44:44', '104388513', '2023-06-28 16:44:25', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14905.45, 14883.68),
	(10110, 14879.9, 8, 132.64, 14867.4, NULL, NULL, '2023-06-28 16:32:18', '104386977', '2023-06-28 16:31:34', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14859.79, 14895.39),
	(10111, 14902.3, 8, -101.94, 14892.7, NULL, NULL, '2023-06-28 16:48:02', '104388999', '2023-06-28 16:47:44', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14900.94, 14882.87),
	(10112, 14908.2, 35, -171.8, 14904.5, NULL, NULL, '2023-06-28 17:12:22', '104391512', '2023-06-28 17:11:51', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10113, 14924.7, 8, 9.55, 14923.8, NULL, NULL, '2023-06-28 16:41:30', '104388136', '2023-06-28 16:40:52', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14918.84, 14954.81),
	(10114, 14901, 8, 136.97, 14913.9, NULL, NULL, '2023-06-28 16:50:53', '104389362', '2023-06-28 16:49:43', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14912.06, 14888.77),
	(10115, 14910.4, 8, -87.04, 14902.2, NULL, NULL, '2023-06-28 16:48:45', '104389088', '2023-06-28 16:48:32', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 0, 0),
	(10116, 14910.5, 8, 48.82, 14905.9, NULL, NULL, '2023-06-28 17:03:59', '104390827', '2023-06-28 17:03:24', 0, NULL, 'us100.cash', b'1', b'1', 7, 1, 14900.98, 14913.44),
	(10117, 14908.6, 8, -73.23, 14901.7, NULL, NULL, '2023-06-28 17:04:53', '104390927', '2023-06-28 17:04:44', 1, NULL, 'us100.cash', b'1', b'1', 7, 1, 14907.55, 0);
/*!40000 ALTER TABLE `trades` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.trade_reasons_for_entry
CREATE TABLE IF NOT EXISTS `trade_reasons_for_entry` (
  `trade_id` bigint(20) NOT NULL,
  `reasons_for_entry` int(11) DEFAULT NULL,
  KEY `FKdmv82ac2e6ouc3vlqp82jnhd4` (`trade_id`),
  CONSTRAINT `FKdmv82ac2e6ouc3vlqp82jnhd4` FOREIGN KEY (`trade_id`) REFERENCES `trades` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trade_reasons_for_entry: ~0 rows (approximately)
/*!40000 ALTER TABLE `trade_reasons_for_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `trade_reasons_for_entry` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.trade_records
CREATE TABLE IF NOT EXISTS `trade_records` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aggregate_interval` int(11) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `statistics_id` bigint(20) DEFAULT NULL,
  `target` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlt8dfxccamqy5gcglyv6jqjo3` (`account_id`),
  KEY `FKi5sk7nqeg33tum7hvh9oj2tm5` (`statistics_id`),
  CONSTRAINT `FKi5sk7nqeg33tum7hvh9oj2tm5` FOREIGN KEY (`statistics_id`) REFERENCES `trade_records_statistics` (`id`),
  CONSTRAINT `FKlt8dfxccamqy5gcglyv6jqjo3` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6258 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trade_records: ~5 rows (approximately)
/*!40000 ALTER TABLE `trade_records` DISABLE KEYS */;
REPLACE INTO `trade_records` (`id`, `aggregate_interval`, `balance`, `end_date`, `start_date`, `account_id`, `statistics_id`, `target`) VALUES
	(6253, 3, 30666.47, '2024-01-01', '2023-01-01', 7, 6511, 383.33),
	(6254, 2, 30666.47, '2023-07-01', '2023-06-01', 7, 6512, 383.33),
	(6255, 1, 30666.47, '2023-07-03', '2023-06-26', 7, 6513, 383.33),
	(6256, 0, 30307.35, '2023-06-28', '2023-06-27', 7, 6514, 378.84),
	(6257, 0, 30666.47, '2023-06-29', '2023-06-28', 7, 6515, 383.33);
/*!40000 ALTER TABLE `trade_records` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.trade_records_statistics
CREATE TABLE IF NOT EXISTS `trade_records_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `average_loss_amount` double DEFAULT NULL,
  `average_loss_size` double DEFAULT NULL,
  `average_win_amount` double DEFAULT NULL,
  `average_win_size` double DEFAULT NULL,
  `largest_loss_amount` double DEFAULT NULL,
  `largest_loss_size` double DEFAULT NULL,
  `largest_win_amount` double DEFAULT NULL,
  `largest_win_size` double DEFAULT NULL,
  `net_profit` double DEFAULT NULL,
  `number_of_losing_trades` int(11) DEFAULT NULL,
  `number_of_trades` int(11) DEFAULT NULL,
  `number_of_winning_trades` int(11) DEFAULT NULL,
  `percentage_profit` double DEFAULT NULL,
  `trading_rate` double DEFAULT NULL,
  `win_percentage` int(11) DEFAULT NULL,
  `pips_earned` double DEFAULT NULL,
  `pips_lost` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6516 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trade_records_statistics: ~5 rows (approximately)
/*!40000 ALTER TABLE `trade_records_statistics` DISABLE KEYS */;
REPLACE INTO `trade_records_statistics` (`id`, `average_loss_amount`, `average_loss_size`, `average_win_amount`, `average_win_size`, `largest_loss_amount`, `largest_loss_size`, `largest_win_amount`, `largest_win_size`, `net_profit`, `number_of_losing_trades`, `number_of_trades`, `number_of_winning_trades`, `percentage_profit`, `trading_rate`, `win_percentage`, `pips_earned`, `pips_lost`) VALUES
	(6511, -144.3, 14.32, 145.89, 18.27, -575.44, 35, 1141.78, 35, 666.47, 52, 108, 56, 2.17, 54, 52, 354.3, 436.6),
	(6512, -144.3, 14.32, 145.89, 18.27, -575.44, 35, 1141.78, 35, 666.47, 52, 108, 56, 2.17, 54, 52, 354.3, 436.6),
	(6513, -144.3, 14.32, 145.89, 18.27, -575.44, 35, 1141.78, 35, 666.47, 52, 108, 56, 2.17, 54, 52, 354.3, 436.6),
	(6514, -137.81, 12.91, 116.12, 19.49, -435.16, 25, 477.7, 8, 307.35, 34, 77, 43, 1.01, 77, 56, 221.6, 295.9),
	(6515, -156.55, 17, 244.39, 14.23, -575.44, 35, 1141.78, 35, 359.12, 18, 31, 13, 1.17, 31, 42, 132.7, 140.7);
/*!40000 ALTER TABLE `trade_records_statistics` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.trade_results_of_trade
CREATE TABLE IF NOT EXISTS `trade_results_of_trade` (
  `trade_id` bigint(20) NOT NULL,
  `results_of_trade` int(11) DEFAULT NULL,
  KEY `FKqvj3hllwoou3vrsci7qjxjxq8` (`trade_id`),
  CONSTRAINT `FKqvj3hllwoou3vrsci7qjxjxq8` FOREIGN KEY (`trade_id`) REFERENCES `trades` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trade_results_of_trade: ~0 rows (approximately)
/*!40000 ALTER TABLE `trade_results_of_trade` DISABLE KEYS */;
/*!40000 ALTER TABLE `trade_results_of_trade` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.trading_plans
CREATE TABLE IF NOT EXISTS `trading_plans` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `compound_frequency` int(11) DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `profit_target` double DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `starting_balance` double DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `deposit_plan_id` bigint(20) DEFAULT NULL,
  `withdrawal_plan_id` bigint(20) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `absolute` bit(1) DEFAULT NULL,
  `aggregate_interval` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueNameAndStartDateAndEndDate` (`name`,`start_date`,`end_date`),
  KEY `FK2jc1tmys8i4y1dc8mo3dv31af` (`deposit_plan_id`),
  KEY `FKa20wf8ar6x1mvkkmb2mo0w14b` (`withdrawal_plan_id`),
  KEY `FKruni9vofdno2ve4uit2w6d3ep` (`account_id`),
  CONSTRAINT `FK2jc1tmys8i4y1dc8mo3dv31af` FOREIGN KEY (`deposit_plan_id`) REFERENCES `deposit_plans` (`id`),
  CONSTRAINT `FKa20wf8ar6x1mvkkmb2mo0w14b` FOREIGN KEY (`withdrawal_plan_id`) REFERENCES `withdrawal_plans` (`id`),
  CONSTRAINT `FKruni9vofdno2ve4uit2w6d3ep` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trading_plans: ~1 rows (approximately)
/*!40000 ALTER TABLE `trading_plans` DISABLE KEYS */;
REPLACE INTO `trading_plans` (`id`, `active`, `compound_frequency`, `end_date`, `name`, `profit_target`, `start_date`, `starting_balance`, `status`, `deposit_plan_id`, `withdrawal_plan_id`, `account_id`, `absolute`, `aggregate_interval`) VALUES
	(5, b'1', 1, '2023-06-27', 'FTMO Challenge Trading Plan', 1.25, '2023-07-28', 30000, 1, NULL, NULL, 7, b'0', 0);
/*!40000 ALTER TABLE `trading_plans` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `user_locale_id` bigint(20) DEFAULT NULL,
  `phone_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3pwaj86pwopu3ot96qlrfo2up` (`account_id`),
  KEY `FKd7vh3l278x7si0bsvpayv312o` (`user_locale_id`),
  KEY `FK4kq8bl1u7lv3i6l7b5b725sf2` (`phone_id`),
  CONSTRAINT `FK3pwaj86pwopu3ot96qlrfo2up` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK4kq8bl1u7lv3i6l7b5b725sf2` FOREIGN KEY (`phone_id`) REFERENCES `phone_numbers` (`id`),
  CONSTRAINT `FKd7vh3l278x7si0bsvpayv312o` FOREIGN KEY (`user_locale_id`) REFERENCES `user_locales` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.users: ~1 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
REPLACE INTO `users` (`id`, `email`, `first_name`, `last_name`, `password`, `username`, `account_id`, `user_locale_id`, `phone_id`) VALUES
	(5, 's.prizio@hotmail.com', 'Stephen', 'Prizio', '$2a$10$/.HyJQA84yfceXoYgPO9euce1PmQNfisSI6GA7G52yHUBBxEBBHo6', 's.prizio', 7, 1, 1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.user_locales
CREATE TABLE IF NOT EXISTS `user_locales` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country` int(11) DEFAULT NULL,
  `time_zone_offset` varchar(50) DEFAULT NULL,
  `town_city` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.user_locales: ~0 rows (approximately)
/*!40000 ALTER TABLE `user_locales` DISABLE KEYS */;
REPLACE INTO `user_locales` (`id`, `country`, `time_zone_offset`, `town_city`) VALUES
	(1, 5, 'America/Toronto (GMT -05:00)', 'Montreal');
/*!40000 ALTER TABLE `user_locales` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.user_locale_currencies
CREATE TABLE IF NOT EXISTS `user_locale_currencies` (
  `user_locale_id` bigint(20) NOT NULL,
  `currencies` int(11) DEFAULT NULL,
  KEY `FK34j8mps1k0nj9bnl5k8yl0r8w` (`user_locale_id`),
  CONSTRAINT `FK34j8mps1k0nj9bnl5k8yl0r8w` FOREIGN KEY (`user_locale_id`) REFERENCES `user_locales` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.user_locale_currencies: ~2 rows (approximately)
/*!40000 ALTER TABLE `user_locale_currencies` DISABLE KEYS */;
REPLACE INTO `user_locale_currencies` (`user_locale_id`, `currencies`) VALUES
	(1, 0),
	(1, 5);
/*!40000 ALTER TABLE `user_locale_currencies` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.user_locale_languages
CREATE TABLE IF NOT EXISTS `user_locale_languages` (
  `user_locale_id` bigint(20) NOT NULL,
  `languages` int(11) DEFAULT NULL,
  KEY `FKd14i3qhxjotd6oinujfodqjtf` (`user_locale_id`),
  CONSTRAINT `FKd14i3qhxjotd6oinujfodqjtf` FOREIGN KEY (`user_locale_id`) REFERENCES `user_locales` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.user_locale_languages: ~3 rows (approximately)
/*!40000 ALTER TABLE `user_locale_languages` DISABLE KEYS */;
REPLACE INTO `user_locale_languages` (`user_locale_id`, `languages`) VALUES
	(1, 0),
	(1, 1),
	(1, 5);
/*!40000 ALTER TABLE `user_locale_languages` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.user_roles
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `roles` int(11) DEFAULT NULL,
  KEY `FKhfh9dx7w3ubf1co1vdev94g3f` (`user_id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.user_roles: ~2 rows (approximately)
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
REPLACE INTO `user_roles` (`user_id`, `roles`) VALUES
	(5, 0),
	(5, 1);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.withdrawal_plans
CREATE TABLE IF NOT EXISTS `withdrawal_plans` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `frequency` int(11) DEFAULT NULL,
  `absolute` bit(1) DEFAULT NULL,
  `aggregate_interval` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.withdrawal_plans: ~0 rows (approximately)
/*!40000 ALTER TABLE `withdrawal_plans` DISABLE KEYS */;
/*!40000 ALTER TABLE `withdrawal_plans` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

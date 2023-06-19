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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.account: ~1 rows (approximately)
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
REPLACE INTO `account` (`id`, `account_id`, `account_open_time`, `active`, `balance`, `trading_plan_id`, `user_id`, `skills_id`, `rank_id`, `name`, `account_number`, `currency`, `account_type`, `broker`, `daily_stop_limit`, `daily_stop_limit_type`, `default_account`, `trade_platform`) VALUES
	(5, '5', '2023-06-05 14:56:49.000000', b'1', 16295.83, NULL, 5, 5, 60, 'FTMO Challenge', 1300451678, 5, 2, 1, 55, 1, b'1', 1);
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
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news: ~17 rows (approximately)
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
	(104, '2023-06-23');
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
) ENGINE=InnoDB AUTO_INCREMENT=666 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news_entries: ~283 rows (approximately)
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
	(744, 'Retail Sales m/m', 2, 663, 13, '-0.2%', '0.5%');
/*!40000 ALTER TABLE `market_news_entries` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.market_news_slots
CREATE TABLE IF NOT EXISTS `market_news_slots` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` time DEFAULT NULL,
  `news_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgx55cd7fhbmhyfm2jig419vpp` (`news_id`),
  CONSTRAINT `FKgx55cd7fhbmhyfm2jig419vpp` FOREIGN KEY (`news_id`) REFERENCES `market_news` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=610 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news_slots: ~196 rows (approximately)
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
	(663, '02:00:00', 104);
/*!40000 ALTER TABLE `market_news_slots` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.phone_numbers
CREATE TABLE IF NOT EXISTS `phone_numbers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country_code` smallint(6) DEFAULT NULL,
  `phone_type` int(11) DEFAULT NULL,
  `telephone_number` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.phone_numbers: ~1 rows (approximately)
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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.retrospectives: ~1 rows (approximately)
/*!40000 ALTER TABLE `retrospectives` DISABLE KEYS */;
REPLACE INTO `retrospectives` (`id`, `end_date`, `interval_frequency`, `start_date`, `account_id`, `media_path`, `name`, `retrospective_type`) VALUES
	(53, '2023-06-12', 1, '2023-06-05', 5, 'path', 'Weekly Retrospective', 1),
	(55, '2023-06-19', 1, '2023-06-12', 5, 'path', 'name', 1);
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
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.retrospective_entries: ~6 rows (approximately)
/*!40000 ALTER TABLE `retrospective_entries` DISABLE KEYS */;
REPLACE INTO `retrospective_entries` (`id`, `entry_text`, `key_point`, `line_number`, `retrospective_id`) VALUES
	(276, 'This week marked the beginning of my first FTMO Challenge ($15,000 Account). It\'s an exciting time! I\'m testing all of my knowledge and experience that I\'ve earned since embarking on my trading journey.', b'0', 1, 53),
	(277, 'I traded 3 sessions and did well in each of them. I am still feeling a little nervous, which has caused me to be a little too quick to sell and I have struggled to accept the risk of each trade.', b'0', 2, 53),
	(278, 'I made good calls this week, the price action was pretty clean. I am product of myself for that.', b'0', 3, 53),
	(279, 'Looking forward I want to focus on accepting the risk for each trade and controlling possible tilt; as well as not having any expectations for the market movement. Let\'s have a good week!', b'0', 4, 53),
	(280, 'Accept the risk.', b'1', 5, 53),
	(281, 'Let the market move. Don\'t assume directions.', b'1', 6, 53),
	(282, 'Second week of the FTMO challenge. I turned in some solid performances this week. I\'ve been reading the market well for the most part and I haven\'t been married to any positions.', b'0', 1, 55),
	(283, 'I traded well despite CPI and FOMC this week as well as a triple-witching day on Friday. I\'m feeling confident and resilient in my trading.', b'0', 2, 55),
	(284, 'I did struggle at times with consecutive losses and oversized positions that I consider myself fortunate to have survived. I must be better with that. There is no more excuse. I must graduate.', b'0', 3, 55),
	(285, 'Looking ahead to next week, we have a holiday on Monday so it is going to be a 4 session week. Let\'s go.', b'0', 4, 55),
	(286, 'Keep a consistent position size.', b'1', 5, 55),
	(287, 'Don\'t let losses pile up needlessly', b'1', 6, 55);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.skills: ~1 rows (approximately)
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
REPLACE INTO `skills` (`id`, `delta`, `last_updated`, `level`, `points`, `step_increment`, `remaining`, `previous_level`, `previous_points`) VALUES
	(5, -3, '2023-06-16 10:27:46.259022', 2, 34, 100, 66, NULL, NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=9892 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trades: ~168 rows (approximately)
/*!40000 ALTER TABLE `trades` DISABLE KEYS */;
REPLACE INTO `trades` (`id`, `close_price`, `lot_size`, `net_profit`, `open_price`, `reason_for_entrance`, `result_of_trade`, `trade_close_time`, `trade_id`, `trade_open_time`, `trade_type`, `trading_platform`, `product`, `relevant`, `processed`, `account_id`, `trade_platform`, `stop_loss`, `take_profit`) VALUES
	(9724, 14335.3, 3.5, -47.25, 14345.4, NULL, NULL, '2023-06-08 16:46:40', '62951743', '2023-06-08 16:46:02', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14336.12, 14359.84),
	(9725, 14596.7, 3.5, -79.92, 14579.6, NULL, NULL, '2023-06-07 17:10:50', '62867129', '2023-06-07 17:10:02', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14594.73, 14565.95),
	(9726, 14389.5, 3.5, 31.8, 14382.7, NULL, NULL, '2023-06-08 17:06:10', '62955867', '2023-06-08 17:05:49', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14388.9),
	(9727, 14575.3, 3.5, 69.6, 14590.2, NULL, NULL, '2023-06-07 17:14:04', '62867700', '2023-06-07 17:12:17', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14601.44, 14572.94),
	(9728, 14479.6, 3.5, -56.14, 14491.6, NULL, NULL, '2023-06-07 17:37:39', '62872678', '2023-06-07 17:36:20', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14479.55, 14503.22),
	(9729, 14588.6, 3.5, -19.14, 14584.5, NULL, NULL, '2023-06-07 17:08:35', '62866317', '2023-06-07 17:06:15', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14596.13, 14569.02),
	(9730, 14374.3, 3.5, 30.87, 14367.7, NULL, NULL, '2023-06-08 17:10:05', '62956612', '2023-06-08 17:09:35', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14361.16, 14373.84),
	(9731, 14604.5, 3.5, 54.84, 14616.2, NULL, NULL, '2023-06-07 16:54:36', '62862534', '2023-06-07 16:53:46', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14614.85, 14591.1),
	(9732, 14360.6, 3.5, -2.8, 14361.2, NULL, NULL, '2023-06-08 17:04:47', '62955067', '2023-06-08 17:02:41', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14339.76, 14385.46),
	(9733, 14351.6, 3.5, 17.31, 14347.9, NULL, NULL, '2023-06-08 16:51:52', '62952792', '2023-06-08 16:49:22', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14325.38, 14366.52),
	(9734, 14467.7, 3.5, 8.88, 14469.6, NULL, NULL, '2023-06-08 20:11:24', '62973451', '2023-06-08 20:07:19', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14476.52, 14454.93),
	(9735, 14638.5, 3.5, 60.66, 14651.5, NULL, NULL, '2023-06-09 16:56:04', '63032278', '2023-06-09 16:54:50', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9736, 14452.8, 3.5, -64.96, 14466.7, NULL, NULL, '2023-06-08 20:20:51', '62974163', '2023-06-08 20:20:06', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14471.02),
	(9737, 14408.9, 3.5, 37.84, 14400.8, NULL, NULL, '2023-06-08 17:32:12', '62959671', '2023-06-08 17:30:18', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14390.53, 14407.55),
	(9738, 14574.2, 3.5, 23.35, 14569.2, NULL, NULL, '2023-06-09 16:35:36', '63028040', '2023-06-09 16:35:20', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14572.85),
	(9739, 14431.6, 3.5, 7.48, 14430, NULL, NULL, '2023-06-07 17:55:29', '62875833', '2023-06-07 17:54:48', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9740, 14554, 3.5, 53.68, 14542.5, NULL, NULL, '2023-06-09 16:31:07', '63027033', '2023-06-09 16:31:01', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9741, 14490.1, 3.5, 53.8, 14478.6, NULL, NULL, '2023-06-07 17:39:35', '62872960', '2023-06-07 17:37:52', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14469.93, 14492.16),
	(9742, 14521.2, 3.5, 37.84, 14513.1, NULL, NULL, '2023-06-07 17:26:08', '62870341', '2023-06-07 17:25:42', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14503.64, 14520.96),
	(9743, 14453.3, 3.5, -41.59, 14462.2, NULL, NULL, '2023-06-08 20:20:50', '62973982', '2023-06-08 20:17:42', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14453.36, 14473.38),
	(9744, 14472.8, 3.5, -40.67, 14464.1, NULL, NULL, '2023-06-08 20:06:48', '62973223', '2023-06-08 20:02:20', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14471.41, 14440.42),
	(9745, 14640.5, 3.5, -46.66, 14630.5, NULL, NULL, '2023-06-09 16:56:09', '63031912', '2023-06-09 16:51:50', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14620.39),
	(9746, 14438.5, 3.5, 56.56, 14450.6, NULL, NULL, '2023-06-08 20:31:13', '62974575', '2023-06-08 20:27:03', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14449.44, 14425.11),
	(9747, 14350.5, 3.5, 2.8, 14349.9, NULL, NULL, '2023-06-08 16:57:52', '62954269', '2023-06-08 16:57:02', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14369.56),
	(9748, 14321.2, 3.5, 8.42, 14323, NULL, NULL, '2023-06-08 16:55:13', '62953791', '2023-06-08 16:54:49', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14313.02),
	(9749, 14575.3, 3.5, 41.57, 14584.2, NULL, NULL, '2023-06-07 17:14:05', '62867670', '2023-06-07 17:12:09', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14600.04, 14566.79),
	(9750, 14386.2, 3.5, -67.77, 14400.7, NULL, NULL, '2023-06-08 17:27:01', '62959020', '2023-06-08 17:26:16', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14401.44),
	(9751, 14445, 10, 108.16, 14453.1, NULL, NULL, '2023-06-08 20:21:57', '62974210', '2023-06-08 20:21:00', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14443.95),
	(9752, 14393.6, 3.5, -71.5, 14378.3, NULL, NULL, '2023-06-08 17:28:58', '62959145', '2023-06-08 17:27:12', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14396.74, 14376.74),
	(9753, 14639.3, 3.5, 0.47, 14639.4, NULL, NULL, '2023-06-09 16:56:06', '63032055', '2023-06-09 16:53:15', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14625.17),
	(9754, 14649.9, 3.5, 4.66, 14650.9, NULL, NULL, '2023-06-09 17:05:46', '63033640', '2023-06-09 17:02:49', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14649.72, 14629.21),
	(9755, 14631.5, 3.5, 92.8, 14651.4, NULL, NULL, '2023-06-09 17:09:11', '63034333', '2023-06-09 17:06:50', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14649.41, 14628.9),
	(9756, 14405.8, 3.5, 44.38, 14396.3, NULL, NULL, '2023-06-08 17:32:17', '62959401', '2023-06-08 17:29:00', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14390.53, 14410.7),
	(9757, 14396.1, 3.5, 31.32, 14402.8, NULL, NULL, '2023-06-08 17:36:41', '62960556', '2023-06-08 17:35:46', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14405.97, 14392.42),
	(9758, 14582, 3.5, -18.19, 14585.9, NULL, NULL, '2023-06-07 17:03:17', '62863648', '2023-06-07 17:02:25', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9759, 14628.3, 3.5, -55.79, 14640.2, NULL, NULL, '2023-06-07 16:49:03', '62861228', '2023-06-07 16:47:26', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14627.95, 14659.55),
	(9760, 14471, 3.5, 22.92, 14466.1, NULL, NULL, '2023-06-07 17:48:12', '62874490', '2023-06-07 17:46:13', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14453.47, 14488.6),
	(9761, 14444.7, 3.5, -2.8, 14444.1, NULL, NULL, '2023-06-08 20:29:31', '62974508', '2023-06-08 20:25:44', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14455.33, 14437.28),
	(9762, 14530.5, 3.5, 28.5, 14524.4, NULL, NULL, '2023-06-07 17:30:41', '62871301', '2023-06-07 17:30:15', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14515.94, 14536.05),
	(9763, 14489.5, 3.5, 15.91, 14486.1, NULL, NULL, '2023-06-07 17:39:34', '62873205', '2023-06-07 17:39:23', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9764, 14356, 3.5, 85.59, 14337.7, NULL, NULL, '2023-06-08 16:52:04', '62952419', '2023-06-08 16:47:45', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14329.84, 14370.98),
	(9765, 14595.5, 3.5, 46.74, 14605.5, NULL, NULL, '2023-06-12 16:51:42', '63123836', '2023-06-12 16:49:05', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14604.4, 14564.81),
	(9766, 14618.4, 3.5, 14.02, 14615.4, NULL, NULL, '2023-06-12 16:45:52', '63122727', '2023-06-12 16:45:11', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14604.72, 14626.51),
	(9767, 14576.8, 3.5, 74.36, 14592.7, NULL, NULL, '2023-06-12 17:06:29', '63128218', '2023-06-12 17:05:05', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14586.59, 14566.4),
	(9768, 14614.4, 3.5, -36.92, 14606.5, NULL, NULL, '2023-06-12 16:48:38', '63123596', '2023-06-12 16:48:19', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9769, 14617.6, 3.5, 101.36, 14595.9, NULL, NULL, '2023-06-12 16:34:07', '63117442', '2023-06-12 16:31:51', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14584.37, 14616.96),
	(9770, 14822.1, 15, 120.04, 14828.1, NULL, NULL, '2023-06-14 21:17:35', '63343471', '2023-06-14 21:17:01', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9771, 14923.7, 10, 124.87, 14914.3, NULL, NULL, '2023-06-14 17:04:08', '63317140', '2023-06-14 17:02:29', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14915.91, 14949.4),
	(9772, 14894.8, 8, 122.61, 14883.3, NULL, NULL, '2023-06-14 21:40:42', '63347477', '2023-06-14 21:40:12', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14896.58),
	(9773, 14913.4, 3.5, -46.49, 14923.4, NULL, NULL, '2023-06-14 17:02:24', '63316494', '2023-06-14 16:59:23', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14907.92, 14934.75),
	(9774, 14927.2, 7, 8.37, 14926.3, NULL, NULL, '2023-06-14 17:05:35', '63317386', '2023-06-14 17:04:40', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14927.33, 14939.31),
	(9775, 14972.4, 10, -55.79, 14976.6, NULL, NULL, '2023-06-14 17:53:58', '63324233', '2023-06-14 17:53:24', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14982.31),
	(9776, 14990.3, 3.5, 141.15, 14960, NULL, NULL, '2023-06-14 22:06:56', '63351696', '2023-06-14 22:05:17', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14967.05, 14992.74),
	(9777, 14966.4, 3.5, -9.3, 14968.4, NULL, NULL, '2023-06-14 17:22:09', '63319920', '2023-06-14 17:20:51', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14965.99, 14985.38),
	(9778, 14787.4, 8, -215.73, 14807.6, NULL, NULL, '2023-06-14 21:07:46', '63340680', '2023-06-14 21:07:15', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9779, 14927.8, 3.5, -65.09, 14913.8, NULL, NULL, '2023-06-14 16:53:22', '63314904', '2023-06-14 16:49:22', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14906.21),
	(9780, 14949.2, 3.5, -15.8, 14945.8, NULL, NULL, '2023-06-14 17:08:26', '63317865', '2023-06-14 17:07:29', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14939.12),
	(9781, 14952.4, 3.5, -4.18, 14953.3, NULL, NULL, '2023-06-14 17:16:42', '63318956', '2023-06-14 17:15:13', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14953.68),
	(9782, 14819.4, 2, -82.94, 14788.3, NULL, NULL, '2023-06-13 15:30:11', '63201508', '2023-06-13 15:30:03', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14753),
	(9783, 14884.1, 2.5, -10.99, 14887.4, NULL, NULL, '2023-06-14 21:01:52', '63337825', '2023-06-14 21:00:43', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9784, 14893.2, 3.5, 89.12, 14874.1, NULL, NULL, '2023-06-14 21:36:40', '63346461', '2023-06-14 21:35:59', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14909.82),
	(9785, 14920.5, 3.5, -35.8, 14912.8, NULL, NULL, '2023-06-14 16:49:00', '63314746', '2023-06-14 16:48:34', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14920.48, 14892.32),
	(9786, 14873.5, 3.5, 40.92, 14882.3, NULL, NULL, '2023-06-14 16:36:10', '63312214', '2023-06-14 16:35:34', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14871.4),
	(9787, 14961.2, 10, 49.14, 14957.5, NULL, NULL, '2023-06-14 17:31:14', '63321559', '2023-06-14 17:31:00', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9788, 14873, 3.5, -46.96, 14883.1, NULL, NULL, '2023-06-14 16:33:25', '63311085', '2023-06-14 16:31:15', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14873.49, 14901.26),
	(9789, 14886.9, 3.5, 134.86, 14858, NULL, NULL, '2023-06-14 21:36:27', '63346338', '2023-06-14 21:35:11', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14843.25, 14914.49),
	(9790, 14914.4, 3.5, 125.84, 14887.4, NULL, NULL, '2023-06-13 16:42:39', '63220110', '2023-06-13 16:40:45', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14888.03, 14913.36),
	(9791, 14884.6, 2.5, 98.23, 14855.1, NULL, NULL, '2023-06-14 21:01:53', '63338240', '2023-06-14 21:01:19', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9792, 14977.5, 3.5, -39.05, 14969.1, NULL, NULL, '2023-06-14 17:49:52', '63323417', '2023-06-14 17:45:22', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14976.83, 14961.15),
	(9793, 14953.4, 10, -107.56, 14961.5, NULL, NULL, '2023-06-14 17:24:43', '63320384', '2023-06-14 17:24:09', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9794, 14976.5, 3.5, -13.95, 14973.5, NULL, NULL, '2023-06-14 17:49:42', '63323014', '2023-06-14 17:42:58', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14976.59, 14961.15),
	(9795, 14945.6, 3.5, 46.01, 14935.7, NULL, NULL, '2023-06-14 17:10:03', '63318063', '2023-06-14 17:09:02', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14934.37, 14952.25),
	(9796, 14840.7, 10, -128.01, 14850.3, NULL, NULL, '2023-06-14 21:16:09', '63343167', '2023-06-14 21:15:39', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9797, 14909.7, 3.5, 42.41, 14900.6, NULL, NULL, '2023-06-13 16:42:37', '63220435', '2023-06-13 16:41:53', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14909.22),
	(9798, 14904.6, 8, 37.31, 14908.1, NULL, NULL, '2023-06-14 21:39:10', '63347204', '2023-06-14 21:38:46', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9799, 14842.6, 7, -261.54, 14870.6, NULL, NULL, '2023-06-14 21:34:45', '63345180', '2023-06-14 21:33:30', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14913.32),
	(9800, 14911.5, 10, 81.05, 14917.6, NULL, NULL, '2023-06-14 16:57:48', '63316127', '2023-06-14 16:56:41', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14912.87),
	(9801, 14887.7, 3.5, 139.41, 14857.8, NULL, NULL, '2023-06-13 15:30:28', '63202948', '2023-06-13 15:30:18', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9802, 14873.2, 3.5, 13.48, 14876.1, NULL, NULL, '2023-06-14 16:39:51', '63312956', '2023-06-14 16:39:14', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14866.07),
	(9803, 14902.3, 3.5, -7.91, 14904, NULL, NULL, '2023-06-14 16:43:00', '63313602', '2023-06-14 16:42:32', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14915.72),
	(9804, 14961.6, 3.5, 106.67, 14938.7, NULL, NULL, '2023-06-14 22:02:25', '63350837', '2023-06-14 21:59:30', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14937.07, 14962.37),
	(9805, 14837.4, 5, -116.65, 14854.9, NULL, NULL, '2023-06-14 21:14:40', '63341935', '2023-06-14 21:10:44', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14866.72),
	(9806, 14913.2, 3.5, -73.45, 14929, NULL, NULL, '2023-06-14 17:02:23', '63316716', '2023-06-14 17:00:07', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14937.41),
	(9807, 14938.3, 7, 53.92, 14932.5, NULL, NULL, '2023-06-14 17:06:04', '63317609', '2023-06-14 17:05:45', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14933.61, 14944.64),
	(9808, 14914.7, 10, 38.53, 14911.8, NULL, NULL, '2023-06-14 16:55:12', '63315874', '2023-06-14 16:55:04', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9809, 14840.9, 2.5, -66.96, 14861, NULL, NULL, '2023-06-14 21:03:49', '63338699', '2023-06-14 21:03:08', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14843.2, 14899.5),
	(9810, 14861.7, 3.5, 135.74, 14890.8, NULL, NULL, '2023-06-13 15:34:31', '63204055', '2023-06-13 15:31:49', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14887.21, 14817.13),
	(9811, 14978.2, 10, 70.39, 14972.9, NULL, NULL, '2023-06-14 17:41:47', '63322679', '2023-06-14 17:40:36', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14966.63, 14986.79),
	(9812, 14887.6, 3.5, -31.63, 14894.4, NULL, NULL, '2023-06-14 16:30:53', '63310809', '2023-06-14 16:30:16', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14888.23, 14907.92),
	(9813, 14974.7, 3.5, -11.62, 14972.2, NULL, NULL, '2023-06-14 17:53:19', '63324213', '2023-06-14 17:53:04', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9814, 14899.2, 3.5, 20.53, 14894.8, NULL, NULL, '2023-06-13 15:59:09', '63210482', '2023-06-13 15:57:06', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14884.46, 14907.29),
	(9815, 14949.6, 3.5, 6.99, 14951.1, NULL, NULL, '2023-06-14 21:55:55', '63350228', '2023-06-14 21:54:34', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14949.53, 14936.68),
	(9816, 14980.3, 3.5, 22.36, 14975.5, NULL, NULL, '2023-06-14 22:05:50', '63351436', '2023-06-14 22:05:29', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14951, 14999),
	(9817, 14829.8, 10, 54.7, 14825.7, NULL, NULL, '2023-06-14 21:27:12', '63344981', '2023-06-14 21:26:47', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14834.03),
	(9818, 14941.7, 3.5, 6.97, 14943.2, NULL, NULL, '2023-06-14 17:12:46', '63318452', '2023-06-14 17:11:43', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14951.3, 14935.89),
	(9819, 14961.6, 3.5, 157.93, 14927.7, NULL, NULL, '2023-06-14 21:50:10', '63348787', '2023-06-14 21:46:49', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14956.53, 14978.34),
	(9820, 14968.4, 5, 23.24, 14964.9, NULL, NULL, '2023-06-14 17:40:15', '63322195', '2023-06-14 17:36:42', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14964.14, 14975.84),
	(9821, 14936.8, 3.5, -77.85, 14953.5, NULL, NULL, '2023-06-14 21:58:10', '63350428', '2023-06-14 21:56:03', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14937.85, 15009.48),
	(9822, 14897.3, 3.5, -70.4, 14882.2, NULL, NULL, '2023-06-13 15:31:38', '63203746', '2023-06-13 15:31:14', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9823, 14897.4, 7, 107.32, 14885.9, NULL, NULL, '2023-06-13 16:33:05', '63217395', '2023-06-13 16:32:12', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14883.88, 14905.32),
	(9824, 14829.8, 10, 93.37, 14822.8, NULL, NULL, '2023-06-14 21:21:27', '63344062', '2023-06-14 21:20:20', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14840.23),
	(9825, 14954.7, 10, 241.75, 14972.9, NULL, NULL, '2023-06-14 17:55:54', '63324291', '2023-06-14 17:54:12', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14966.88, 14951.94),
	(9826, 14824.3, 2, -96.58, 14860.5, NULL, NULL, '2023-06-13 15:30:10', '63201495', '2023-06-13 15:30:00', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14906.53),
	(9827, 14927.8, 3.5, -33.01, 14920.7, NULL, NULL, '2023-06-14 16:53:23', '63314974', '2023-06-14 16:49:54', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14908.11),
	(9828, 14916.5, 10, 29.22, 14918.7, NULL, NULL, '2023-06-14 16:54:24', '63315622', '2023-06-14 16:53:45', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14906.59),
	(9829, 14886.4, 3.5, -4.18, 14887.3, NULL, NULL, '2023-06-14 16:35:18', '63312078', '2023-06-14 16:35:06', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9830, 14957.6, 10, -118.2, 14966.5, NULL, NULL, '2023-06-14 17:23:54', '63320205', '2023-06-14 17:23:18', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14969.62),
	(9831, 14908.3, 5, -47.97, 14915.5, NULL, NULL, '2023-06-14 21:42:37', '63347727', '2023-06-14 21:41:31', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14916.44),
	(9832, 14914.2, 3.5, -45.58, 14904.4, NULL, NULL, '2023-06-14 16:46:52', '63314296', '2023-06-14 16:46:03', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14914.39, 14897.84),
	(9833, 14899.6, 3.5, -16.8, 14903.2, NULL, NULL, '2023-06-13 15:59:08', '63208881', '2023-06-13 15:47:19', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14885.23, 14908.05),
	(9834, 14911.3, 3.5, 12.09, 14908.7, NULL, NULL, '2023-06-14 16:45:42', '63314097', '2023-06-14 16:45:08', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14901.26, 14924.28),
	(9835, 14968.7, 5, 2.66, 14968.3, NULL, NULL, '2023-06-14 17:40:16', '63322424', '2023-06-14 17:38:50', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14975.09),
	(9836, 14800.2, 7, -184.94, 14820, NULL, NULL, '2023-06-14 21:25:45', '63344539', '2023-06-14 21:23:27', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14799.96, 14831.97),
	(9837, 14953.6, 10, 96.93, 14946.3, NULL, NULL, '2023-06-14 17:27:51', '63320802', '2023-06-14 17:25:43', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14965.63),
	(9838, 14961.9, 3.5, 35.4, 14954.3, NULL, NULL, '2023-06-14 22:02:26', '63351283', '2023-06-14 22:01:51', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14963.54),
	(9839, 14886.2, 3.5, -102.2, 14908.1, NULL, NULL, '2023-06-13 16:31:33', '63216818', '2023-06-13 16:30:10', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14918.47),
	(9840, 14878.8, 2, -52.55, 14859.1, NULL, NULL, '2023-06-13 15:42:04', '63206581', '2023-06-13 15:38:17', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14878.37, 14845.77),
	(9841, 14835.3, 2.5, -54, 14851.5, NULL, NULL, '2023-06-14 21:05:43', '63339394', '2023-06-14 21:03:57', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14835.99, 14894.7),
	(9842, 14958.4, 5, -45.16, 14965.2, NULL, NULL, '2023-06-14 17:36:23', '63322069', '2023-06-14 17:35:27', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14974.59),
	(9843, 14852.4, 3.5, 124.94, 14879.2, NULL, NULL, '2023-06-13 16:46:47', '63221624', '2023-06-13 16:45:22', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14870.49, 14853.44),
	(9844, 14943.8, 3.5, -20.91, 14948.3, NULL, NULL, '2023-06-14 17:14:15', '63318684', '2023-06-14 17:13:17', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14935.7, 14962.71),
	(9845, 14921.2, 3.5, 32.64, 14914.2, NULL, NULL, '2023-06-13 16:00:34', '63210991', '2023-06-13 15:59:57', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14901.96, 14923.26),
	(9846, 14952.2, 3.5, 24.16, 14947, NULL, NULL, '2023-06-14 17:16:43', '63318840', '2023-06-14 17:14:40', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14944.64, 14953.25),
	(9847, 14922.8, 3.5, -20.93, 14918.3, NULL, NULL, '2023-06-14 16:59:22', '63316463', '2023-06-14 16:59:07', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14925.62, 14911.73),
	(9848, 14882.7, 3.5, 52.25, 14893.9, NULL, NULL, '2023-06-13 16:37:12', '63218678', '2023-06-13 16:36:30', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14880.96),
	(9849, 14816.5, 2.5, -70.36, 14837.6, NULL, NULL, '2023-06-14 21:06:52', '63340230', '2023-06-14 21:06:17', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14817.4, 14871.65),
	(9850, 14942.2, 3.5, 7.44, 14943.8, NULL, NULL, '2023-06-14 17:12:47', '63318525', '2023-06-14 17:12:14', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9851, 14918.5, 5, -89.27, 14905.1, NULL, NULL, '2023-06-14 21:43:59', '63348072', '2023-06-14 21:43:18', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14919.55, 14882.96),
	(9852, 14837.4, 2.5, -93.33, 14865.4, NULL, NULL, '2023-06-14 21:05:39', '63339522', '2023-06-14 21:04:15', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14838.74, 14884.05),
	(9853, 14908.1, 3.5, -39.53, 14899.6, NULL, NULL, '2023-06-14 16:45:06', '63313794', '2023-06-14 16:44:18', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14908.11, 14890.61),
	(9854, 14915.7, 3.5, -31.62, 14908.9, NULL, NULL, '2023-06-14 16:47:10', '63314244', '2023-06-14 16:45:50', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14917.82, 14893.66),
	(9855, 14808.9, 8, 209.24, 14789.3, NULL, NULL, '2023-06-14 21:09:01', '63340906', '2023-06-14 21:07:54', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14830.03),
	(9856, 14894.2, 3.5, 2.8, 14894.8, NULL, NULL, '2023-06-13 15:45:17', '63208275', '2023-06-13 15:44:42', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 14907.01, 0),
	(9857, 14900.8, 2.5, 16.3, 14905.7, NULL, NULL, '2023-06-14 21:00:07', '63337061', '2023-06-14 21:00:02', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14876.04),
	(9858, 14929.6, 3.5, -50.35, 14940.4, NULL, NULL, '2023-06-15 16:33:07', '63424284', '2023-06-15 16:32:52', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14929.77, 14956.13),
	(9859, 14996.3, 3.5, 76.28, 15012.7, NULL, NULL, '2023-06-15 16:49:07', '63427561', '2023-06-15 16:48:47', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 15013.92, 14997.6),
	(9860, 14934.4, 3.5, 39.61, 14925.9, NULL, NULL, '2023-06-15 16:34:59', '63424475', '2023-06-15 16:33:29', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14915.73, 14940.77),
	(9861, 14950, 3.5, 56.35, 14937.9, NULL, NULL, '2023-06-15 16:39:15', '63425867', '2023-06-15 16:38:55', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14920.41, 14948.03),
	(9862, 14969.2, 3.5, 34.45, 14961.8, NULL, NULL, '2023-06-15 16:41:31', '63426317', '2023-06-15 16:40:50', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14957.26, 14975.92),
	(9863, 14922.2, 3.5, -46.59, 14932.2, NULL, NULL, '2023-06-15 16:38:07', '63425237', '2023-06-15 16:36:31', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14916.43, 14947.09),
	(9864, 14952.2, 3.5, 91.74, 14932.5, NULL, NULL, '2023-06-15 16:39:15', '63425820', '2023-06-15 16:38:37', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 14919, 14951.31),
	(9865, 14931.5, 3.5, -39.61, 14923, NULL, NULL, '2023-06-15 16:38:31', '63425689', '2023-06-15 16:38:09', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 14893.95),
	(9866, 15205.1, 3.5, 36.06, 15197.3, NULL, NULL, '2023-06-16 17:24:25', '63532194', '2023-06-16 17:22:43', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15195.9, 15205.06),
	(9867, 15191.5, 3.5, -16.18, 15195, NULL, NULL, '2023-06-16 17:18:59', '63531386', '2023-06-16 17:18:06', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 15210.81),
	(9868, 15181.9, 3.5, 21.27, 15186.5, NULL, NULL, '2023-06-16 17:20:18', '63531585', '2023-06-16 17:19:20', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 15172.06),
	(9869, 15158.4, 3.5, -60.56, 15145.3, NULL, NULL, '2023-06-16 16:56:08', '63525783', '2023-06-16 16:54:50', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 15157.15, 15128.41),
	(9870, 15178, 3.5, -43.01, 15187.3, NULL, NULL, '2023-06-16 17:20:42', '63531777', '2023-06-16 17:20:30', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15178.02, 15199.31),
	(9871, 15177.9, 8, 94, 15186.8, NULL, NULL, '2023-06-16 17:03:58', '63527821', '2023-06-16 17:03:32', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9872, 15194.5, 8, 72.92, 15187.6, NULL, NULL, '2023-06-16 17:22:20', '63531915', '2023-06-16 17:21:01', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15189.3, 15211.02),
	(9873, 15196.7, 3.5, 1.39, 15197, NULL, NULL, '2023-06-16 16:42:00', '63523183', '2023-06-16 16:41:42', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9874, 15177.3, 3.5, 3.7, 15178.1, NULL, NULL, '2023-06-16 17:15:38', '63530904', '2023-06-16 17:15:22', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 15176.95, 0),
	(9875, 15174.8, 3.5, -66.59, 15189.2, NULL, NULL, '2023-06-16 16:43:21', '63523366', '2023-06-16 16:42:45', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15174.83, 15205.7),
	(9876, 15161, 3.5, -42.54, 15170.2, NULL, NULL, '2023-06-16 16:43:52', '63523520', '2023-06-16 16:43:34', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9877, 15168.3, 3.5, 49.02, 15157.7, NULL, NULL, '2023-06-16 16:50:03', '63524455', '2023-06-16 16:47:30', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15159.71, 15174.83),
	(9878, 15192.6, 3.5, 52.67, 15181.2, NULL, NULL, '2023-06-16 17:09:29', '63529127', '2023-06-16 17:08:07', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15182.28, 15199.1),
	(9879, 15185.7, 3.5, 0.46, 15185.6, NULL, NULL, '2023-06-16 17:00:29', '63526674', '2023-06-16 17:00:05', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9880, 15205.1, 3.5, 3.7, 15204.3, NULL, NULL, '2023-06-16 17:24:25', '63532283', '2023-06-16 17:23:15', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15195.69, 15204.85),
	(9881, 15182.5, 3.5, -45.3, 15192.3, NULL, NULL, '2023-06-16 17:17:37', '63531170', '2023-06-16 17:16:41', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 15203.57),
	(9882, 15172.7, 3.5, 11.55, 15175.2, NULL, NULL, '2023-06-16 17:07:49', '63528821', '2023-06-16 17:06:59', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 15184.62, 15153.96),
	(9883, 15182, 3.5, 38.36, 15190.3, NULL, NULL, '2023-06-16 17:14:38', '63530677', '2023-06-16 17:14:19', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9884, 15182.7, 3.5, -76.73, 15166.1, NULL, NULL, '2023-06-16 17:00:00', '63526503', '2023-06-16 16:59:43', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 15174.4, 15154.6),
	(9885, 15157, 3.5, -49.48, 15167.7, NULL, NULL, '2023-06-16 16:45:38', '63523833', '2023-06-16 16:45:05', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15158.01, 15167.8),
	(9886, 15167.8, 3.5, 16.65, 15164.2, NULL, NULL, '2023-06-16 16:49:20', '63524567', '2023-06-16 16:47:54', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15152.68, 15178.23),
	(9887, 15182.9, 3.5, -66.54, 15168.5, NULL, NULL, '2023-06-16 17:03:14', '63527550', '2023-06-16 17:02:34', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9888, 15190.8, 3.5, 10.63, 15188.5, NULL, NULL, '2023-06-16 17:09:15', '63529279', '2023-06-16 17:08:56', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 0, 0),
	(9889, 15195.8, 3.5, 14.32, 15192.7, NULL, NULL, '2023-06-16 17:10:44', '63529697', '2023-06-16 17:10:21', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15184.62, 15208.46),
	(9890, 15196, 3.5, 19.42, 15191.8, NULL, NULL, '2023-06-16 16:38:12', '63522146', '2023-06-16 16:37:19', 0, NULL, 'us100.cash', b'1', b'1', 5, 1, 15182.92, 15203.14),
	(9891, 15157.7, 3.5, 99.84, 15179.3, NULL, NULL, '2023-06-16 16:53:33', '63525334', '2023-06-16 16:52:30', 1, NULL, 'us100.cash', b'1', b'1', 5, 1, 15168.23, 15127.77);
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
) ENGINE=InnoDB AUTO_INCREMENT=6245 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trade_records: ~12 rows (approximately)
/*!40000 ALTER TABLE `trade_records` DISABLE KEYS */;
REPLACE INTO `trade_records` (`id`, `aggregate_interval`, `balance`, `end_date`, `start_date`, `account_id`, `statistics_id`, `target`) VALUES
	(6233, 3, 16295.83, '2024-01-01', '2023-01-01', 5, 6491, 203.7),
	(6234, 2, 16295.83, '2023-07-01', '2023-06-01', 5, 6492, 203.7),
	(6235, 1, 15416.83, '2023-06-12', '2023-06-05', 5, 6493, 192.71),
	(6236, 0, 15103.28, '2023-06-08', '2023-06-07', 5, 6494, 188.79),
	(6237, 0, 15227.87, '2023-06-09', '2023-06-08', 5, 6495, 190.35),
	(6238, 0, 15416.83, '2023-06-10', '2023-06-09', 5, 6496, 192.71),
	(6239, 1, 16295.83, '2023-06-19', '2023-06-12', 5, 6497, 203.7),
	(6240, 0, 15616.39, '2023-06-13', '2023-06-12', 5, 6498, 195.2),
	(6241, 0, 15978.8, '2023-06-14', '2023-06-13', 5, 6499, 199.74),
	(6242, 0, 16054.92, '2023-06-15', '2023-06-14', 5, 6500, 200.69),
	(6243, 0, 16216.8, '2023-06-16', '2023-06-15', 5, 6501, 202.71),
	(6244, 0, 16295.83, '2023-06-17', '2023-06-16', 5, 6502, 203.7);
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
) ENGINE=InnoDB AUTO_INCREMENT=6503 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trade_records_statistics: ~12 rows (approximately)
/*!40000 ALTER TABLE `trade_records_statistics` DISABLE KEYS */;
REPLACE INTO `trade_records_statistics` (`id`, `average_loss_amount`, `average_loss_size`, `average_win_amount`, `average_win_size`, `largest_loss_amount`, `largest_loss_size`, `largest_win_amount`, `largest_win_size`, `net_profit`, `number_of_losing_trades`, `number_of_trades`, `number_of_winning_trades`, `percentage_profit`, `trading_rate`, `win_percentage`, `pips_earned`, `pips_lost`) VALUES
	(6491, -57.4, 3.99, 54.22, 4.7, -261.54, 7, 241.75, 10, 1295.83, 70, 168, 98, 7.95, 21, 58, 908.2, 774.9),
	(6492, -57.4, 3.99, 54.22, 4.7, -261.54, 7, 241.75, 10, 1295.83, 70, 168, 98, 7.95, 21, 58, 908.2, 774.9),
	(6493, -43.94, 3.5, 38.22, 3.74, -79.92, 3.5, 108.16, 10, 416.83, 14, 41, 27, 2.7, 13.67, 66, 205.8, 131.6),
	(6494, -45.84, 3.5, 36.94, 3.5, -79.92, 3.5, 69.6, 3.5, 103.28, 5, 14, 9, 0.68, 14, 64, 71.1, 49),
	(6495, -42.42, 3.5, 38.66, 4.04, -71.5, 3.5, 108.16, 10, 124.59, 8, 20, 12, 0.82, 20, 60, 84.2, 72.6),
	(6496, -46.66, 3.5, 39.27, 3.5, -46.66, 3.5, 92.8, 3.5, 188.96, 1, 7, 6, 1.23, 7, 86, 50.5, 10),
	(6497, -60.78, 4.1, 60.31, 5.05, -261.54, 7, 241.75, 10, 879, 56, 127, 71, 5.39, 25.4, 56, 702.4, 643.3),
	(6498, -36.92, 3.5, 59.12, 3.5, -36.92, 3.5, 101.36, 3.5, 199.56, 1, 5, 4, 1.28, 5, 80, 50.6, 7.9),
	(6499, -70.24, 2.75, 78.39, 3.85, -102.2, 3.5, 139.41, 3.5, 362.41, 6, 16, 10, 2.27, 16, 62, 156.6, 127.6),
	(6500, -63.28, 4.54, 69.07, 6.3, -261.54, 7, 241.75, 10, 76.12, 37, 72, 35, 0.47, 72, 49, 333.3, 377.5),
	(6501, -45.52, 3.5, 59.69, 3.5, -50.35, 3.5, 91.74, 3.5, 161.88, 3, 8, 5, 1, 8, 62, 64.1, 29.3),
	(6502, -51.88, 3.5, 32.12, 4.03, -76.73, 3.5, 99.84, 3.5, 79.03, 9, 26, 17, 0.48, 26, 65, 97.8, 101);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trading_plans: ~1 rows (approximately)
/*!40000 ALTER TABLE `trading_plans` DISABLE KEYS */;
REPLACE INTO `trading_plans` (`id`, `active`, `compound_frequency`, `end_date`, `name`, `profit_target`, `start_date`, `starting_balance`, `status`, `deposit_plan_id`, `withdrawal_plan_id`, `account_id`, `absolute`, `aggregate_interval`) VALUES
	(5, b'1', 1, '2023-06-09', 'FTMO Challenge Trading Plan', 1.25, '2023-09-09', 15000, 1, NULL, NULL, 5, b'0', 0);
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
	(5, 's.prizio@hotmail.com', 'Stephen', 'Prizio', '$2a$10$/.HyJQA84yfceXoYgPO9euce1PmQNfisSI6GA7G52yHUBBxEBBHo6', 's.prizio', 5, 1, 1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.user_locales
CREATE TABLE IF NOT EXISTS `user_locales` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `country` int(11) DEFAULT NULL,
  `time_zone_offset` varchar(50) DEFAULT NULL,
  `town_city` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.user_locales: ~1 rows (approximately)
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

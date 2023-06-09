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
REPLACE INTO `account` (`id`, `account_id`, `account_open_time`, `active`, `balance`, `trading_plan_id`, `user_id`, `skills_id`, `rank_id`, `name`, `account_number`, `currency`, `account_type`, `broker`, `daily_stop_limit`, `daily_stop_limit_type`, `default_account`) VALUES
	(5, '5', '2023-06-05 14:56:49.000000', b'1', 15000, NULL, 5, 5, 60, 'FTMO Challenge', 1300451678, 5, 2, 1, 55, 1, b'1');
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

-- Dumping structure for table trader_buddy.audio_retrospectives
CREATE TABLE IF NOT EXISTS `audio_retrospectives` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `interval_frequency` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqcli56nbow807g5mkleyxkojb` (`account_id`),
  CONSTRAINT `FKqcli56nbow807g5mkleyxkojb` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.audio_retrospectives: ~0 rows (approximately)
/*!40000 ALTER TABLE `audio_retrospectives` DISABLE KEYS */;
REPLACE INTO `audio_retrospectives` (`id`, `end_date`, `interval_frequency`, `name`, `start_date`, `url`, `account_id`) VALUES
	(1, '2023-02-28', 2, 'February Retrospective', '2023-02-01', '\\audio\\temp.wav', 5);
/*!40000 ALTER TABLE `audio_retrospectives` ENABLE KEYS */;

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

-- Dumping data for table trader_buddy.deposit_plans: ~3 rows (approximately)
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
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news: ~11 rows (approximately)
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
	(92, '2023-06-09');
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
) ENGINE=InnoDB AUTO_INCREMENT=566 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news_entries: ~183 rows (approximately)
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
	(565, 'Italian Industrial Production m/m', 3, 548, 2, '0.2%', '-0.6%');
/*!40000 ALTER TABLE `market_news_entries` ENABLE KEYS */;

-- Dumping structure for table trader_buddy.market_news_slots
CREATE TABLE IF NOT EXISTS `market_news_slots` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` time DEFAULT NULL,
  `news_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgx55cd7fhbmhyfm2jig419vpp` (`news_id`),
  CONSTRAINT `FKgx55cd7fhbmhyfm2jig419vpp` FOREIGN KEY (`news_id`) REFERENCES `market_news` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=549 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.market_news_slots: ~135 rows (approximately)
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
	(548, '04:00:00', 92);
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueIntervalAndStartDateAndEndDate` (`interval_frequency`,`start_date`,`end_date`),
  KEY `FK45ox2nc7as2dui59tb1uyfgqb` (`account_id`),
  CONSTRAINT `FK45ox2nc7as2dui59tb1uyfgqb` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=276 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.skills: ~1 rows (approximately)
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
REPLACE INTO `skills` (`id`, `delta`, `last_updated`, `level`, `points`, `step_increment`, `remaining`, `previous_level`, `previous_points`) VALUES
	(5, 0, '2023-06-05 14:58:14.000000', 1, 0, 100, 100, NULL, NULL);
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueTradeIdAndAccount` (`trade_id`,`account_id`),
  KEY `FKhrx1ya4wn13vvty5h2t85970t` (`account_id`),
  CONSTRAINT `FKhrx1ya4wn13vvty5h2t85970t` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9724 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trades: ~0 rows (approximately)
/*!40000 ALTER TABLE `trades` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6233 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trade_records: ~0 rows (approximately)
/*!40000 ALTER TABLE `trade_records` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6491 DEFAULT CHARSET=utf8;

-- Dumping data for table trader_buddy.trade_records_statistics: ~0 rows (approximately)
/*!40000 ALTER TABLE `trade_records_statistics` DISABLE KEYS */;
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

-- Dumping data for table trader_buddy.users: ~0 rows (approximately)
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

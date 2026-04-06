-- =============================================================================
-- NIPC School Management — import into phpMyAdmin
-- XAMPP MySQL on port 3307: use Server connection tab with port 3307, or CLI:
--   mysql -u root -P 3307 -h 127.0.0.1 < nipc_school.sql
-- =============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `nipc_school` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `nipc_school`;

DROP TABLE IF EXISTS `scores`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `school_info`;

CREATE TABLE `users` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` ENUM('admin','user') NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `students` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `student_id` VARCHAR(20) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `gender` ENUM('male','female') NOT NULL,
    `date_of_birth` DATE NULL,
    `class` VARCHAR(50) NULL,
    `phone` VARCHAR(20) NULL,
    `address` TEXT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `scores` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `student_id` VARCHAR(20) NOT NULL,
    `subject` VARCHAR(100) NOT NULL,
    `score` DECIMAL(5,2) NOT NULL,
    `semester` VARCHAR(20) NULL,
    `year` VARCHAR(10) NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_scores_student` (`student_id`),
    CONSTRAINT `fk_scores_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `school_info` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `school_name` VARCHAR(200) NULL,
    `address` TEXT NULL,
    `phone` VARCHAR(20) NULL,
    `email` VARCHAR(100) NULL,
    `principal` VARCHAR(100) NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Passwords: MD5('admin123'), MD5('user123') — matches PHP api/login.php
INSERT INTO `users` (`username`, `password`, `role`) VALUES
('admin', MD5('admin123'), 'admin'),
('user1', MD5('user123'), 'user');

INSERT INTO `school_info` (`id`, `school_name`, `address`, `phone`, `email`, `principal`) VALUES
(1, 'NIPC Institute', 'Phnom Penh, Cambodia', '012345678', 'nipc@school.com', 'Mr. Sambath');

SET FOREIGN_KEY_CHECKS = 1;

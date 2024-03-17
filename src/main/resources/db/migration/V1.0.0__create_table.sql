CREATE DATABASE IF NOT EXISTS `book-management`;

USE `book-management`;

CREATE TABLE IF NOT EXISTS Author (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) CHARACTER SET utf8mb4;

CREATE TABLE IF NOT EXISTS Book (
    id bigint NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author_id bigint NOT NULL,
    published_at date NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (author_id) REFERENCES Author(id),
    INDEX index_published_at (published_at)
) CHARACTER SET utf8mb4;


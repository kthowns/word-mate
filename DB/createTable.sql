CREATE DATABASE myvoca;
CREATE USER 'myvoca'@'%' identified by 'password';
GRANT ALL PRIVILEGES ON *.* TO 'myvoca'@'%';
USE myvoca;

CREATE TABLE users
(
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(32)        NOT NULL
);

CREATE TABLE vocabs
(
    vocab_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT         NOT NULL,
    title       VARCHAR(16) NOT NULL UNIQUE,
    description VARCHAR(32),
    word_count  INT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE words
(
    word_id    INT AUTO_INCREMENT PRIMARY KEY,
    vocab_id   INT         NOT NULL,
    expression VARCHAR(32) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vocab_id) REFERENCES vocabs (vocab_id) ON DELETE CASCADE
);

CREATE TABLE word_stats
(
    word_id         INT PRIMARY KEY,
    correct_count   INT     DEFAULT 0,
    incorrect_count INT     DEFAULT 0,
    is_learned      TINYINT DEFAULT 0,
    FOREIGN KEY (word_id) REFERENCES words (word_id) ON DELETE CASCADE
);

CREATE TABLE definition
(
    definition_id INT AUTO_INCREMENT PRIMARY KEY,
    word_id       INT         NOT NULL,
    definition    VARCHAR(64) NOT NULL,
    type          VARCHAR(32) NOT NULL,
    FOREIGN KEY (word_id) REFERENCES words (word_id) ON DELETE CASCADE
);

CREATE TABLE theme
(
    theme_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id   INT         NOT NULL,
    font      VARCHAR(32) NOT NULL,
    font_size INT         NOT NULL,
    color     CHAR(7)     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
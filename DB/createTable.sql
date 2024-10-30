CREATE TABLE vocab
(
    vocab_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT         NOT NULL,
    title       VARCHAR(16) NOT NULL UNIQUE,
    description VARCHAR(32),
    word_count  INT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
);

CREATE TABLE word
(
    word_id    INT AUTO_INCREMENT PRIMARY KEY,
    expression VARCHAR(32) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vocab_word
(
    vocab_id INT NOT NULL,
    word_id  INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (vocab_id, word_id),
    FOREIGN KEY (vocab_id) REFERENCES vocab (vocab_id) ON DELETE CASCADE,
    FOREIGN KEY (word_id) REFERENCES word (word_id) ON DELETE CASCADE
);

CREATE TABLE word_stastics
(
    word_id         INT PRIMARY KEY,
    correct_count   INT     DEFAULT 0,
    incorrect_count INT     DEFAULT 0,
    isLearned       TINYINT DEFAULT 0,
    FOREIGN KEY (word_id) REFERENCES word (word_id) ON DELETE CASCADE
);

CREATE TABLE definition
(
    definition_id INT AUTO_INCREMENT PRIMARY KEY,
    definition    VARCHAR(64) NOT NULL,
    type          VARCHAR(32) NOT NULL
);

CREATE TABLE word_definition
(
    word_definition_id INT AUTO_INCREMENT PRIMARY KEY,
    word_id            INT NOT NULL,
    definition_id      INT NOT NULL,
    FOREIGN KEY (word_id) REFERENCES word (word_id) ON DELETE CASCADE,
    FOREIGN KEY (definition_id) REFERENCES definition (definition_id) ON DELETE CASCADE
);

CREATE TABLE theme
(
    theme_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id   INT         NOT NULL,
    font      VARCHAR(32) NOT NULL,
    font_size INT         NOT NULL,
    color     CHAR(7)     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
);

CREATE TABLE user
(
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(32)        NOT NULL
);

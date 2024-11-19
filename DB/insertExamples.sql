show tables;
describe users;
describe vocabs;
describe words;
describe definition;
describe word_stats;
describe theme;

INSERT INTO users (username, password)
VALUES ('user1', 'password1'),
       ('user2', 'password2'),
       ('user3', 'password3'),
       ('user4', 'password4'),
       ('user5', 'password5'),
       ('user6', 'password6'),
       ('user7', 'password7'),
       ('user8', 'password8'),
       ('user9', 'password9'),
       ('user10', 'password10');

INSERT INTO vocabs (user_id, title, description, word_count)
VALUES (1, '어휘 세트 1', '첫 번째 어휘 세트', 3),
       (2, '어휘 세트 2', '두 번째 어휘 세트', 2),
       (3, '어휘 세트 3', '세 번째 어휘 세트', 2),
       (4, '어휘 세트 4', '네 번째 어휘 세트', 2),
       (5, '어휘 세트 5', '다섯 번째 어휘 세트', 1),
       (6, '어휘 세트 6', '여섯 번째 어휘 세트', 0),
       (7, '어휘 세트 7', '일곱 번째 어휘 세트', 0),
       (8, '어휘 세트 8', '여덟 번째 어휘 세트', 0),
       (9, '어휘 세트 9', '아홉 번째 어휘 세트', 0),
       (10, '어휘 세트 10', '열 번째 어휘 세트', 0);

INSERT INTO words (vocab_id, expression)
VALUES (1, 'apple'),
       (1, 'run'),
       (1, 'happy'),
       (2, 'quickly'),
       (2, 'beautiful'),
       (3, 'banana'),
       (3, 'jump'),
       (4, 'sad'),
       (4, 'slowly'),
       (5, 'orange');

INSERT INTO definition (word_id, definition, type)
VALUES (1, '사과', 'NOUN'),
       (2, '달리다', 'VERB'),
       (3, '행복한', 'ADJECTIVE'),
       (4, '빠르게', 'ADVERB'),
       (5, '아름다운', 'ADJECTIVE'),
       (6, '바나나', 'NOUN'),
       (7, '점프하다', 'VERB'),
       (8, '슬픈', 'ADJECTIVE'),
       (9, '천천히', 'ADVERB'),
       (10, '오렌지', 'NOUN');

INSERT INTO word_stats (word_id, correct_count, incorrect_count, is_learned)
VALUES (1, 10, 2, 1),
       (2, 5, 3, 1),
       (3, 8, 1, 1),
       (4, 7, 4, 0),
       (5, 6, 1, 1),
       (6, 9, 0, 1),
       (7, 4, 2, 0),
       (8, 3, 5, 0),
       (9, 1, 1, 0),
       (10, 8, 2, 1);

INSERT INTO theme (user_id, font, font_size, color)
VALUES (1, 'Arial', 12, '#000000'),
       (2, 'Times New Roman', 14, '#FF5733'),
       (3, 'Verdana', 16, '#33FF57'),
       (4, 'Courier New', 18, '#3357FF'),
       (5, 'Georgia', 20, '#FF33A1'),
       (6, 'Helvetica', 22, '#F1C40F'),
       (7, 'Comic Sans MS', 10, '#9B59B6'),
       (8, 'Trebuchet MS', 24, '#2ECC71'),
       (9, 'Impact', 26, '#E74C3C'),
       (10, 'Lucida Console', 28, '#3498DB');

select * from users;
select * from vocabs;
select * from words;
select * from definition;
select * from word_stats;
select * from theme;
show tables;
describe user;
describe vocab;
describe word;
describe vocab_word;
describe definition;
describe word_definition;
describe word_stastics;
describe theme;

INSERT INTO user (username, password)
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

INSERT INTO vocab (user_id, title, description, word_count)
VALUES (1, '어휘 세트 1', '첫 번째 어휘 세트', 5),
       (2, '어휘 세트 2', '두 번째 어휘 세트', 7),
       (3, '어휘 세트 3', '세 번째 어휘 세트', 10),
       (4, '어휘 세트 4', '네 번째 어휘 세트', 3),
       (5, '어휘 세트 5', '다섯 번째 어휘 세트', 8),
       (6, '어휘 세트 6', '여섯 번째 어휘 세트', 6),
       (7, '어휘 세트 7', '일곱 번째 어휘 세트', 9),
       (8, '어휘 세트 8', '여덟 번째 어휘 세트', 4),
       (9, '어휘 세트 9', '아홉 번째 어휘 세트', 2),
       (10, '어휘 세트 10', '열 번째 어휘 세트', 11);

INSERT INTO word (expression)
VALUES ('apple'),
       ('run'),
       ('happy'),
       ('quickly'),
       ('beautiful'),
       ('banana'),
       ('jump'),
       ('sad'),
       ('slowly'),
       ('orange');

INSERT INTO definition (definition, type)
VALUES ('사과', '명사'),
       ('달리다', '동사'),
       ('행복한', '형용사'),
       ('빠르게', '부사'),
       ('아름다운', '형용사'),
       ('바나나', '명사'),
       ('점프하다', '동사'),
       ('슬픈', '형용사'),
       ('천천히', '부사'),
       ('오렌지', '명사');

INSERT INTO vocab_word (vocab_id, word_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (3, 6),
       (3, 7),
       (4, 8),
       (4, 9),
       (5, 10);

INSERT INTO word_definition (word_id, definition_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10);

INSERT INTO word_stastics (word_id, correct_count, incorrect_count, isLearned)
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

select *
from user;
select *
from vocab;
select *
from word;
select *
from vocab_word;
select *
from definition;
select *
from word_definition;
select *
from word_stastics;
select *
from theme;
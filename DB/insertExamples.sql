show tables;
describe users;
describe vocabs;
describe words;
describe defs;
describe stats;

INSERT INTO users (username, password) VALUES
('user1', 'password1'),
('user2', 'password2');

INSERT INTO vocabs (user_id, title, description, word_count) VALUES
(1, 'Vocabulary Set 1', 'First vocabulary set', 10),
(1, 'Vocabulary Set 2', 'Second vocabulary set', 10),
(2, 'Vocabulary Set 3', 'Third vocabulary set', 10);

INSERT INTO words (vocab_id, expression) VALUES
-- Vocabulary Set 1
(1, 'apple'), (1, 'run'), (1, 'happy'), (1, 'table'), (1, 'chair'),
(1, 'jump'), (1, 'banana'), (1, 'orange'), (1, 'sky'), (1, 'mountain'),
-- Vocabulary Set 2
(2, 'cat'), (2, 'dog'), (2, 'book'), (2, 'game'), (2, 'dream'),
(2, 'king'), (2, 'queen'), (2, 'water'), (2, 'fire'), (2, 'earth'),
-- Vocabulary Set 3
(3, 'music'), (3, 'dance'), (3, 'sing'), (3, 'laugh'), (3, 'cry'),
(3, 'smile'), (3, 'frown'), (3, 'play'), (3, 'study'), (3, 'write');

INSERT INTO defs (word_id, definition, type) VALUES
-- Vocabulary Set 1 (ID 1~10)
(1, '사과', 'NOUN'), (2, '달리다', 'VERB'), (3, '행복한', 'ADJECTIVE'), 
(4, '탁자', 'NOUN'), (5, '의자', 'NOUN'),
(6, '점프하다', 'VERB'), (7, '바나나', 'NOUN'), (8, '오렌지', 'NOUN'), 
(9, '하늘', 'NOUN'), (10, '산', 'NOUN'),
-- Vocabulary Set 2 (ID 11~20)
(11, '고양이', 'NOUN'), (12, '강아지', 'NOUN'), (13, '책', 'NOUN'),
(14, '게임', 'NOUN'), (15, '꿈', 'NOUN'),
(16, '왕', 'NOUN'), (17, '여왕', 'NOUN'), (18, '물', 'NOUN'),
(19, '불', 'NOUN'), (20, '흙', 'NOUN'),
-- Vocabulary Set 3 (ID 21~30)
(21, '음악', 'NOUN'), (22, '춤추다', 'VERB'), (23, '노래하다', 'VERB'),
(24, '웃다', 'VERB'), (25, '울다', 'VERB'),
(26, '미소', 'NOUN'), (27, '찡그리다', 'VERB'), (28, '놀다', 'VERB'),
(29, '공부하다', 'VERB'), (30, '쓰다', 'VERB');

INSERT INTO stats (word_id, correct_count, incorrect_count, is_learned) VALUES
-- Vocabulary Set 1
(1, 10, 2, 1), (2, 5, 3, 1), (3, 8, 1, 1), (4, 3, 0, 0), (5, 6, 1, 1),
(6, 4, 1, 0), (7, 5, 3, 1), (8, 2, 2, 0), (9, 1, 1, 0), (10, 7, 3, 1),
-- Vocabulary Set 2
(11, 9, 1, 1), (12, 6, 2, 1), (13, 4, 0, 1), (14, 3, 3, 0), (15, 7, 1, 1),
(16, 5, 2, 1), (17, 2, 2, 0), (18, 6, 0, 1), (19, 8, 1, 1), (20, 1, 1, 0),
-- Vocabulary Set 3
(21, 10, 0, 1), (22, 3, 2, 0), (23, 4, 1, 0), (24, 5, 3, 1), (25, 2, 2, 0),
(26, 7, 1, 1), (27, 6, 2, 1), (28, 3, 1, 0), (29, 8, 0, 1), (30, 9, 1, 1);

select * from users;
select * from vocabs;
select * from words;
select * from defs;
select * from stats;

DELETE
FROM user_role;

DELETE
FROM users;

DELETE
FROM meals;


ALTER SEQUENCE global_seq RESTART WITH 100000;
ALTER SEQUENCE meals_id_seq RESTART WITH 1;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

insert into meals(user_id, datetime, description, calories)
values (100000, '2024-01-10 08:00:00', 'завтрак', 500),
       (100000, '2024-01-10 12:00:00', 'обед', 500),
       (100000, '2024-01-10 17:00:00', 'ужин', 500),
       (100001, '2024-01-10 08:00:00', 'завтрак', 500),
       (100001, '2024-01-10 12:00:00', 'обед', 500),
       (100001, '2024-01-10 17:00:00', 'ужин', 500)


CREATE TABLE users(
    id UUID primary key,
    email varchar(100) not null,
    password varchar(200) not null,
    nickname varchar(100) not null,
    name varchar(100) not null,
    phone_number varchar(12),
    role varchar(100) not null,
    active boolean not null
);

INSERT INTO users (id, email, password, nickname, name, phone_number, role, active)
VALUES
('9453e047-a56b-42f6-95a5-328b8a22841e', 'denchik1@example.com', '$2a$10$RxP2XkafMfOeycRQz2LBeO0M1lLQu8hklWq4Uv5Ny5ECU5MXmWpuC', 'denchik', 'John John', '88005553535', 'ADMIN', 'true'),
('4c6a8dda-f4f2-4981-8a01-c314115828f6', 'rus@russia.ru', '$2a$10$RxP2XkafMfOeycRQz2LBeO0M1lLQu8hklWq4Uv5Ny5ECU5MXmWpuC', 'Bronislav', 'Margo Black', null, 'USER', 'true'),
('8513be2f-c6bd-4ffd-83ef-d03773756e53', 'yascher@snake.com', '$2a$10$RxP2XkafMfOeycRQz2LBeO0M1lLQu8hklWq4Uv5Ny5ECU5MXmWpuC', 'Cobron', 'Goblin Grey', null, 'USER', 'true');

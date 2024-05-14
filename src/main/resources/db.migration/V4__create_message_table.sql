CREATE TABLE message(
    id UUID primary key,
    topic_id UUID not null REFERENCES topic(id),
    text varchar(10000) not null,
    user_creator_id UUID REFERENCES users(id) on delete set null,
    creation_date timestamp not null,
    editing_date timestamp
    );

INSERT INTO message (id, topic_id, text, user_creator_id, creation_date, editing_date)
VALUES
    ('01e5d538-3984-4ab2-ba10-6da54b8fec4c', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Привет', '4c6a8dda-f4f2-4981-8a01-c314115828f6', '2024-02-05 09:00:00', NULL),
    ('12d657a2-db98-460e-bca2-739ec1478cc4', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Как дела', '4c6a8dda-f4f2-4981-8a01-c314115828f6', '2024-02-22 09:00:00', NULL),
    ('d0ea02b8-236f-4cf2-bd35-b9e784eb2439', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Делаю джаву', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-02-08 09:00:00', NULL),
    ('184dc794-942a-4583-ba03-5160ba866a0a', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Купил 10 литров колы', '8513be2f-c6bd-4ffd-83ef-d03773756e53', '2024-02-01 09:00:00', NULL),
    ('1ec9b48c-dd26-4d3e-a838-6a50a546c864', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Открыл бизнес', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-02-10 09:00:00', NULL),
    ('d3cbc10c-a657-4f9d-993b-32656f21eb2a', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Я инвестор', '4c6a8dda-f4f2-4981-8a01-c314115828f6', '2024-02-15 09:00:00', NULL),
    ('b1f87ed5-9f3c-4e95-83e2-cb0f57282855', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Здравствуйте', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-02-20 09:00:00', NULL),
    ('d187d172-4054-49e5-9f61-bae1524ecf49', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Как вы', '4c6a8dda-f4f2-4981-8a01-c314115828f6', '2024-03-05 09:00:00', NULL),
    ('027684c5-3fa5-48f3-96ac-f7a04161543c', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Продолжайте', '8513be2f-c6bd-4ffd-83ef-d03773756e53', '2024-03-10 09:00:00', NULL),
    ('b267a3c1-7a78-4c8f-ace7-b0d68372a7a3', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Удачи', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-03-15 09:00:00', NULL),
    ('7d124c9f-9e47-4a7a-927e-7552e59b9167', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Уверен, что получится', '4c6a8dda-f4f2-4981-8a01-c314115828f6', '2024-03-20 09:00:00', NULL),
    ('71e4f6a6-1c12-42cc-bc27-4562347e0716', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Не сомневайтесь', '8513be2f-c6bd-4ffd-83ef-d03773756e53', '2024-03-25 09:00:00', NULL),
    ('5be18e28-724f-46cf-b3f4-41b189c4f46d', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Хорошего дня', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-04-01 09:00:00', NULL),
    ('9146b520-64e5-4c1e-b487-09c1ab95b10f', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Всего доброго', '4c6a8dda-f4f2-4981-8a01-c314115828f6', '2024-04-05 09:00:00', NULL),
    ('8aee70e4-1083-41c1-892f-8a70a8d5437d', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'До свидания', '8513be2f-c6bd-4ffd-83ef-d03773756e53', '2024-04-10 09:00:00', NULL),
    ('7a1545f3-cd85-44d1-9a17-02d5b2d58839', '9e40b5f3-39c2-408c-97a8-03b4ca64d671', 'Пока', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-04-15 09:00:00', NULL);
CREATE TABLE category(
    id UUID primary key,
    parent_category_id UUID REFERENCES category(id),
    name varchar(100) not null,
    user_creator_id UUID REFERENCES users(id),
    creation_date timestamp not null,
    editing_date timestamp
    );

INSERT INTO category (id, parent_category_id, name, user_creator_id, creation_date, editing_date)
VALUES
    ('ca93107d-82bf-41aa-86da-789a5738ef35', NULL, 'Category 1', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-02-23 08:00:00', NULL),
    ('4361d1d1-3116-4a84-ae41-fcd07da45331', 'ca93107d-82bf-41aa-86da-789a5738ef35', 'Category 1.1', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-02-01 09:00:00', NULL),
    ('175207a8-c958-48f2-afd9-bad33cc2eeab', '4361d1d1-3116-4a84-ae41-fcd07da45331', 'Category 1.1.1', '9453e047-a56b-42f6-95a5-328b8a22841e', '2024-02-23 10:00:00', NULL),
    ('8c49cf77-b34a-46f5-8236-b7014044937c', 'ca93107d-82bf-41aa-86da-789a5738ef35', 'Category 1.2', '4c6a8dda-f4f2-4981-8a01-c314115828f6', '2024-01-01 11:00:00', NULL),
    ('52319e1d-bb34-4fc7-b342-9b865ea087a2', 'ca93107d-82bf-41aa-86da-789a5738ef35', 'Category 1.3', '8513be2f-c6bd-4ffd-83ef-d03773756e53', '2024-02-23 12:00:00', NULL),
    ('f2ec0c60-da35-408e-97e3-81676b1c122e', NULL, 'Category 2', '8513be2f-c6bd-4ffd-83ef-d03773756e53', '2024-02-23 13:00:00', NULL);


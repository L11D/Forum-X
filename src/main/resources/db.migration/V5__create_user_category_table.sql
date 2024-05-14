CREATE TABLE user_category(
                    user_id UUID REFERENCES users(id) on delete cascade,
                    category_id UUID REFERENCES category(id) on delete cascade,
                    primary key (user_id, category_id)
);
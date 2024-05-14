CREATE TABLE refresh_token(
                        id UUID primary key,
                        expire_date timestamp not null,
                        user_id UUID REFERENCES users(id) on delete cascade
);
CREATE TABLE message_attachment(
    id UUID primary key,
    file_name varchar(1000) not null,
    byte_size bigint not null,
    upload_date timestamp not null,
    message_id UUID REFERENCES message(id) on delete cascade
);

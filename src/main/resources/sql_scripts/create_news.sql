-- auto-generated definition
create table news
(
    id          int auto_increment
        primary key,
    name        varchar(255) not null,
    description varchar(528) not null,
    file_name   varchar(255) not null,
    added_date  timestamp    not null
);


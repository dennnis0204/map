create sequence users_id_seq start 1 increment 1;
create sequence charging_stations_id_seq start 1 increment 1;

create table users
(
    id          int8         not null,
    email       varchar(255) not null,
    name        varchar(255),
    password    varchar(255),
    provider    varchar(255),
    provider_id varchar(255),
    primary key (id)
);

create table charging_stations
(
    id        int8          not null,
    latitude  numeric(6, 2) not null,
    longitude numeric(6, 2) not null,
    user_id   int8,
    primary key (id)
);

alter table if exists users
    add constraint user_email unique (email);
alter table if exists charging_stations
    add constraint charging_stations_user_fk foreign key (user_id) references users;

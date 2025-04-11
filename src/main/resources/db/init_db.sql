create table users
(
    id           int auto_increment
        primary key,
    email        varchar(255)                       not null,
    password     varchar(255)                       not null,
    avatar       varchar(255)                       null,
    gender       char(10)                           null,
    introduce    text                               null,
    nick_name    varchar(255)                       null,
    level        char(10)                           null,
    role         varchar(255)                       not null,
    created_date datetime default current_timestamp not null,
    updated_date datetime                           null on update current_timestamp,
    constraint users_pk_2
        unique (email)
);

create table tournaments
(
    id                 int auto_increment
        primary key,
    name               varchar(255)                       not null,
    avatar             varchar(255)                       null,
    introduce          text                               null,
    competition_format text(2000)                         null,
    fee                double                             null,
    amount_limit       int                                null,
    award              double                             null,
    sponser            text                               null,
    started_date       datetime                           null,
    ended_date         datetime                           null,
    created_date       datetime default current_timestamp not null,
    updated_date       datetime                           null on update current_timestamp
);


create table pair_play_tournament
(
    id            int auto_increment
        primary key,
    player_1      int                                not null,
    player_2      int                                not null,
    player_winner int                                not null,
    round         int                                not null,
    result        varchar(255)                       not null,
    table_play    varchar(255)                       not null,
    started_date  datetime                           null,
    ended_date    datetime                           null,
    created_date  datetime default current_timestamp not null,
    updated_date  datetime                           null on update current_timestamp,
    tournament_id int                                not null
);

alter table users
    add active boolean default true not null;

alter table users
    add is_deleted boolean default false not null;

alter table tournaments
    add is_deleted boolean default false not null;

alter table tournaments
    add active boolean default true not null;


alter table users
    add name varchar(255) null;


create table user_refresh_token
(
    id            int auto_increment
        primary key,
    user_id       int                                not null,
    refresh_token varchar(255)                       null,
    invoke        boolean  default false             not null,
    created_date  datetime default CURRENT_TIMESTAMP not null,
    updated_date  datetime                           null on update CURRENT_TIMESTAMP
);

create table roles
(
    id   int auto_increment
        primary key,
    name varchar(255) null
);

create table user_roles
(
    id      int auto_increment
        primary key,
    user_id int not null,
    role_id int not null
);

alter table users
    drop column role;

INSERT INTO roles (name) VALUES
                             ('ADMIN'),
                             ('ORGANIZER'),
                             ('PLAYER');

alter table tournaments
    change active status int null;

alter table tournaments
    change sponser sponsor varchar(255) null;

alter table tournaments
    modify introduce text(2000) null;

create table tournament_register
(
    id               int auto_increment
        primary key,
    player_id        int                                not null,
    is_payment_fee   boolean                            null,
    payment_fee_date datetime                           null,
    created_date     datetime default CURRENT_TIMESTAMP not null,
    updated_date     datetime                           null on update CURRENT_TIMESTAMP,
    tournament_id    int                                null,
    status           int                                null,
    award            int                                null
);

alter table tournament_register
    add payment_method varchar(255) null;

alter table tournaments
    add success_amount int default 0 not null;

alter table tournaments
    add cancel_amount int default 0 not null;

alter table tournament_register
    modify status boolean default true not null;

create table vnpay_payments
(
    id                  int auto_increment
        primary key,
    amount              mediumtext                         null,
    bank_code           char(32)                           null,
    bank_transaction_no char(32)                           null,
    card_type           char(32)                           null,
    order_info          varchar(255)                       null,
    pay_date            datetime                           null,
    status              int                                null,
    transaction_no      char(32)                           null,
    txn_ref             char(10)                           null,
    language            char(32)                           null,
    user_id             int                                not null,
    tournament_id       int                            not null,
    created_date        datetime default CURRENT_TIMESTAMP not null,
    updated_date        datetime                           null on update CURRENT_TIMESTAMP
);

alter table vnpay_payments
    change user_id player_id int not null;


create schema if not exists auth;

create table role
(
    id   int auto_increment comment '権限ID'
        primary key,
    name varchar(255) not null comment '権限名'
) comment '権限';

insert into auth.role (name)
values ('user'),
       ('admin');

create table user
(
    id        varchar(255) not null
        primary key,
    name      varchar(255) null comment 'ユーザー名',
    email     varchar(255) null comment 'メールアドレス',
    role_id   int          not null comment '権限ID',
    activated tinyint(1) not null comment '有効かどうか',
    constraint user_role_id_fk
        foreign key (role_id) references role (id)
) comment 'ユーザー';

create table login_info
(
    id           bigint auto_increment comment 'ログイン情報ID'
        primary key,
    provider_id  varchar(255) null comment 'プロバイダID',
    provider_key varchar(255) null comment 'プロバイダキー'
) comment 'ログイン情報';

create table user_login_info
(
    user_id       varchar(255) not null comment 'ユーザーID',
    login_info_id bigint       not null,
    constraint user_login_info_login_info_id_fk
        foreign key (login_info_id) references login_info (id),
    constraint user_login_info_user_id_fk
        foreign key (user_id) references user (id)
) comment 'userとlogin_infoの中間テーブル';

create table password_info
(
    hasher        varchar(255) not null comment 'ハッシャー',
    password      varchar(255) not null comment 'パスワード',
    salt          varchar(255) null comment 'ソルト',
    login_info_id bigint       not null comment 'ログイン情報ID',
    constraint password_info_login_info_id_fk
        foreign key (login_info_id) references login_info (id)
) comment 'パスワード情報';


create table token
(
    id      varchar(255)                          not null comment '認証トークンID'
        primary key,
    user_id varchar(255)                          not null comment 'ユーザーID',
    expiry  timestamp default current_timestamp() not null on update current_timestamp () comment '有効期限',
    constraint token_user_id_fk
        foreign key (user_id) references user (id)
) comment '認証トークン';



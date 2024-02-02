create table users
(
    id               uuid primary key,
    email            varchar(255)                not null unique,
    is_admin         boolean                     not null,
    balance          bigint                      not null,
    first_name       varchar(255),
    last_name        varchar(255),
    created_at       timestamp(6) with time zone not null,
    last_modified_at timestamp(6) with time zone not null,
    version          bigint
);

create table deployments
(
    id               uuid primary key,
    user_id          uuid references users,
    realm_name       varchar(255)                not null unique,
    plan             varchar(255),
    state            varchar(255)                not null,
    created_at       timestamp(6) with time zone not null,
    last_modified_at timestamp(6) with time zone not null,
    version          bigint
);

create table tickets
(
    id               uuid primary key,
    state            varchar(255)                not null,
    customer_id      uuid references users,
    created_at       timestamp(6) with time zone not null,
    last_modified_at timestamp(6) with time zone not null,
    version          bigint
);

create table dialogs
(
    id         uuid primary key,
    text       varchar(255)                not null,
    ticket_id  uuid references tickets,
    user_id    uuid references users,
    created_at timestamp(6) with time zone not null
);

create table tickets_dialogs
(
    dialogs_id      uuid not null unique references dialogs,
    ticket_model_id uuid not null references tickets
);

create table users_deployments
(
    deployments_id uuid not null unique references deployments,
    user_model_id  uuid not null references users
);

create table users_tickets
(
    tickets_id    uuid not null unique references tickets,
    user_model_id uuid not null references users
);

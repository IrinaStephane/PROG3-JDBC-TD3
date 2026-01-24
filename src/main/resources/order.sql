create table if not exists "order" (
    id                serial primary key,
    reference         varchar(255) not null,
    creation_datetime timestamp default now()
);

create table if not exists dish_order (
    id       serial primary key,
    id_order int,
    id_dish  int,
    quantity int,
    foreign key (id_order) references "order" (id),
    foreign key (id_dish) references dish (id)
);
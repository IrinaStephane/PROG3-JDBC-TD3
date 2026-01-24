create type mouvement_type as enum ('IN', 'OUT');

create table if not exists stock_movement (
    id                serial primary key,
    id_ingredient     int,
    quantity          numeric(10, 2) not null,
    type              mouvement_type not null,
    unit              unit_type      not null,
    creation_datetime timestamp default current_timestamp,
    foreign key (id_ingredient) references ingredient (id)
);

insert into stock_movement (id, id_ingredient, quantity, type, unit, creation_datetime)
values (1, 1, 5.0, 'IN', 'KG', '2024-01-05 08:00'),
       (2, 1, 0.2, 'OUT', 'KG', '2024-01-06 12:00'),
       (3, 2, 4.0, 'IN', 'KG', '2024-01-05 08:00'),
       (4, 2, 0.15, 'OUT', 'KG', '2024-01-06 12:00'),
       (5, 3, 10.0, 'IN', 'KG', '2024-01-04 09:00'),
       (6, 3, 1.0, 'OUT', 'KG', '2024-01-06 13:00'),
       (7, 4, 3.0, 'IN', 'KG', '2024-01-05 10:00'),
       (8, 4, 0.3, 'OUT', 'KG', '2024-01-06 14:00'),
       (9, 5, 2.5, 'IN', 'KG', '2024-01-05 10:00'),
       (10, 5, 0.2, 'OUT', 'KG', '2024-01-06 14:00');
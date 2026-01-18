create type unit_type as enum ('PCS', 'KG', 'L');

create table dish_ingredient
(
    id                serial primary key,
    id_dish           int references dish (id),
    id_ingredient     int references ingredient (id),
    quantity_required numeric(10, 2),
    unit              unit_type
);
ALTER TABLE ingredient
DROP COLUMN IF EXISTS id_dish,
DROP COLUMN IF EXISTS required_quantity;

insert into dish_ingredient (id, id_dish, id_ingredient, quantity_required, unit)
values (1, 1, 1, 0.20, 'KG'),
       (2, 1, 2, 0.15, 'KG'),
       (3, 2, 3, 1.00, 'KG'),
       (4, 4, 4, 0.30, 'KG'),
       (5, 4, 5, 0.20, 'KG');

SELECT setval('dish_ingredient_id_seq', (SELECT MAX(id) FROM dish_ingredient));

UPDATE dish SET price = 3500.00 WHERE id = 1;
UPDATE dish SET price = 12000.00 WHERE id = 2;
UPDATE dish SET price = 8000.00 WHERE id = 4;

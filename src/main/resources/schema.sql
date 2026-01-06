CREATE TYPE dish_type_enum AS ENUM ('START', 'MAIN', 'DESSERT');

CREATE TYPE ingredient_category AS ENUM (
    'VEGETABLE',
    'ANIMAL',
    'MARINE',
    'DAIRY',
    'OTHER'
);

CREATE TABLE dish(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255),
    dish_type dish_type_enum
);

CREATE TABLE ingredient(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255) UNIQUE,
    price    NUMERIC(10, 2),
    category ingredient_category,
    id_dish  INTEGER,
    CONSTRAINT fk_dish
        FOREIGN KEY (id_dish)
            REFERENCES dish (id)
);
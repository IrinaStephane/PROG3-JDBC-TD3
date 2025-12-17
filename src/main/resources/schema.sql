CREATE TYPE dish_type_enum AS ENUM ('START', 'MAIN', 'DESSERT');

CREATE TYPE category_enum AS ENUM (
    'VEGETABLE',
    'ANIMAL',
    'MARINE',
    'DAIRY',
    'OTHER'
);

CREATE TABLE Dish(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255)   NOT NULL,
    dish_type dish_type_enum NOT NULL
);

CREATE TABLE Ingredient(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255)   NOT NULL UNIQUE,
    price    NUMERIC(10, 2) NOT NULL,
    category category_enum  NOT NULL,
    id_dish  INTEGER,
    CONSTRAINT fk_dish
        FOREIGN KEY (id_dish)
            REFERENCES Dish (id)
);
INSERT INTO dish (id, name, dish_type)
VALUES (1, 'Salade Fraiche', 'START'),
       (2, 'Poulet grille', 'MAIN'),
       (3, 'Riz au legumes', 'MAIN'),
       (4, 'Gateau au chocolat', 'DESSERT'),
       (5, 'Salade de fruit', 'DESSERT');

INSERT INTO ingredient (id, name, price, category, id_dish)
VALUES (1, 'Laitue', 800.00, 'VEGETABLE', 1),
       (2, 'Tomate', 600.00, 'VEGETABLE', 1),
       (3, 'Poulet', 4500.00, 'ANIMAL', 2),
       (4, 'Chocolat', 3000.00, 'OTHER', 4),
       (5, 'Beurre', 2500.00, 'DAIRY', 4);

select setvail('"Dish_id_seq"', select(max(id) from "dish"));
select setvail('"Ingredient_id_seq"', select(max(id) from "ingredient"));


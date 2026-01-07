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

SELECT setval('dish_id_seq', (SELECT MAX(id) FROM dish));
SELECT setval('ingredient_id_seq', (SELECT MAX(id) FROM ingredient));

ALTER TABLE dish ADD COLUMN IF NOT EXISTS price NUMERIC(10,2);
-- 2) Mettre à jour les valeurs demandées (utilise le nom pour retrouver les plats)
UPDATE dish SET price = 2000.00 WHERE name = 'Salade Fraiche';
UPDATE dish SET price = 6000.00 WHERE name = 'Poulet grille';
UPDATE dish SET price = NULL    WHERE name = 'Riz au legumes';
UPDATE dish SET price = NULL    WHERE name = 'Gateau au chocolat';
UPDATE dish SET price = NULL    WHERE name = 'Salade de fruit';
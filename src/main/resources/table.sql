-- table des tables de restaurant (éviter le nom réservé "table")
CREATE TABLE IF NOT EXISTS restaurant_table (
    id serial PRIMARY KEY,
    number integer NOT NULL UNIQUE
);

-- table des réservations / affectations table = commande
CREATE TABLE IF NOT EXISTS table_order (
   id serial PRIMARY KEY,
   id_order int REFERENCES "order"(id),
   id_table int REFERENCES restaurant_table(id),
   arrival_datetime timestamp without time zone NOT NULL,
   departure_datetime timestamp without time zone NOT NULL
);

INSERT INTO restaurant_table (id, number) VALUES
  (1, 1),
  (2, 2),
  (3, 3);

-- commande existante
INSERT INTO "order"(reference, creation_datetime)
VALUES ('CMD-001', '2026-01-29 12:00')
RETURNING id;

-- supposons id = 1
INSERT INTO table_order (id_order, id_table, arrival_datetime, departure_datetime)
VALUES (1, 1, '2026-01-29 12:00', '2026-01-29 14:00');

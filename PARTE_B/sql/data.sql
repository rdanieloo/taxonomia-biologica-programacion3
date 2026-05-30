-- Datos de prueba: taxonomía biológica (3+ niveles, 9 nodos)
DELETE FROM nodes;

ALTER SEQUENCE nodes_id_seq RESTART WITH 1;

INSERT INTO nodes (id, "value", parent_id) VALUES
(1, 'Animalia', NULL),
(2, 'Chordata', 1),
(3, 'Mammalia', 2),
(4, 'Primates', 3),
(5, 'Hominidae', 4),
(6, 'Homo', 5),
(7, 'Homo sapiens', 6),
(8, 'Arthropoda', 1),
(9, 'Insecta', 8);

SELECT setval('nodes_id_seq', (SELECT MAX(id) FROM nodes));

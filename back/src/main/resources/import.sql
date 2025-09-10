INSERT INTO tipo_evento (nombre, duracion_tipica, duracion_minima, duracion_maxima, aforo_habitual) VALUES ('Concierto', 120, 90, 180, 500);
INSERT INTO tipo_evento (nombre, duracion_tipica, duracion_minima, duracion_maxima, aforo_habitual) VALUES ('Conferencia', 60, 45, 90, 200);
INSERT INTO tipo_evento (nombre, duracion_tipica, duracion_minima, duracion_maxima, aforo_habitual) VALUES ('Taller', 180, 120, 240, 30);

-- eventos para hoy día 10 de septiembre de 2025
INSERT INTO evento (nombre, fecha_hora, duracion_especifica, aforo_especifico, descripcion, lugar, tipo_evento_id) VALUES ('Concierto de Rock', '2025-09-10 21:00:00', 150, 450, 'Gran concierto de la banda local de rock.', 'Sala de Conciertos Principal', 1);
INSERT INTO evento (nombre, fecha_hora, duracion_especifica, aforo_especifico, descripcion, lugar, tipo_evento_id) VALUES ('Charla sobre IA', '2025-09-10 11:30:00', 75, 150, 'Conferencia sobre los últimos avances en Inteligencia Artificial.', 'Auditorio Central', 2);

-- otros días 
INSERT INTO evento (nombre, fecha_hora, duracion_especifica, aforo_especifico, descripcion, lugar, tipo_evento_id) VALUES ('Taller de Cocina', '2025-09-15 18:00:00', 180, 25, 'Aprende a cocinar platos exóticos.', 'Escuela de Cocina', 3);
INSERT INTO evento (nombre, fecha_hora, duracion_especifica, aforo_especifico, descripcion, lugar, tipo_evento_id) VALUES ('Concierto de Jazz', '2025-09-01 20:00:00', 120, 100, 'Noche de jazz suave.', 'Club de Jazz', 1);
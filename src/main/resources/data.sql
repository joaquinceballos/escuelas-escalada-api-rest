/*
 * Inicializamos base de datos con usuarios y roles
 */

--usuario admin
insert
	into
	usuario (email,
	nombre,
	password,
	id,
	username,
	apellido1,
	pais)
values ('admin@email.es',
'Carla',
'$2a$10$HUXRJe8aGk2xXXZm6eP/MuFhLsR7KvyAen4fnB3kOGLifBwqIvI4O',
1, 'admin', 'admin', 'ES');

--usuario user
insert
	into
	usuario (email,
	nombre,
	password,
	id,
	username,
	apellido1,
	apellido2,
	pais,
	nacimiento)
values ('user@email.es',
'Juan',
'$2a$10$HUXRJe8aGk2xXXZm6eP/MuFhLsR7KvyAen4fnB3kOGLifBwqIvI4O',
2, 'user', 'apellido1', 'apellido2', 'ES', STR_TO_DATE('1980-07-31', '%Y-%m-%d') );

--usuario guest
insert
	into
	usuario (email,
	nombre,
	password,
	id,
	username,
	apellido1,
	apellido2,
	pais,
	nacimiento)
values ('web_guest@email.es',
'Juan',
'$2a$10$HUXRJe8aGk2xXXZm6eP/MuFhLsR7KvyAen4fnB3kOGLifBwqIvI4O',
3, 'web_guest', 'web', 'guest', 'ES', STR_TO_DATE('1980-07-31', '%Y-%m-%d') );

-- rol ADMIN
insert
	into
	rol(ID,
	nombre)
values (4,
"ADMIN");

--rol USER
insert
	into
	rol(ID,
	nombre)
values (5,
"USER");


--rol GUEST
insert
	into
	rol(ID,
	nombre)
values (6,
"GUEST");

--rol ADMIN para usuario admin
insert
	into
	usuario_rol(usuario_id,
	rol_id)
values(1,
4);

--rol USER para usuario admin
insert
	into
	usuario_rol(usuario_id,
	rol_id)
values(1,
5);

--rol USER para usuario user
insert
	into
	usuario_rol(usuario_id,
	rol_id)
values(2,
5);

--rol GUEST para usuario web_guest
insert
	into
	usuario_rol(usuario_id,
	rol_id)
values(3,
6);

-- privilegios
insert into privilegio(id, nombre) values(7, "ESCRITURA");
insert into privilegio(id, nombre) values(8, "LECTURA");
insert into privilegio(id, nombre) values(9, "BORRADO");
insert into privilegio(id, nombre) values(10, "ESCRITURA_ZONA");
insert into privilegio(id, nombre) values(11, "BORRADO_ZONA");

-- privilegios de ADMIN
insert into rol_privilegio(rol_id, privilegio_id) values(4, 7);
insert into rol_privilegio(rol_id, privilegio_id) values(4, 8);
insert into rol_privilegio(rol_id, privilegio_id) values(4, 9);
insert into rol_privilegio(rol_id, privilegio_id) values(4, 10);
insert into rol_privilegio(rol_id, privilegio_id) values(4, 11);

--privilegios de USER
insert into rol_privilegio(rol_id, privilegio_id) values(5, 7);
insert into rol_privilegio(rol_id, privilegio_id) values(5, 8);
insert into rol_privilegio(rol_id, privilegio_id) values(5, 9);

--privilegios de GUEST
insert into rol_privilegio(rol_id, privilegio_id) values(6, 8);

--secuencial
update hibernate_sequence set next_val = 100;



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
-1, 'admin', 'admin', 'ES');

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
-2, 'user', 'apellido1', 'apellido2', 'ES', STR_TO_DATE('1980-07-31', '%Y-%m-%d') );

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
-3, 'web_guest', 'web', 'guest', 'ES', STR_TO_DATE('1980-07-31', '%Y-%m-%d') );

-- rol ADMIN
insert
	into
	rol(ID,
	nombre)
values (-1,
"ADMIN");

--rol USER
insert
	into
	rol(ID,
	nombre)
values (-2,
"USER");


--rol GUEST
insert
	into
	rol(ID,
	nombre)
values (-3,
"GUEST");

--rol ADMIN para usuario admin
insert
	into
	usuario_rol(usuario_id,
	rol_id)
values(-1,
-1);

--rol USER para usuario admin
insert
	into
	usuario_rol(usuario_id,
	rol_id)
values(-1,
-2);

--rol USER para usuario user
insert
	into
	usuario_rol(usuario_id,
	rol_id)
values(-2,
-2);

--rol GUEST para usuario web_guest
insert
	into
	usuario_rol(usuario_id,
	rol_id)
values(-3,
-3);

-- privilegios
insert into privilegio(id, nombre) values(-1, "ESCRITURA");
insert into privilegio(id, nombre) values(-2, "LECTURA");
insert into privilegio(id, nombre) values(-3, "BORRADO");

-- privilegios de ADMIN
insert into rol_privilegio(rol_id, privilegio_id) values(-1, -1);
insert into rol_privilegio(rol_id, privilegio_id) values(-1, -2);
insert into rol_privilegio(rol_id, privilegio_id) values(-1, -3);

--privilegios de USER
insert into rol_privilegio(rol_id, privilegio_id) values(-2, -1);
insert into rol_privilegio(rol_id, privilegio_id) values(-2, -2);

--privilegios de GUEST
insert into rol_privilegio(rol_id, privilegio_id) values(-3, -2);



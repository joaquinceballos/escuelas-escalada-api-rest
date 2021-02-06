/*
 * Inicializamos base de datos con usuarios y roles
 */


/*
--usuario admin
insert
	into
	usuario (email,
	nombre,
	password,
	id)
values ('admin@email.es',
'admin',
'$2a$10$HUXRJe8aGk2xXXZm6eP/MuFhLsR7KvyAen4fnB3kOGLifBwqIvI4O',
-1);

--usuario user
insert
	into
	usuario (email,
	nombre,
	password,
	id)
values ('user@email.es',
'user',
'$2a$10$HUXRJe8aGk2xXXZm6eP/MuFhLsR7KvyAen4fnB3kOGLifBwqIvI4O',
-2);

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
	ROL(ID,
	NOMBRE)
values (-2,
"USER");

--rol ADMIN para usuario admin
insert
	into
	USUARIO_ROL(usuario_id,
	rol_id)
values(-1,
-1);

--rol USER para usuario admin
insert
	into
	USUARIO_ROL(usuario_id,
	rol_id)
values(-1,
-2);

--rol USER para usuario user
insert
	into
	USUARIO_ROL(usuario_id,
	rol_id)
values(-2,
-2);

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
*/


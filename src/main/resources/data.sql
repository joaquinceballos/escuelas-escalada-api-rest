/*
 * Inicializamos base de datos con usuarios y roles
 */

insert
	into
	usuario (email,
	nombre,
	password,
	id)
values ('pepe@email.es',
'pepe',
'$2a$10$HUXRJe8aGk2xXXZm6eP/MuFhLsR7KvyAen4fnB3kOGLifBwqIvI4O',
-1);

insert
	into
	rol(ID,
	nombre)
values (-1,
"ADMIN");

insert
	into
	ROL(ID,
	NOMBRE)
values (-2,
"USER");

insert
	into
	USUARIO_ROL(usuario_id,
	rol_id)
values(-1,
-1);

insert
	into
	USUARIO_ROL(usuario_id,
	rol_id)
values(-1,
-2);
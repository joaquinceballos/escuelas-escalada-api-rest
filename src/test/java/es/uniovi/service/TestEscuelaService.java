package es.uniovi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.NombrePrivilegio;
import es.uniovi.domain.NombreRol;
import es.uniovi.domain.Privilegio;
import es.uniovi.domain.Rol;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Usuario;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RecursoYaExisteException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.repository.EscuelaRepository;
import es.uniovi.repository.PrivilegioRepository;
import es.uniovi.repository.RolRepository;
import es.uniovi.repository.SectorRepository;
import es.uniovi.repository.ViaRepository;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith({ MockitoExtension.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestInstance(Lifecycle.PER_CLASS)
class TestEscuelaService {

	@Autowired
	private EscuelaService escuelaService;

	@Autowired
	private EscuelaRepository escuelaRepository;

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private ViaRepository viaRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private PrivilegioRepository privilegioRepository;

	Authentication authentication = Mockito.mock(Authentication.class);
	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	
	@BeforeAll
	void setupAll() throws Exception{
		addRolesYPrivilegios();
		addUsuarios();
	}

	@BeforeEach
	void setupEach() throws Exception {
		escuelaRepository.deleteAll();
		Mockito.when(authentication.getName()).thenReturn("user");
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	private void addUsuarios() throws RestriccionDatosException {
		Usuario user = new Usuario();
		user.setNombre("Userino");
		user.setUsername("user");
		user.setEmail("user@user.es");
		user.setPassword("12345");
		user.setPais("ES");
		usuarioService.addUsuario(user);
	}

	private void addRolesYPrivilegios() {
		// privilegios
		Privilegio lectura = new Privilegio();
		lectura.setNombre(NombrePrivilegio.LECTURA);
		Privilegio escritura = new Privilegio();
		escritura.setNombre(NombrePrivilegio.ESCRITURA);
		Privilegio borrado = new Privilegio();
		borrado.setNombre(NombrePrivilegio.BORRADO);

		privilegioRepository.save(lectura);
		privilegioRepository.save(escritura);
		privilegioRepository.save(borrado);

		// roles
		Rol rolAdmin = new Rol();
		Rol rolUser = new Rol();
		Rol rolGuest = new Rol();
		
		ArrayList<Privilegio> privilegiosAdmin = new ArrayList<>();
		privilegiosAdmin.add(escritura);
		privilegiosAdmin.add(lectura);
		privilegiosAdmin.add(borrado);
		rolAdmin.setNombre(NombreRol.ADMIN);
		rolAdmin.setPrivilegios(privilegiosAdmin);

		ArrayList<Privilegio> privilegiosUser = new ArrayList<>();
		privilegiosUser.add(escritura);
		privilegiosUser.add(lectura);
		privilegiosUser.add(borrado);
		rolUser.setNombre(NombreRol.USER);
		rolUser.setPrivilegios(privilegiosUser);
		

		ArrayList<Privilegio> privilegiosGuest = new ArrayList<>();
		privilegiosGuest.add(escritura);
		rolGuest.setNombre(NombreRol.GUEST);
		rolGuest.setPrivilegios(privilegiosGuest);

		rolRepository.save(rolAdmin);
		rolRepository.save(rolUser);
		rolRepository.save(rolGuest);
	}

	@Test
	void testGetEscuelas() throws Exception {
		// Con la base de datos vacía compruebo que el método retorna una página vacía y
		// el total de escuelas es 0
		Page<Escuela> page = escuelaService.getEscuelas(0, 50, null);
		assertEquals(0, page.getTotalElements());

		// Añado una escuela y compruebo que se retorna correctamente en la Page
		Escuela escuela1 = nuevaEscuela("Oviedo 1");
		escuela1 = escuelaService.addEscuela(escuela1);
		page = escuelaService.getEscuelas(0, 50, null);
		assertEquals(1, page.getTotalElements());
		assertEquals(escuela1, page.get().findAny().orElse(null));

		// Añado una escuela más, el tamaño total ahora es 2 y la página contiene las
		// dos escuelas grabadas
		Escuela escuela2 = nuevaEscuela("Oviedo 2");
		escuela2 = escuelaService.addEscuela(escuela2);
		page = escuelaService.getEscuelas(0, 50, null);
		assertEquals(2, page.getTotalElements());
		Set<Escuela> set = page.get().collect(Collectors.toSet());
		assertEquals(2, set.size());
		assertTrue(set.contains(escuela1));
		assertTrue(set.contains(escuela2));
	}

	@Test
	void testGetEscuela() throws Exception {
		// Salta exception cuando no se encuentra la escuela
		assertThrows(NoEncontradoException.class, () -> escuelaService.getEscuela(0l));

		// Retorna la escuela correctamente cuano existe la id
		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("Oviedo 1"));
		assertEquals(escuela, escuelaService.getEscuela(escuela.getId()));
	}

	@Test
	void testAddEscuela() throws Exception {
		// Añade correctamente la escuela
		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("Oviedo 1"));
		assertNotNull(escuela);
		assertNotNull(escuela.getId());

		// No me permite añadir una escuela cuyo nombre ya existe
		assertThrows(RestriccionDatosException.class, () -> escuelaService.addEscuela(nuevaEscuela("Oviedo 1")));

		// No me permite añadir una escuela sin nombre
		// TODO se debería validar antes de intentar grabar. Lo suyo es que se lance una
		// excepción comprobada
		assertThrows(TransactionSystemException.class, () -> escuelaService.addEscuela(nuevaEscuela("")));
		assertThrows(TransactionSystemException.class, () -> escuelaService.addEscuela(nuevaEscuela(null)));

		// Persiste correctamente todas las entities cuando paso una escuela con
		// sectores con y sin vías
		Escuela conSectores = nuevaEscuela("Oviedo con sectores");
		Sector sector1 = nuevoSector("sector 1", .0, .0);
		Sector sector2 = nuevoSector("sector 2", null, null);
		Sector sector3 = nuevoSector("sector 3", null, null);
		asocia(conSectores, sector1, sector2, sector3);
		asocia(sector1, nuevaVia("via1", "6a", 5, 10.), nuevaVia("via2", "6a", 10, null),
				nuevaVia("via3", "6a", null, 5.));
		asocia(sector2, nuevaVia("via1", "6a", null, null));
		Escuela escuela2 = escuelaService.addEscuela(conSectores);
		assertEquals(3, escuela2.getSectores().size());
		assertEquals(3, escuela2.getSectores().stream().filter(s -> s.getNombre().equals("sector 1")).findAny()
				.orElse(null).getVias().size());
		assertEquals(1, escuela2.getSectores().stream().filter(s -> s.getNombre().equals("sector 2")).findAny()
				.orElse(null).getVias().size());
		assertEquals(0, escuela2.getSectores().stream().filter(s -> s.getNombre().equals("sector 3")).findAny()
				.orElse(null).getVias().size());
		for (Sector sector : escuela2.getSectores()) {
			assertNotNull(sector.getId());
			for (Via via : sector.getVias()) {
				// assertNotNull(via.getId()); //TODO al insertar un nuevo sector con vías los
				// campos de éstas se ponen a null...
			}
		}

		// Intento añadir una escuela con una vía no válida
		Via noValida = nuevaVia("no valida", "6a", -1, null);
		Sector sector = nuevoSector("sector1", null, null);
		Escuela conViaInvalida = nuevaEscuela("invalida");
		asocia(conViaInvalida, sector);
		asocia(sector, noValida);
		// assertThrows(TransactionSystemException.class, () ->
		// escuelaService.addEscuela(conViaInvalida)); //TODO
	}

	@Test
	void testGetSectores() throws Exception {
		// añadimos escuela sin sectores, se comprueba que getSectores retorna Set vacío
		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("escuela"));
		assertThat(escuelaService.getSectores(escuela.getId())).isEmpty();

		// añadimos un sector a la escuela, se comprueba que getSectores retorna Set
		// conteniendoel sector añadido
		Sector sector1 = escuelaService.addSector(escuela.getId(), nuevoSector("sector1", null, null));
		assertThat(escuelaService.getSectores(escuela.getId())).containsExactlyInAnyOrder(sector1);

		// borramos el sector, se comprueba que la colección de sectores vuelve a estar
		// vacía
		escuelaService.deleteSector(escuela.getId(), sector1.getId());
		assertThat(escuelaService.getSectores(escuela.getId())).isEmpty();

		// añadimos dos sectores, se comprueba que getSectores contiene estos dos
		// elementos y nada más
		Sector sector2 = escuelaService.addSector(escuela.getId(), nuevoSector("sector2", null, null));
		Sector sector3 = escuelaService.addSector(escuela.getId(), nuevoSector("sector3", null, null));
		assertThat(escuelaService.getSectores(escuela.getId())).containsExactlyInAnyOrder(sector2, sector3);

		// lanza excepción cuando la id de la escuela no existe
		assertThrows(NoEncontradoException.class, () -> escuelaService.getSectores(-999l));
	}

	@Test
	void testGetSector() throws Exception {
		Escuela escuela1 = escuelaService.addEscuela(nuevaEscuela("escuela1"));
		Escuela escuela2 = escuelaService.addEscuela(nuevaEscuela("escuela2"));
		Sector sector11 = escuelaService.addSector(escuela1.getId(), nuevoSector("sector11", null, null));
		Sector sector12 = escuelaService.addSector(escuela1.getId(), nuevoSector("sector12", null, null));
		Sector sector21 = escuelaService.addSector(escuela2.getId(), nuevoSector("sector21", null, null));

		// Retorna los sectores esperados cuando se pasan ids válidas
		assertThat(escuelaService.getSector(escuela1.getId(), sector11.getId())).isEqualTo(sector11);
		assertThat(escuelaService.getSector(escuela1.getId(), sector12.getId())).isEqualTo(sector12);
		assertThat(escuelaService.getSector(escuela2.getId(), sector21.getId())).isEqualTo(sector21);

		// NoEncontradoException cuando se pasan ids no válidas
		assertThrows(NoEncontradoException.class, () -> escuelaService.getSector(escuela1.getId(), sector21.getId()));
		assertThrows(NoEncontradoException.class, () -> escuelaService.getSector(escuela2.getId(), sector11.getId()));
		assertThrows(NoEncontradoException.class, () -> escuelaService.getSector(escuela1.getId(), -999l));
		assertThrows(NoEncontradoException.class, () -> escuelaService.getSector(-999l, sector21.getId()));
	}

	@Test
	void testAddSector() throws Exception {
		Escuela escuela1 = escuelaService.addEscuela(nuevaEscuela("escuela1"));

		// Añado un sector sin vías
		Sector sector1 = escuelaService.addSector(escuela1.getId(), nuevoSector("sector1", null, null));
		assertThat(sector1.getId()).isNotNull();
		assertThat(sector1.getVias()).isEmpty();
		assertThat(sector1.getLatitud()).isNull();
		assertThat(sector1.getLongitud()).isNull();

		// Añado un sector con vías
		Sector sector2 = nuevoSector("sector2", 1., -1.);
		asocia(sector2, nuevaVia("via1", "6a", null, null));
		sector2 = escuelaService.addSector(escuela1.getId(), sector2);
		assertThat(sector2.getId()).isNotNull();
		assertThat(sector2.getVias()).isNotEmpty();
		assertThat(sector2.getVias()).allMatch(v -> v.getId() != null).allMatch(v -> v.getNombre().equals("via1"))
				.allMatch(v -> v.getGrado().equals("6a")).allMatch(v -> v.getSector().getId() != null).size()
				.isEqualTo(1);
		assertThat(sector2.getLatitud()).isEqualTo(1.);
		assertThat(sector2.getLongitud()).isEqualTo(-1.);

		// NotEncontradoException cuando se pasa id de escuela no existente
		assertThrows(NoEncontradoException.class,
				() -> escuelaService.addSector(-999l, nuevoSector("sector2", null, null)));

		// No permite añadir sector inválido
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.addSector(escuela1.getId(), nuevoSector("", null, null)));
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.addSector(escuela1.getId(), nuevoSector(null, null, null)));
		assertThrows(RestriccionDatosException.class,
				() -> escuelaService.addSector(escuela1.getId(), nuevoSector("sector1", null, null)));
		assertThrows(RestriccionDatosException.class,
				() -> escuelaService.addSector(escuela1.getId(), nuevoSector("sector2", 90.01, 0.)));
		assertThrows(RestriccionDatosException.class,
				() -> escuelaService.addSector(escuela1.getId(), nuevoSector("sector2", 0., -180.01)));
		assertThrows(RestriccionDatosException.class,
				() -> escuelaService.addSector(escuela1.getId(), nuevoSector("sector2", null, 0.)));

		// No permite añadir sector con vía inválida

		// No permite añadir sector inválido
		assertThrows(TransactionSystemException.class, () -> {
			Sector nuevoSector = nuevoSector("", null, null);
			asocia(nuevoSector, nuevaVia("", "", null, null));
			escuelaService.addSector(escuela1.getId(), nuevoSector);
		});
	}

	@Test
	void testGetVias() throws Exception {
		Escuela escuela1 = escuelaService.addEscuela(nuevaEscuela("escuela"));
		Sector sector11 = escuelaService.addSector(escuela1.getId(), nuevoSector("sector1", null, null));
		Sector sector12 = escuelaService.addSector(escuela1.getId(), nuevoSector("sector2", null, null));
		Sector sector13 = escuelaService.addSector(escuela1.getId(), nuevoSector("sector3", null, null));
		Via via111 = escuelaService.addVia(escuela1.getId(), sector11.getId(), nuevaVia("via1", "6a", null, null));
		Via via112 = escuelaService.addVia(escuela1.getId(), sector11.getId(), nuevaVia("via2", "6a", 0, 10.));
		Via via121 = escuelaService.addVia(escuela1.getId(), sector12.getId(), nuevaVia("via1", "6a", 0, 10.));

		// Compruebo que getVias retorna las vías correctas
		assertThat(escuelaService.getVias(escuela1.getId(), sector11.getId())).containsExactlyInAnyOrder(via111,
				via112);
		assertThat(escuelaService.getVias(escuela1.getId(), sector12.getId())).containsExactlyInAnyOrder(via121);
		assertThat(escuelaService.getVias(escuela1.getId(), sector13.getId())).isEmpty();

		// lanza NoEncontradoException cuando se pasan ids no válidas
		assertThrows(NoEncontradoException.class, () -> escuelaService.getVias(escuela1.getId(), -999l));
		assertThrows(NoEncontradoException.class, () -> escuelaService.getVias(-999l, sector11.getId()));
		escuelaService.deleteSector(escuela1.getId(), sector11.getId());
		assertThrows(NoEncontradoException.class, () -> escuelaService.getVias(escuela1.getId(), sector11.getId()));
	}

	@Test
	void testGetVia() throws Exception {
		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("escuela"));
		Sector sector = escuelaService.addSector(escuela.getId(), nuevoSector("sector", null, null));
		Via via = escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("nombre", "grado", null, null));

		// Retorna la vía correcta
		assertThat(escuelaService.getVia(escuela.getId(), sector.getId(), via.getId())).isEqualTo(via);

		// lanza NoEncontradoException cuando se pasan ids no válidas
		assertThrows(NoEncontradoException.class, () -> escuelaService.getVia(escuela.getId(), sector.getId(), -999l));
		assertThrows(NoEncontradoException.class, () -> escuelaService.getVia(escuela.getId(), -999l, via.getId()));
		assertThrows(NoEncontradoException.class, () -> escuelaService.getVia(-999l, sector.getId(), via.getId()));
	}

	@Test
	void testAddVia() throws Exception {
		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("escuela"));
		Sector sector = escuelaService.addSector(escuela.getId(), nuevoSector("sector", null, null));

		// Me añade correctamente la vía
		Via via1 = escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("via1", "grado", null, null));
		assertThat(via1.getId()).isNotNull();
		assertThat(via1.getNombre()).isEqualTo("via1");
		assertThat(via1.getGrado()).isEqualTo("grado");
		assertThat(via1.getNumeroChapas()).isNull();
		assertThat(via1.getLongitud()).isNull();

		// Me añade correctamente una segunda vía en el mismo sector
		Via via2 = escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("via2", "grado", 5, 10.));
		assertThat(via2.getId()).isNotNull();
		assertThat(via2.getNombre()).isEqualTo("via2");
		assertThat(via2.getGrado()).isEqualTo("grado");
		assertThat(via2.getNumeroChapas()).isEqualTo(5);
		assertThat(via2.getLongitud()).isEqualTo(10.0);

		// salta excepción cuando se intenta añadir vía con nombre que ya existe
		assertThrows(RecursoYaExisteException.class,
				() -> escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("via1", "", null, null)));

		// salta excepción cuando se intenta añadir vía no válida
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("", "", null, null)));
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("vía3", null, null, null)));
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("vía3", "grado", -1, null)));
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("vía3", "grado", 0, -0.1)));

		// salta NoEncontradoException cuando se pasan ids no válidas
		assertThrows(NoEncontradoException.class,
				() -> escuelaService.addVia(-999l, sector.getId(), nuevaVia("via3", "grado", null, null)));
		assertThrows(NoEncontradoException.class,
				() -> escuelaService.addVia(escuela.getId(), -999l, nuevaVia("via3", "grado", null, null)));
	}

	@Test
	void testActualizaEscuela() throws Exception {
		// Grabo escuela con nombre Oviedo
		Long id = escuelaService.addEscuela(nuevaEscuela("Oviedo")).getId();

		// Actualizo la escuela pasando nuevo nombre, compruebo que se actualiza
		// correctamente
		escuelaService.actualizaEscuela(id, nuevaEscuela("Gijón"));
		assertThat(escuelaService.getEscuela(id).getNombre()).isEqualTo("Gijón");

		// Compruebo que salta NoEncontradoException cuado se pasa una id que no existe
		assertThrows(NoEncontradoException.class, () -> escuelaService.actualizaEscuela(-999l, nuevaEscuela("Oviedo")));
	}

	@Test
	void testActualizaSector() throws Exception {
		Long idEscuela = escuelaService.addEscuela(nuevaEscuela("Oviedo")).getId();
		Long idSector = escuelaService.addSector(idEscuela, nuevoSector("sector 1", null, null)).getId();
		escuelaService.addSector(idEscuela, nuevoSector("sector 2", null, null));

		// Actualiza el sector y compruebo que actualiza correcto
		escuelaService.actualizaSector(idEscuela, idSector, nuevoSector("sector 1", .01, 90.0));
		assertThat(escuelaService.getSector(idEscuela, idSector).getNombre()).isEqualTo("sector 1");
		assertThat(escuelaService.getSector(idEscuela, idSector).getLatitud()).isEqualTo(.01);
		assertThat(escuelaService.getSector(idEscuela, idSector).getLongitud()).isEqualTo(90.0);

		escuelaService.actualizaSector(idEscuela, idSector, nuevoSector("sector 3", null, null));
		assertThat(escuelaService.getSector(idEscuela, idSector).getNombre()).isEqualTo("sector 3");
		assertThat(escuelaService.getSector(idEscuela, idSector).getLatitud()).isNull();
		assertThat(escuelaService.getSector(idEscuela, idSector).getLongitud()).isNull();

		// Salta excepción cuando actualizo nombre se sector por otro que ya existe
		assertThrows(DataIntegrityViolationException.class,
				() -> escuelaService.actualizaSector(idEscuela, idSector, nuevoSector("sector 2", null, null)));

		// Salta excepcipón cuando actualizo con datos inválidos
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.actualizaSector(idEscuela, idSector, nuevoSector(null, null, null)));
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.actualizaSector(idEscuela, idSector, nuevoSector("nombre válido", 90.01, 0.0)));
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.actualizaSector(idEscuela, idSector, nuevoSector("nombre válido", null, 0.0)));

		// Salta no encontrado cuando se pasan ids que no existen
		assertThrows(NoEncontradoException.class,
				() -> escuelaService.actualizaSector(idEscuela, -999l, nuevoSector("nombre válido", null, null)));
	}

	@Test
	void testActualizaVia() throws Exception {
		Long idEscuela = escuelaService.addEscuela(nuevaEscuela("Oviedo")).getId();
		Long idSector = escuelaService.addSector(idEscuela, nuevoSector("sector", null, null)).getId();
		Long idVia1 = escuelaService.addVia(idEscuela, idSector, nuevaVia("vía1", "6a", 10, 10.)).getId();
		escuelaService.addVia(idEscuela, idSector, nuevaVia("vía2", "6a", null, null)).getId();

		Via via = escuelaService.getVia(idEscuela, idSector, idVia1);
		assertThat(via.getNombre()).isEqualTo("vía1");
		assertThat(via.getGrado()).isEqualTo("6a");
		assertThat(via.getNumeroChapas()).isEqualTo(10);
		assertThat(via.getLongitud()).isEqualTo(10.0);

		// Actualizo la vía y compruebo que se actualizó correctamente
		escuelaService.actualizaVia(idEscuela, idSector, idVia1, nuevaVia("vía3", "8a+", null, null));
		via = escuelaService.getVia(idEscuela, idSector, idVia1);
		assertThat(via.getNombre()).isEqualTo("vía3");
		assertThat(via.getGrado()).isEqualTo("8a+");
		assertThat(via.getNumeroChapas()).isNull();
		assertThat(via.getLongitud()).isNull();

		// No permite actualizar vía si ya existe una en el sector con ese nombre
		assertThrows(DataIntegrityViolationException.class,
				() -> escuelaService.actualizaVia(idEscuela, idSector, idVia1, nuevaVia("vía2", "grado", null, null)));
		// Tampoco con datos inválidos
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.actualizaVia(idEscuela, idSector, idVia1, nuevaVia("vía4", null, null, null)));
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.actualizaVia(idEscuela, idSector, idVia1, nuevaVia("", "grado", null, null)));
		assertThrows(TransactionSystemException.class,
				() -> escuelaService.actualizaVia(idEscuela, idSector, idVia1, nuevaVia("vía4", "grado", -10, null)));
	}

	@Test
	void testDeleteEscuela() throws Exception {
		Long idEscuela1 = escuelaService.addEscuela(nuevaEscuela("Oviedo")).getId();
		Long idSector = escuelaService.addSector(idEscuela1, nuevoSector("sector", null, null)).getId();
		Long idVia = escuelaService.addVia(idEscuela1, idSector, nuevaVia("vía", "grado", null, null)).getId();
		Long idEscuela2 = escuelaService.addEscuela(nuevaEscuela("Gijón")).getId();

		// Borro la escuela con sectores y vías, compruebo que se ha borrado todo en
		// cascada
		escuelaService.deleteEscuela(idEscuela1);
		assertFalse(escuelaRepository.existsById(idEscuela1));
		assertFalse(sectorRepository.existsById(idSector));
		assertFalse(viaRepository.existsById(idVia));

		// Borro la escuela sin sectores
		escuelaService.deleteEscuela(idEscuela2);
		assertFalse(escuelaRepository.existsById(idEscuela2));

		// Salta no encontrado cuando se pasa id que no existe
		assertThrows(NoEncontradoException.class, () -> escuelaService.deleteEscuela(idEscuela1));
	}

	// @Test
	void testActualizaEscuelaJsonPatchReplace() throws Exception {

		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("Oviedo"));
		Sector sector1 = escuelaService.addSector(escuela.getId(), nuevoSector("sector Oviedo 1", null, null));
		Via via = escuelaService.addVia(escuela.getId(), sector1.getId(), nuevaVia("via Oviedo 1", "6a", null, null));

		// Cambio el nombre a la escuela
		JsonPatch replaceNombreEscuela = getPatch("[{\"op\":\"replace\",\"path\":\"/nombre\",\"value\":\"Gijón\"}]");
		assertThat(escuela.getNombre()).isEqualTo("Oviedo");
		escuela = escuelaService.actualizaEscuela(escuela.getId(), replaceNombreEscuela);
		assertThat(escuela.getNombre()).isEqualTo("Gijón");

		// Cambio el nombre al sector
		// Cambio nombre, número de chapas, longitud y grado a la vía
		JsonPatch replaceNombreSectorYVia = getPatch(
				"[" + "{\"op\":\"replace\",\"path\":\"/sectores/0/nombre\",\"value\":\"sector Gijón 1\"},"
						+ "{\"op\":\"replace\",\"path\":\"/sectores/0/latitud\",\"value\":90},"
						+ "{\"op\":\"replace\",\"path\":\"/sectores/0/longitud\",\"value\":-180},"
						+ "{\"op\":\"replace\",\"path\":\"/sectores/0/vias/0/nombre\",\"value\":\"via Gijón 1\"},"
						+ "{\"op\":\"replace\",\"path\":\"/sectores/0/vias/0/grado\",\"value\":\"8a+\"},"
						+ "{\"op\":\"replace\",\"path\":\"/sectores/0/vias/0/numeroChapas\",\"value\":\"10\"},"
						+ "{\"op\":\"replace\",\"path\":\"/sectores/0/vias/0/longitud\",\"value\":\"25\"}" + "]");
		escuela = escuelaService.actualizaEscuela(escuela.getId(), replaceNombreSectorYVia);

		// Compruebo que se han atualizado correctamente los valores
		sector1 = sectorRepository.findById(sector1.getId()).orElse(null);
		via = viaRepository.findById(via.getId()).orElse(null);
		assertThat(escuela.getNombre()).isEqualTo("Gijón");
		assertThat(escuela.getNombre()).isEqualTo("Gijón");
		assertThat(sector1.getNombre()).isEqualTo("sector Gijón 1");
		assertThat(sector1.getLatitud()).isEqualTo(90);
		assertThat(sector1.getLongitud()).isEqualTo(-180);
		assertThat(via.getNombre()).isEqualTo("via Gijón 1");
		assertThat(via.getGrado()).isEqualTo("8a+");
		assertThat(via.getNumeroChapas()).isEqualTo(10);
		assertThat(via.getLongitud()).isEqualTo(25.);

		// Reemplazo por completo los sectores
		Long idSsector = sector1.getId();
		long idVia = via.getId();
		JsonPatch replaceSectoresLista = getPatch("[" + "{\"op\":\"replace\",\"path\":\"/sectores\",\"value\":["
				+ "{\"nombre\":\"nuevo sector\", \"vias\":[" + "{\"nombre\":\"nueva vía\", \"grado\":\"x\"}" + "]}"
				+ "]}" + "]");
		assertTrue(sectorRepository.existsById(idSsector));
		assertTrue(viaRepository.existsById(idVia));
		escuela = escuelaService.actualizaEscuela(escuela.getId(), replaceSectoresLista);
		assertFalse(sectorRepository.existsById(idSsector));
		assertFalse(viaRepository.existsById(idVia));

		// Intengo actualizar con valores no válidos
		JsonPatch replaceLongitudViaInvalido = getPatch(
				"[" + "{\"op\":\"replace\",\"path\":\"/sectores/0/vias/0/longitud\",\"value\":-0.1}" + "]");
		JsonPatch replaceNombreViaInvalido = getPatch(
				"[" + "{\"op\":\"replace\",\"path\":\"/sectores/0/vias/0/nombre\",\"value\":null}" + "]");
		JsonPatch replaceCoordenadasInvalidas1 = getPatch(
				"[" + "{\"op\":\"replace\",\"path\":\"/sectores/0/latitud\",\"value\":90.01}" + "]");
		JsonPatch replaceCoordenadasInvalidas2 = getPatch(
				"[" + "{\"op\":\"replace\",\"path\":\"/sectores/0/latitud\",\"value\":null},"
						+ "{\"op\":\"replace\",\"path\":\"/sectores/0/longitud\",\"value\":0}" + "]");
		final Long idEscuela = escuela.getId();
		assertThrows(ConstraintViolationException.class,
				() -> escuelaService.actualizaEscuela(idEscuela, replaceLongitudViaInvalido));
		assertThrows(ConstraintViolationException.class,
				() -> escuelaService.actualizaEscuela(idEscuela, replaceNombreViaInvalido));
		assertThrows(ConstraintViolationException.class,
				() -> escuelaService.actualizaEscuela(idEscuela, replaceCoordenadasInvalidas1));
		assertThrows(ConstraintViolationException.class,
				() -> escuelaService.actualizaEscuela(idEscuela, replaceCoordenadasInvalidas2));

		// lanza NoEncontradoException cuando la id no existe
		assertThrows(NoEncontradoException.class, () -> escuelaService.actualizaEscuela(-999l, replaceNombreEscuela));
	}

	// @Test
	void testActualizaEscuelaJsonPatchAdd() throws Exception {
		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("Oviedo 1"));

		// Añado nuevo sector, compruebo que se añade correctamente
		escuela = escuelaService.actualizaEscuela(escuela.getId(),
				getPatch("[" + "{\"op\":\"add\",\"path\":\"/sectores/-\",\"value\":{\"nombre\":\"sector 1\"}}" + "]"));
		assertThat(escuela.getSectores()).isNotEmpty();
		Sector sector1 = escuela.getSectores().stream().filter(s -> "sector 1".equals(s.getNombre())).findAny()
				.orElse(null);
		assertThat(sector1.getId()).isNotNull();
		assertThat(sector1.getVias()).isEmpty();

		// Añado nuevo sector con vías, compruebo que se añaden correctamente
		escuela = escuelaService.actualizaEscuela(escuela.getId(),
				getPatch("[" + "{" + "\"op\":\"add\"," + "\"path\":\"/sectores/-\"," + "\"value\":{"
						+ "\"nombre\":\"sector 2\"," + "\"vias\":[" + "{" + "\"nombre\":\"vía 1\","
						+ "\"grado\":\"6b+\"," + "\"longitud\":10," + "\"numeroChapas\":5" + "}" + "]" + "}" + "}"
						+ "]"));
		assertThat(escuela.getSectores()).size().isEqualTo(2);
		Sector sector2 = escuela.getSectores().stream().filter(s -> "sector 2".equals(s.getNombre())).findAny()
				.orElse(null);
		assertThat(sector2.getId()).isNotNull();
		assertThat(sector2.getVias()).isNotEmpty();
		assertThat(sector2.getVias()).allMatch(v -> v.getId() != null);
		assertThat(sector2.getVias()).allMatch(v -> v.getNombre().equals("vía 1"));
		assertThat(sector2.getVias()).allMatch(v -> v.getGrado().equals("6b+"));
		assertThat(sector2.getVias()).allMatch(v -> v.getLongitud().equals(10.0));
		assertThat(sector2.getVias()).allMatch(v -> v.getNumeroChapas().equals(5));

		Long escuelaId = escuela.getId();
		// No permite añadir sector si el nombre ya existe
		assertThrows(RestriccionDatosException.class, () -> escuelaService.actualizaEscuela(escuelaId,
				getPatch("[" + "{\"op\":\"add\",\"path\":\"/sectores/-\",\"value\":{\"nombre\":\"sector 1\"}}" + "]")));

		// No se permite añadir vía al sector si no es válida (longitud negativa)
		assertThrows(ConstraintViolationException.class, () -> escuelaService.actualizaEscuela(escuelaId,
				getPatch("[" + "{" + "\"op\":\"add\"," + "\"path\":\"/sectores/0/vias/-\","
						+ "\"value\":{\"nombre\":\"vía 1\",\"grado\":\"6b+\",\"longitud\":-10,\"numeroChapas\":5}" + "}"
						+ "]")));
	}

	// @Test
	void testActualizaEscuelaJsonPatchMove() throws Exception {
		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("Oviedo 1"));
		Sector sector1 = escuelaService.addSector(escuela.getId(), nuevoSector("sector1", null, null));
		Sector sector2 = nuevoSector("sector2", null, null);
		asocia(sector2, nuevaVia("vía1", "grado", null, null));
		sector2 = escuelaService.addSector(escuela.getId(), sector2);

		Long idSector1 = sector1.getId();
		Long idSector2 = sector2.getId();
		Long idVia = sector2.getVias().stream().findAny().orElse(null).getId();

		assertThat(escuelaService.getVias(escuela.getId(), idSector1)).isEmpty();
		assertThat(escuelaService.getVias(escuela.getId(), idSector2)).allMatch(v -> v.getNombre().equals("vía1"));
		assertThat(escuelaService.getVia(escuela.getId(), idSector2, idVia)).isNotNull();
		assertThrows(NoEncontradoException.class, () -> escuelaService.getVia(escuela.getId(), idSector1, idVia));

		// muevo la vía de sector, compruebo que se han actualizado correctamente los
		// sectores
		escuelaService.actualizaEscuela(escuela.getId(), getPatch(
				"[" + "{\"op\":\"move\",\"from\":\"/sectores/1/vias/0\",\"path\":\"/sectores/0/vias/0\"}" + "]"));
		assertThat(escuelaService.getVias(escuela.getId(), idSector1)).allMatch(v -> v.getNombre().equals("vía1"));
		assertThat(escuelaService.getVias(escuela.getId(), idSector2)).isEmpty();
		assertThat(escuelaService.getVia(escuela.getId(), idSector1, idVia)).isNotNull();
		assertThrows(NoEncontradoException.class, () -> escuelaService.getVia(escuela.getId(), idSector2, idVia));

		// muevo una vía de sector, ya existe una vía con ese nombre, compruebo que no
		// permite mover la vía
		escuelaService.addVia(escuela.getId(), idSector2, nuevaVia("vía1", "grado", null, null));
		assertThrows(RestriccionDatosException.class, () -> escuelaService.actualizaEscuela(escuela.getId(), getPatch(
				"[" + "{\"op\":\"move\",\"from\":\"/sectores/1/vias/0\",\"path\":\"/sectores/0/vias/0\"}" + "]")));
		assertThat(escuelaService.getVias(escuela.getId(), idSector1)).allMatch(v -> v.getNombre().equals("vía1"));
		assertThat(escuelaService.getVias(escuela.getId(), idSector2)).allMatch(v -> v.getNombre().equals("vía1"));
	}

	// @Test
	void testActualizaEscuelaJsonPatchRemove() throws Exception {
		Escuela escuela = escuelaService.addEscuela(nuevaEscuela("Oviedo"));
		Sector sector = escuelaService.addSector(escuela.getId(), nuevoSector("sector1", null, null));
		Via via1 = escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("vía1", "grado", null, null));
		Via via2 = escuelaService.addVia(escuela.getId(), sector.getId(), nuevaVia("vía2", "grado", null, null));

		// elimino la segunda vía del sector
		escuelaService.actualizaEscuela(escuela.getId(),
				getPatch("[{\"op\":\"remove\",\"path\":\"/sectores/0/vias/1\"}]"));
		assertFalse(viaRepository.existsById(via2.getId()));
	}

	private JsonPatch getPatch(String jsonString) throws JsonMappingException, JsonProcessingException, IOException {
		return JsonPatch.fromJson(new ObjectMapper().readTree(jsonString));
	}

	private void asocia(Sector sector, Via... vias) {
		for (Via via : vias) {
			sector.getVias().add(via);
		}
	}

	private void asocia(Escuela escuela, Sector... sectores) {
		for (Sector sector : sectores) {
			escuela.getSectores().add(sector);
		}
	}

	private Via nuevaVia(String nombre, String grado, Integer numeroChapas, Double longitud) {
		Via via = new Via();
		via.setNombre(nombre);
		via.setGrado(grado);
		via.setLongitud(longitud);
		via.setNumeroChapas(numeroChapas);
		return via;
	}

	private Sector nuevoSector(String nombre, Double latitud, Double longitud) {
		Sector sector = new Sector();
		sector.setNombre(nombre);
		sector.setLatitud(latitud);
		sector.setLongitud(longitud);
		return sector;
	}

	private Escuela nuevaEscuela(String nombre) {
		Escuela escuela = new Escuela();
		escuela.setNombre(nombre);
		return escuela;
	}

}

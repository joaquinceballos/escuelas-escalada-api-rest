package es.uniovi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.dto.SectorDto;
import es.uniovi.dto.ViaDto;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.service.EscuelaService;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith({ MockitoExtension.class })
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
class TestEscuelaController {

	@Mock
	private EscuelaService escuelaService;

	@InjectMocks
	private EscuelaController escuelaController;

	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(escuelaController).build();
		ReflectionTestUtils.setField(escuelaController, "modelMapper", new ModelMapper());
	}

	/**
	 * Llamada correcta se retorna una lista paginada con dos resultados
	 * 
	 * No se pasan parámetros de paginación ni filtro
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetEscuelas() throws Exception {
		
		ArrayList<Escuela> content = new ArrayList<>();
		
		Escuela escuela1 = newEscuela(1l, "escuela 1");
		// sector1 con dos vías
		Sector sector1 = newSector(escuela1, 1l, 0.0, 0.0, "sector 1");
		sector1.getVias().add(newVia(sector1, "6a+", 1l, 10.0, "vía 1", 5));
		sector1.getVias().add(newVia(sector1, "6b+", 2l, 5.0, "vía 2", 10));
		escuela1.getSectores().add(sector1);
		// sector2 sin vías
		escuela1.getSectores().add(newSector(escuela1, 2l, 0.0, 0.0, "sector 2"));
		content.add(escuela1);
		
		// escuela 2 sin sectores
		content.add(newEscuela(2l, "escuela 2"));
				
		Page<Escuela> pageEscuela = new PageImpl<Escuela>(content, PageRequest.of(0, 50), 1);
		Mockito.when(escuelaService.getEscuelas(0, 50)).thenReturn(pageEscuela);
		
		mockMvc.perform(get("/escuelas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data", notNullValue()))
				.andExpect(jsonPath("$.data.size", is(50)))
				.andExpect(jsonPath("$.data.page", is(0)))
				.andExpect(jsonPath("$.data.totalPaginas", is(1)))
				.andExpect(jsonPath("$.data.contenido").isArray())
				.andExpect(jsonPath("$.data.contenido", hasSize(2)))
				.andExpect(jsonPath("$.data.contenido[0]", notNullValue()))
				.andExpect(jsonPath("$.data.contenido[0].id", is(1)))
				.andExpect(jsonPath("$.data.contenido[0].nombre", is("escuela 1")))
				.andExpect(jsonPath("$.data.contenido[0].sectores").isArray())
				.andExpect(jsonPath("$.data.contenido[0].sectores", hasSize(2)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].id", is(1)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].nombre", is("sector 1")))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias").isArray())
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias", hasSize(2)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[0].id", is(1)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[0].nombre", is("vía 1")))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[0].id", is(1)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[0].grado", is("6a+")))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[0].longitud", is(10.0)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[0].numeroChapas", is(5)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[1].id", is(2)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[1].nombre", is("vía 2")))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[1].grado", is("6b+")))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[1].longitud", is(5.0)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[0].vias[1].numeroChapas", is(10)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[1].id", is(2)))
				.andExpect(jsonPath("$.data.contenido[0].sectores[1].nombre", is("sector 2")))
				.andExpect(jsonPath("$.data.contenido[0].sectores[1].vias").isArray())
				.andExpect(jsonPath("$.data.contenido[0].sectores[1].vias").isEmpty())
				.andExpect(jsonPath("$.data.contenido[1].id", is(2)))
				.andExpect(jsonPath("$.data.contenido[1]", notNullValue()))
				.andExpect(jsonPath("$.data.contenido[1].id", is(2)))
				.andExpect(jsonPath("$.data.contenido[1].nombre", is("escuela 2")))
				.andExpect(jsonPath("$.data.contenido[1].sectores").isArray())
				.andExpect(jsonPath("$.data.contenido[1].sectores").isEmpty());
	}
	
	/**
	 * Parámetro size menor al mínimo
	 * 
	 * @throws Exception
	 */
	//@Test TODO no funciona @Validated desde Test
	void testGetEscuelasFail1() throws Exception {
		mockMvc.perform(get("/escuelas")
					.param("size", "0"))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.status", is("FAIL")));
	}
	
	/**
	 * Parámetro size de tipo incorrecto
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetEscuelasFail2() throws Exception {
		mockMvc.perform(get("/escuelas")
					.param("size", "uno"))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.status", is("FAIL")))
				.andExpect(jsonPath("$.data", notNullValue()));
	}

	/**
	 * Llamada correcta
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetEscuela() throws Exception {
		Escuela escuela = newEscuela(1L, "nombre escuela");
		Sector sector = newSector(escuela, 2l, 0.1, 1.0, "nombre sector");
		escuela.getSectores().add(sector);
		sector.getVias().add(newVia(sector, "6a+", 3L, 20.0, "nombre vía", 10));
		Mockito.when(escuelaService.getEscuela(ArgumentMatchers.any())).thenReturn(escuela);
		mockMvc.perform(get("/escuelas/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data", notNullValue()))
				.andExpect(jsonPath("$.data.id", is(1)))
				.andExpect(jsonPath("$.data.nombre", is("nombre escuela")))
				.andExpect(jsonPath("$.data.sectores").isArray())
				.andExpect(jsonPath("$.data.sectores", hasSize(1)))
				.andExpect(jsonPath("$.data.sectores[0]", notNullValue()))
				.andExpect(jsonPath("$.data.sectores[0].id", is(2)))
				.andExpect(jsonPath("$.data.sectores[0].nombre", is("nombre sector")))
				.andExpect(jsonPath("$.data.sectores[0].longitud", is(1.0)))
				.andExpect(jsonPath("$.data.sectores[0].latitud", is(0.1)))
				.andExpect(jsonPath("$.data.sectores[0].vias").isArray())
				.andExpect(jsonPath("$.data.sectores[0].vias", hasSize(1)))				
				.andExpect(jsonPath("$.data.sectores[0].vias[0].id", is(3)))
				.andExpect(jsonPath("$.data.sectores[0].vias[0].nombre", is("nombre vía")))
				.andExpect(jsonPath("$.data.sectores[0].vias[0].longitud", is(20.0)))
				.andExpect(jsonPath("$.data.sectores[0].vias[0].grado", is("6a+")))
				.andExpect(jsonPath("$.data.sectores[0].vias[0].numeroChapas", is(10)));
	}
	
	/**
	 * id de escuela de tipo incorrecto
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetEscuelaFail1() throws Exception {
		mockMvc.perform(get("/escuelas/uno"))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.status", is("FAIL")))
				.andExpect(jsonPath("$.data", notNullValue()));
	}
	
	/**
	 * Escuela no encontrada
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetEscuelaFail2() throws Exception {
		Mockito.when(escuelaService.getEscuela(1l)).thenThrow(NoEncontradoException.class);
		mockMvc.perform(get("/escuelas/1"))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.status", is("FAIL")))
				.andExpect(jsonPath("$.data", notNullValue()));
	}
	
	/**
	 * Llamada correcta para persistir escuela
	 * 
	 * @throws Exception
	 */
	@Test
	void testAddEscuela() throws Exception {
		Escuela escuela = new Escuela();
		escuela.setId(1l);
		escuela.setNombre("nombre escuela");
		escuela.setSectores(new HashSet<>());
		Mockito.when(escuelaService.addEscuela(ArgumentMatchers.any())).thenReturn(escuela);
		mockMvc.perform(post("/escuelas")
					.content(asJsonString(newEscuelaDto(null, "nombre escuela", null)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data", notNullValue()))
				.andExpect(jsonPath("$.data.id", is(1)))
				.andExpect(jsonPath("$.data.nombre", is("nombre escuela")))
				.andExpect(jsonPath("$.data.sectores").isArray())
				.andExpect(jsonPath("$.data.sectores").isEmpty());
	}
	
	@Test
	void testAddEscuelaFail1() throws Exception {
		Mockito.when(escuelaService.addEscuela(ArgumentMatchers.any())).thenThrow(RestriccionDatosException.class);
		mockMvc.perform(post("/escuelas")
					.content(asJsonString(newEscuelaDto(null, "nombre escuela", null)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is4xxClientError())
		.andExpect(jsonPath("$.status", is("FAIL")))
		.andExpect(jsonPath("$.data", notNullValue()));
	}
	
	/**
	 * Llamada correcta para obtener los sectores de una escuela
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetSectores() throws Exception {
		HashSet<Sector> sectores = new HashSet<>();
		sectores.add(newSector(null, 1l, null, null, "sector1"));
		sectores.add(newSector(null, 2l, null, null, "sector2"));
		Mockito.when(escuelaService.getSectores(1l)).thenReturn(sectores);
		mockMvc.perform(get("/escuelas/1/sectores"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data").isArray())
				.andExpect(jsonPath("$.data", hasSize(2)))
				.andExpect(jsonPath("$.data[0].id", is(1)))
				.andExpect(jsonPath("$.data[0].nombre", is("sector1")))
				.andExpect(jsonPath("$.data[1].id", is(2)))
				.andExpect(jsonPath("$.data[1].nombre", is("sector2")));
	}
	
	/**
	 * Llamada correcta para obtener un sector pasando id
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetSector() throws Exception {
		Sector sector = newSector(null, 1l, null, null, "sector1");		
		Mockito.when(escuelaService.getSector(1l, 2l)).thenReturn(sector);
		mockMvc.perform(get("/escuelas/1/sectores/2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data.id", is(1)))
				.andExpect(jsonPath("$.data.nombre", is("sector1")));
	}

	/**
	 * Se añade nuevo sector a la escuela pasada correctamente
	 * 
	 * @throws Exception
	 */
	@Test
	void testAddSector() throws Exception {
		Sector sector = newSector(null, 1l, 0.1, 1.0, "sector 7G");
		Mockito.when(escuelaService.addSector(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(sector);
		mockMvc.perform(post("/escuelas/1/sectores")
				.content(asJsonString(newSectorDto(null, null, null, "sector 7G", null)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data.id", is(1)))
				.andExpect(jsonPath("$.data.latitud", is(0.1)))
				.andExpect(jsonPath("$.data.longitud", is(1.0)))
				.andExpect(jsonPath("$.data.nombre", is("sector 7G")));
	}
	
	/**
	 * Llamada correcta para recuperar las vías de un sector
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetVias() throws Exception {
		HashSet<Via> vias = new HashSet<>();
		vias.add(newVia(null, "5a", 1l, 5.0, "vía1", 10));
		vias.add(newVia(null, "5a", 2l, 10.0, "vía2", 5));
		Mockito.when(escuelaService.getVias(1l, 2l)).thenReturn(vias);
		mockMvc.perform(get("/escuelas/1/sectores/2/vias"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data").isArray())
				.andExpect(jsonPath("$.data", hasSize(2)))
				.andExpect(jsonPath("$.data[0].id", is(1)))
				.andExpect(jsonPath("$.data[0].nombre", is("vía1")))
				.andExpect(jsonPath("$.data[0].longitud", is(5.0)))
				.andExpect(jsonPath("$.data[0].numeroChapas", is(10)))
				.andExpect(jsonPath("$.data[1].id", is(2)))
				.andExpect(jsonPath("$.data[1].nombre", is("vía2")))
				.andExpect(jsonPath("$.data[1].longitud", is(10.0)))
				.andExpect(jsonPath("$.data[1].numeroChapas", is(5)));
	}
	
	/**
	 * Llamada correcta para recuperar la vía pasada
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetVia() throws Exception {
		Mockito.when(escuelaService.getVia(1l, 2l, 3l)).thenReturn(newVia(null, "5a", 1l, 10.0, "vía1", 5));
		mockMvc.perform(get("/escuelas/1/sectores/2/vias/3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data.id", is(1)))
				.andExpect(jsonPath("$.data.nombre", is("vía1")))
				.andExpect(jsonPath("$.data.grado", is("5a")))
				.andExpect(jsonPath("$.data.longitud", is(10.0)))
				.andExpect(jsonPath("$.data.numeroChapas", is(5)));
	}
	
	@Test
	void testAddVia() throws Exception {
		Mockito
		.when(escuelaService.addVia(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(),	ArgumentMatchers.any()))
		.thenReturn(newVia(null, "5a", 1l, null, "vía1", 7));
		mockMvc.perform(post("/escuelas/1/sectores/2/vias")
					.content(asJsonString(newViaDto("5a", null, null, "vía1", null)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status", is("SUCCESS")))
				.andExpect(jsonPath("$.data.id", is(1)))
				.andExpect(jsonPath("$.data.nombre", is("vía1")))
				.andExpect(jsonPath("$.data.grado", is("5a")))
				.andExpect(jsonPath("$.data.numeroChapas", is(7)));	
	}
	
	private Object newViaDto(String grado, Long id, Double longitud, String nombre, Integer numeroChapas) {
		ViaDto viaDto = new ViaDto();
		viaDto.setGrado(grado);
		viaDto.setId(id);
		viaDto.setLongitud(longitud);
		viaDto.setNombre(nombre);
		viaDto.setNumeroChapas(numeroChapas);
		return viaDto;
	}

	private SectorDto newSectorDto(Long id, Double latitud, Double longitud, String nombre, List<ViaDto> vias) {
		SectorDto sectorDto = new SectorDto();
		sectorDto.setId(id);
		sectorDto.setLatitud(latitud);
		sectorDto.setLongitud(longitud);
		sectorDto.setNombre(nombre);
		sectorDto.setVias(vias);
		return sectorDto;
	}

	private EscuelaDto newEscuelaDto(Long id, String nombre, List<SectorDto> sectores) {
		EscuelaDto escuelaDto = new EscuelaDto();
		escuelaDto.setId(id);
		escuelaDto.setNombre("nombre escuela");
		escuelaDto.setSectores(sectores);
		return escuelaDto;
	}

	private Via newVia(Sector sector, String grado, Long id, Double longitud, String nombre, Integer numeroChapas) {
		Via via = new Via();
		via.setGrado(grado);
		via.setId(id);
		via.setLongitud(longitud);
		via.setNombre(nombre);
		via.setNumeroChapas(numeroChapas);
		via.setSector(sector);
		return via;
	}

	private Escuela newEscuela(Long id, String nombre) {
		Escuela escuela = new Escuela();
		escuela.setId(id);
		escuela.setNombre(nombre);
		escuela.setSectores(new HashSet<>());
		return escuela;
	}

	private Sector newSector(Escuela escuela, Long id, Double latitud, Double longitud, String nombre) {
		Sector sector = new Sector();
		sector.setEscuela(escuela);
		sector.setId(id);
		sector.setLatitud(latitud);
		sector.setLongitud(longitud);
		sector.setNombre(nombre);
		sector.setVias(new HashSet<>());
		return sector;
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}

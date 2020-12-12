package es.uniovi.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDomain {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testEscuela() {
		Escuela escuela1 = new Escuela();
		escuela1.setId(1L);
		Escuela escuela2 = new Escuela();
		escuela2.setId(2L);
		Escuela escuela3 = new Escuela();
		escuela3.setId(1L);
		escuela1.setNombre("nombre");
		escuela1.setSectores(new ArrayList<>());
		assertEquals(1L, escuela1.getId());
		assertEquals("nombre", escuela1.getNombre());
		assertEquals(new ArrayList<Sector>(), escuela1.getSectores());
		assertNotEquals(escuela1, escuela2);
		assertEquals(escuela1, escuela3);
		assertNotNull(escuela1.toString());
		assertNotEquals(new Escuela(), escuela1);
		assertFalse(escuela1.equals(null));
		assertFalse(escuela1.equals(""));
		assertTrue(escuela1.equals(escuela1));
		assertNotNull(escuela1.hashCode());
	}

	@Test
	void testSector() {
		Sector sector = new Sector();
		sector.setEscuela(new Escuela());
		sector.setId(1l);
		sector.setLatitud(0.0);
		sector.setLongitud(0.0);
		sector.setNombre("nombre");
		sector.setVias(new ArrayList<>());
		assertEquals(1L, sector.getId());
		assertEquals(new Escuela(), sector.getEscuela());
		assertEquals(0.0, sector.getLatitud());
		assertEquals(0.0, sector.getLongitud());
		assertEquals("nombre", sector.getNombre());
		assertEquals(new ArrayList<>(), sector.getVias());
		assertNotEquals(new Sector(), sector);
		assertNotNull(sector.toString());
		assertNotNull(sector.hashCode());
	}

	@Test
	void testVia() {
		Via via = new Via();
		via.setGrado("grado");
		via.setId(1l);
		via.setLongitud(1.0);
		via.setNombre("nombre");
		via.setNumeroChapas(1);
		via.setSector(new Sector());
		assertEquals("grado", via.getGrado());
		assertEquals(1l, via.getId());
		assertEquals(1.0, via.getLongitud());
		assertEquals("nombre", via.getNombre());
		assertEquals(1, via.getNumeroChapas());
		assertEquals(new Sector(), via.getSector());
		assertNotNull(via.toString());
		assertNotNull(via.hashCode());
		assertNotEquals(new Via(), via);
	}

}

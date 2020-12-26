package es.uniovi.escuelas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@ActiveProfiles("test")
class ApiEscuelasDeEscaladaApplicationTests {

	@Test
	void contextLoads() {		
		assertTrue(true);
	}

}

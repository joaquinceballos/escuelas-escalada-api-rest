package es.uniovi.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public InitializingBean initializingBean(@Autowired EntityManagerFactory entityManagerFactory) {
		return new InitializingBean() {

			@Override
			public void afterPropertiesSet() throws Exception {
				EntityManager entityManager = entityManagerFactory.createEntityManager();
				FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
				fullTextEntityManager.createIndexer().startAndWait();
			}

		};
	}

}

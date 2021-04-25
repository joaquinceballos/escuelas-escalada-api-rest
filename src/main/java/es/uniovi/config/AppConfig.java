package es.uniovi.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("es.uniovi.controller"))
				.build();
	}

	@Bean
	public LocalValidatorFactoryBean getValidator(@Autowired MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}

}

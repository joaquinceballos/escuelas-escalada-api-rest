package es.uniovi.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.lucene.search.Query;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;
import es.uniovi.service.BuscadorService;

@Service
public class BuscadorServiceImpl implements BuscadorService {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Override
	public Page<Escuela> buscaEscuelas(String texto, Integer page, Integer size) {
		return paginaResultados(texto, page, size, Escuela.class, "nombre", "informacion");
	}

	@Override
	public Page<Sector> buscaSectores(String texto, Integer page, Integer size) {
		return paginaResultados(texto, page, size, Sector.class, "nombre");
	}

	@Override
	public Page<Via> buscaVias(String texto, Integer page, Integer size) {
		return paginaResultados(texto, page, size, Via.class, "nombre");
	}
	
	private <T> Page<T> paginaResultados(
			String texto,
			Integer page,
			Integer size,
			Class<T> className,
			String... campos) {
		Page<T> pagina = null;

		EntityManager em = entityManagerFactory.createEntityManager();
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		try {
			QueryBuilder queryBuilder = fullTextEntityManager
					.getSearchFactory()
					.buildQueryBuilder()
					.forEntity(className)
					.get();
			Query query = queryBuilder
					.keyword()
					.onFields(campos)
					.matching(texto)
					.createQuery();
			FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(query, className);
			persistenceQuery.setFirstResult(page * size);
			persistenceQuery.setMaxResults(size);
			@SuppressWarnings("unchecked")
			List<T> result = persistenceQuery.getResultList();
			int total = persistenceQuery.getResultSize();
			pagina = new PageImpl<>(result, PageRequest.of(page, size), total);
		} catch (SearchException e) {
			pagina = Page.empty();
		}

		em.getTransaction().commit();
		em.close();
		return pagina;
	}
}

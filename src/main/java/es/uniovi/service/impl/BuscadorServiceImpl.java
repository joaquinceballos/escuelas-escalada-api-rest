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
import es.uniovi.service.BuscadorService;

@Service
public class BuscadorServiceImpl implements BuscadorService {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Override
	public Page<Escuela> getEscuelas(String nombre, Integer page, Integer size) {

		Page<Escuela> escuelas = null;

		EntityManager em = entityManagerFactory.createEntityManager();
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		try {
			Query query = createLuceneQuery(nombre, fullTextEntityManager);
			FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(query, Escuela.class);
			persistenceQuery.setFirstResult(page *  size);
			persistenceQuery.setMaxResults(size);
			@SuppressWarnings("unchecked")
			List<Escuela> result = persistenceQuery.getResultList();
			int total = persistenceQuery.getResultSize();
			escuelas = new PageImpl<>(result, PageRequest.of(page, size), total);
		} catch (SearchException e) {
			escuelas = Page.empty();
		}
		em.getTransaction().commit();
		em.close();
		return escuelas;
	}

	private Query createLuceneQuery(String nombre, FullTextEntityManager fullTextEntityManager) {
		QueryBuilder qb = fullTextEntityManager
				.getSearchFactory()
				.buildQueryBuilder()
				.forEntity(Escuela.class)
				.get();
				
		return qb
				.keyword()
				.onFields("nombre")
				.matching(nombre)
				.createQuery();
	}

}

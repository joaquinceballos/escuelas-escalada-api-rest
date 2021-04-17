package es.uniovi.repository;

import java.util.EnumSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import es.uniovi.domain.LogModificaciones;
import es.uniovi.domain.LogModificaciones.TipoRecurso;

public interface LogModificacionesRepository extends PagingAndSortingRepository<LogModificaciones, Long> {

	@Query(
			"select m " + 
			"from logModificaciones m " +
			"where m.tipoRecurso in :tipos " +
			"and (:idRecurso is null or m.idRecurso = :idRecurso) " +
			"and (:idUsuario is null or m.usuario.id = :idUsuario) " +
			"order by fecha desc")
	Page<LogModificaciones> findModificacionesPublicas(
			EnumSet<TipoRecurso> tipos,
			Long idRecurso,
			Long idUsuario,
			Pageable pageable);

}

package es.uniovi.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.fge.jsonpatch.JsonPatch;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.api.ListaPaginada;
import es.uniovi.domain.Ascension;
import es.uniovi.domain.Croquis;
import es.uniovi.domain.TipoLeyenda;
import es.uniovi.domain.TrazoVia;
import es.uniovi.domain.Via;
import es.uniovi.dto.AscensionDto;
import es.uniovi.dto.CierreTemporadaDto;
import es.uniovi.dto.CroquisDto;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.dto.SectorDto;
import es.uniovi.dto.SectorRootDto;
import es.uniovi.dto.TrazoViaDto;
import es.uniovi.dto.ViaDto;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.ServiceException;
import es.uniovi.service.EscuelaService;
import es.uniovi.validation.ValueOfEnum;

@Validated
@RestController
@RequestMapping("escuelas")
public class EscuelaController extends BaseController {

	@Autowired
	private EscuelaService escuelaService;
	
	/**
	 * Retorna una lista paginada de escuelas
	 * 
	 * @param page La página
	 * @param size El tamaño de la página
	 * @return Response con la lista paginada de escuelas
	 * @throws NoEncontradoException 
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<EscuelaDto>> getEscuelas(
			@RequestParam(name = "idZona", required = false)  Long idZona,
			@RequestParam(name = "page", defaultValue = "0", required = false) @Min(0) Integer page,
			@RequestParam(name = "size", defaultValue = "50", required = false) @Min(1) @Max(100) Integer size) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.getEscuelas(page, size, idZona)), ApiResponseStatus.SUCCESS);
	}

	/**
	 * Retorna la escuela cuya id es pasada
	 * 
	 * @param id La id de la escuela
	 * @return Response conteniendo la escula
	 * @throws ServiceException
	 */
	@GetMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<EscuelaDto> getEscuela(@PathVariable(name = "id") @NotNull Long id) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.getEscuela(id)), ApiResponseStatus.SUCCESS);
	}

	/**
	 * Persiste y retorna la escuela de escalada pasada
	 * 
	 * @param escuelaDto DTO de la escuela a persistir
	 * @return Response con la escuela persistida
	 * @throws ServiceException
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<EscuelaDto> addEscuela(@Valid @RequestBody EscuelaDto escuelaDto) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.addEscuela(toEntity(escuelaDto))), ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Modifica la escuela cuya id es pasada
	 * 
	 * @param id        La id de la escuela a cambiar
	 * @param jsonPatch Los cambios a aplicar en la escuela
	 * @return Response con la escuela actualizada
	 * @throws ServiceException
	 */
	@PatchMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<EscuelaDto> patchEscuela(
			@PathVariable(name = "id") @NotNull Long id,
			@RequestBody JsonPatch jsonPatch) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.actualizaEscuela(id, jsonPatch)), ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Actualiza la escuela pasada
	 * 
	 * @param id         La id de la escuela
	 * @param escuelaDto El dto de la escuela a actualizar
	 * @return Response con la escuela actualizada
	 * @throws ServiceException
	 */
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<EscuelaDto> putEscuela(
			@PathVariable(name = "id") @NotNull Long id,
			@RequestBody @Valid EscuelaDto escuelaDto) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.actualizaEscuela(id, toEntity(escuelaDto))), ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Elimina la escuela cuya id es pasada en el path
	 * 
	 * @param id La id de la escuela a borrar
	 * @return Response con el resultado de la operación
	 * @throws ServiceException
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<Void> deleteEscuela(@PathVariable("id") @NotNull Long id) throws ServiceException {
		escuelaService.deleteEscuela(id);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);
	}

	/**
	 * Retorna los sectores de la escuela cuya id es pasada
	 * 
	 * @param id La id de la escuela
	 * @return Response con la lista de sectores solicitada
	 * @throws ServiceException
	 */
	@GetMapping("/{id}/sectores")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<List<SectorDto>> getSectores(@PathVariable(name = "id") @NotNull Long id)
			throws ServiceException {
		return new ApiResponse<>(toSectoresDto(escuelaService.getSectores(id)), ApiResponseStatus.SUCCESS);
	}

	/**
	 * Retorna el sector cuya id es pasado+
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @return Response con el sector solicitado
	 * @throws ServiceException
	 */
	@GetMapping("/{idEscuela}/sectores/{idSector}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<SectorRootDto> getSector(
			@PathVariable(name = "idEscuela") @NotNull Long idEscuela,
			@PathVariable(name = "idSector") @NotNull Long idSector) throws ServiceException {
		return new ApiResponse<>(toRootDto(escuelaService.getSector(idEscuela, idSector)), ApiResponseStatus.SUCCESS);
	}

	/**
	 * Persiste el sector cuyo DTO es pasado
	 * 
	 * @param id        La id de la escuela
	 * @param sectorDto El sector a persistir
	 * @return Response conteniendo el sector persistido
	 * @throws ServiceException
	 */
	@PostMapping("/{id}/sectores")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<SectorDto> addSector(
			@PathVariable(name = "id") @NotNull Long id,
			@Valid @RequestBody SectorDto sectorDto) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.addSector(id, toEntity(sectorDto))), ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Actualiza el sector pasado
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param sectorDto El dto del sector a actualizar
	 * @return Response con el sector actualizado
	 * @throws ServiceException
	 */
	@PutMapping("/{idEscuela}/sectores/{idSector}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<SectorDto> putSector(
			@PathVariable(name = "idEscuela") @NotNull Long idEscuela,
			@PathVariable(name = "idSector") @NotNull Long idSector,
			@Valid @RequestBody SectorDto sectorDto) throws ServiceException {
		SectorDto data = toDto(escuelaService.actualizaSector(idEscuela, idSector, toEntity(sectorDto)));
		return new ApiResponse<>(data, ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Elimina el sector cuya id es pasado en el path
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @return Response con el resultado de la operación
	 * @throws ServiceException
	 */
	@DeleteMapping("/{idEscuela}/sectores/{idSector}")
	@ResponseStatus(code= HttpStatus.OK)
	public ApiResponse<Void> deleteSector(
			@PathVariable("idEscuela") @NotNull Long idEscuela,
			@PathVariable("idSector") @NotNull Long idSector) throws ServiceException {
		escuelaService.deleteSector(idEscuela, idSector);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);
	}
	
	@GetMapping("/{idEscuela}/sectores/{idSector}/croquis")
	@ResponseStatus(code= HttpStatus.OK)
	public ApiResponse<List<CroquisDto>> getCroquis(
			@PathVariable("idEscuela") @NotNull Long idEscuela,
			@PathVariable("idSector") @NotNull Long idSector) throws ServiceException {
		List<CroquisDto> croquis = toCroquisDto(escuelaService.getCroquis(idEscuela, idSector));
		return new ApiResponse<>(croquis, ApiResponseStatus.SUCCESS);
	}
	
	@GetMapping("/{idEscuela}/sectores/{idSector}/croquis/{idCroquis}")
	@ResponseStatus(code= HttpStatus.OK)
	public ApiResponse<CroquisDto> getCroquis(
			@PathVariable("idEscuela") @NotNull Long idEscuela,
			@PathVariable("idSector") @NotNull Long idSector,
			@PathVariable("idCroquis") @NotNull Long idCroquis) throws ServiceException {
		CroquisDto croquis = toDto(escuelaService.getCroquis(idEscuela, idSector, idCroquis));
		return new ApiResponse<>(croquis, ApiResponseStatus.SUCCESS);
	}	

	@PostMapping("/{idEscuela}/sectores/{idSector}/croquis")
	@ResponseStatus(code= HttpStatus.OK)
	public ApiResponse<CroquisDto> addCroquis(
			@PathVariable("idEscuela") @NotNull Long idEscuela,
			@PathVariable("idSector") @NotNull Long idSector,
			@RequestBody @Valid CroquisDto croquisDto) throws ServiceException {
		Croquis croquis = escuelaService.addCroquis(idEscuela, idSector, toEntity(croquisDto));
		return new ApiResponse<>(toDto(croquis), ApiResponseStatus.SUCCESS);
	}

	/**
	 * Elimina el croquis cuya id es pasasda
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idCroquis La id del croquis
	 * @return Response con el resultado de la operación
	 * @throws ServiceException
	 */
	@DeleteMapping("/{idEscuela}/sectores/{idSector}/croquis/{idCroquis}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<Void> deleteCroquis(
			@PathVariable(name = "idEscuela") @NotNull Long idEscuela,
			@PathVariable(name = "idSector") @NotNull Long idSector,
			@PathVariable(name = "idCroquis") @NotNull Long idCroquis) throws ServiceException {
		escuelaService.deleteCroquis(idEscuela, idSector, idCroquis);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);
	}
	
	@PutMapping("/{idEscuela}/sectores/{idSector}/croquis/{idCroquis}/leyenda")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<CroquisDto> updateTipoLeyenda(
			@PathVariable(name = "idEscuela")
			@NotNull
			Long idEscuela,
			@PathVariable(name = "idSector")
			@NotNull
			Long idSector,
			@PathVariable(name = "idCroquis")
			@NotNull
			Long idCroquis,
			@RequestParam(name = "tipoLeyenda", required = true)
			@ValueOfEnum(enumClass = TipoLeyenda.class)
			String tipoLeyenda) throws ServiceException {
		escuelaService.actualizaTipoLeyenda(idEscuela, idSector, idCroquis, tipoLeyenda);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);
	}
	
	@PostMapping("/{idEscuela}/sectores/{idSector}/croquis/{idCroquis}/via/{idVia}")
	@ResponseStatus(code= HttpStatus.OK)
	public ApiResponse<TrazoViaDto> addTrazoVia(
			@PathVariable("idEscuela") @NotNull Long idEscuela,
			@PathVariable("idSector") @NotNull Long idSector,
			@PathVariable("idCroquis") @NotNull Long idCroquis,
			@PathVariable("idVia") @NotNull Long idVia,
			@RequestBody @Valid TrazoViaDto trazoViaDto) throws ServiceException {
		TrazoVia trazoVia = escuelaService.addTrazoVia(idEscuela, idSector, idCroquis, idVia, toEntity(trazoViaDto));
		return new ApiResponse<>(toDto(trazoVia), ApiResponseStatus.SUCCESS);
	}

	@PutMapping("/{idEscuela}/sectores/{idSector}/croquis/{idCroquis}/via/{idVia}")
	@ResponseStatus(code= HttpStatus.OK)
	public ApiResponse<TrazoViaDto> updateTrazoVia(
			@PathVariable("idEscuela") @NotNull Long idEscuela,
			@PathVariable("idSector") @NotNull Long idSector,
			@PathVariable("idCroquis") @NotNull Long idCroquis,
			@PathVariable("idVia") @NotNull Long idVia,
			@RequestBody @Valid TrazoViaDto trazoViaDto) throws ServiceException {
		TrazoVia trazoVia = escuelaService.updateTrazoVia(
				idEscuela,
				idSector,
				idCroquis,
				idVia,
				toEntity(trazoViaDto));
		return new ApiResponse<>(toDto(trazoVia), ApiResponseStatus.SUCCESS);
	}
	
	@DeleteMapping("/{idEscuela}/sectores/{idSector}/croquis/{idCroquis}/via/{idVia}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<Void> deleteTrazoVia(
			@PathVariable("idEscuela") @NotNull Long idEscuela,
			@PathVariable("idSector") @NotNull Long idSector,
			@PathVariable("idCroquis") @NotNull Long idCroquis,
			@PathVariable("idVia") @NotNull Long idVia) throws ServiceException {
		escuelaService.deleteTrazoVia(idEscuela, idSector, idCroquis, idVia);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Retorna la lista de vías del sector pasado
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @return La lista de vías
	 * @throws ServiceException
	 */
	@GetMapping("/{idEscuela}/sectores/{idSector}/vias")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<List<ViaDto>> getVias(
			@PathVariable(name = "idEscuela") Long idEscuela,
			@PathVariable(name = "idSector") Long idSector) throws ServiceException {
		return new ApiResponse<>(toViasDto(escuelaService.getVias(idEscuela, idSector)), ApiResponseStatus.SUCCESS);
	}	

	/**
	 * Retorna la vía cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @return Response conteniendo la vía solicitada
	 * @throws ServiceException
	 */
	@GetMapping("/{idEscuela}/sectores/{idSector}/vias/{idVia}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ViaDto> getVia(
			@PathVariable(name = "idEscuela") Long idEscuela,
			@PathVariable(name = "idSector") Long idSector,
			@PathVariable(name = "idVia") Long idVia) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.getVia(idEscuela, idSector, idVia)), ApiResponseStatus.SUCCESS);
	}

	/**
	 * Persiste nueva vía
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param viaDto    DTO de la vía a persistir
	 * @return La vía persistida
	 * @throws ServiceException
	 */
	@PostMapping("/{idEscuela}/sectores/{idSector}/vias")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<ViaDto> addVia(
			@PathVariable(name = "idEscuela") Long idEscuela,
			@PathVariable(name = "idSector") Long idSector,
			@RequestBody @Valid ViaDto viaDto) throws ServiceException {
		Via nuevaVia = escuelaService.addVia(idEscuela, idSector, toEntity(viaDto));
		return new ApiResponse<>(toDto(nuevaVia), ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Actualiza la vía pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @param viaDto    el dto de la vía con los campos a actualizar
	 * @return Response con la vía actualizada
	 * @throws ServiceException
	 */
	@PutMapping("/{idEscuela}/sectores/{idSector}/vias/{idVia}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ViaDto> putVia(
	@PathVariable(name = "idEscuela") @NotNull Long idEscuela,
	@PathVariable(name = "idSector") @NotNull Long idSector,
	@PathVariable(name = "idVia") @NotNull Long idVia,
	@RequestBody @Valid ViaDto viaDto) throws ServiceException {
		Via nuevaVia = escuelaService.actualizaVia(idEscuela, idSector, idVia, toEntity(viaDto));
		return new ApiResponse<>(toDto(nuevaVia), ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Elimina la vía cuya id es pasada en el path
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @return Response con el resultado de la operación
	 * @throws ServiceException
	 */
	@DeleteMapping("/{idEscuela}/sectores/{idSector}/vias/{idVia}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<Void> deleteVia(
			@PathVariable(name = "idEscuela") @NotNull Long idEscuela,
			@PathVariable(name = "idSector") @NotNull Long idSector,
			@PathVariable(name = "idVia") @NotNull Long idVia) throws ServiceException {
		escuelaService.deleteVia(idEscuela, idSector, idVia);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);		
	}
	
	@GetMapping("/{idEscuela}/sectores/{idSector}/vias/{idVia}/ascenciones")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<AscensionDto>> getAscencionesVia(
			@PathVariable(name = "idEscuela") @NotNull Long idEscuela,
			@PathVariable(name = "idSector") @NotNull Long idSector,
			@PathVariable(name = "idVia") @NotNull Long idVia,
			Pageable pageable) throws ServiceException {
		Page<Ascension> pageAscensiones = escuelaService.getAscencionesVia(idEscuela, idSector, idVia, pageable);
		return new ApiResponse<>(pageAscensionToDto(pageAscensiones), ApiResponseStatus.SUCCESS);
	}

	/**
	 * Añade nuevo cierre de temporada a la escuela pasada en el path
	 * 
	 * @param id  El id de la escuela
	 * @param dto El nuevo cierre de temporada
	 * @return Response con el cierre persistido
	 * @throws ServiceException
	 */
	@PostMapping("/{id}/cierres")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<CierreTemporadaDto> addCierreTemporada(
			@PathVariable("id") Long id,
			@Valid @RequestBody CierreTemporadaDto dto) throws ServiceException {
		return new ApiResponse<>(
				toDto(escuelaService.addCierreTemporada(id, toEntity(dto))),
				ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Elimina el cierre de temporada cuya id es pasado en el path
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del cierre de temporada
	 * @return Response con el resultado de la operación
	 * @throws ServiceException
	 */	
	@DeleteMapping("/{idEscuela}/cierres/{idCierre}")
	@ResponseStatus(code= HttpStatus.OK)	
	public ApiResponse<Void> deleteCierreTemporada(
			@PathVariable("idEscuela") @NotNull Long idEscuela,
			@PathVariable("idCierre") @NotNull Long idCierre) throws ServiceException {
		escuelaService.deleteCierre(idEscuela, idCierre);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);
	}
	
}

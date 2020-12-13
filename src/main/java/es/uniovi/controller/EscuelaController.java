package es.uniovi.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.api.ListaPaginada;
import es.uniovi.domain.Via;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.dto.SectorDto;
import es.uniovi.dto.ViaDto;
import es.uniovi.exception.ServiceException;
import es.uniovi.service.EscuelaService;

@RestController
@RequestMapping("escuela")
public class EscuelaController extends BaseController {

	@Autowired
	private EscuelaService escuelaService;
	
	@GetMapping()
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<EscuelaDto>> getEscuelas(
			@RequestParam(name = "page", defaultValue = "0", required = false) @Min(1) Integer page,
			@RequestParam(name = "size", defaultValue = "50", required = false) @Min(1) @Max(100) Integer size) {
		return new ApiResponse<>(toDto(escuelaService.getEscuelas(page, size)), ApiResponseStatus.SUCCESS);
	}

	@GetMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<EscuelaDto> getEscuela(@PathVariable(name = "id") @NotNull Long id) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.getEscuela(id)), ApiResponseStatus.SUCCESS);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<EscuelaDto> addEscuela(@Valid @RequestBody EscuelaDto escuelaDto) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.addEscuela(toEntity(escuelaDto))), ApiResponseStatus.SUCCESS);
	}

	@GetMapping("/{id}/sector")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<List<SectorDto>> getSectores(
			@PathVariable(name = "id") @NotNull Long id) throws ServiceException {
		return new ApiResponse<>(toSectoresDto(escuelaService.getSectores(id)), ApiResponseStatus.SUCCESS);
	}

	@PostMapping("/{id}/sector")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<SectorDto> addSector(
			@PathVariable(name = "id") @NotNull Long id,
	        @Valid @RequestBody SectorDto sectorDto) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.addSector(id, toEntity(sectorDto))), ApiResponseStatus.SUCCESS);
	}
	
	@GetMapping("/{idEscuela}/sector/{idSector}/via")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<List<ViaDto>> getVias(
			@PathVariable(name = "idEscuela") Long idEscuela,
			@PathVariable(name = "idSector") Long idSector) throws ServiceException {
		return new ApiResponse<>(toViasDto(escuelaService.getVias(idEscuela, idSector)), ApiResponseStatus.SUCCESS);
	}
	
	@GetMapping("/{idEscuela}/sector/{idSector}/via/{idVia}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ViaDto> getVia(
			@PathVariable(name = "idEscuela") Long idEscuela,
			@PathVariable(name = "idSector") Long idSector,
			@PathVariable(name = "idVia") Long idVia) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.getVia(idEscuela, idSector, idVia)), ApiResponseStatus.SUCCESS);
	}

	@PostMapping("/{idEscuela}/sector/{idSector}/via")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<ViaDto> addVia(
			@PathVariable(name = "idEscuela") Long idEscuela,
			@PathVariable(name = "idSector") Long idSector,
			@RequestBody @Valid ViaDto viaDto) throws ServiceException {
		Via nuevaVia = escuelaService.addVia(idEscuela, idSector, toEntity(viaDto));
		return new ApiResponse<>(toDto(nuevaVia), ApiResponseStatus.SUCCESS);
	}

}

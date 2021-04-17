package es.uniovi.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.api.ListaPaginada;
import es.uniovi.dto.ZonaDto;
import es.uniovi.exception.NoAutorizadoException;
import es.uniovi.exception.ServiceException;
import es.uniovi.filtro.FiltroZonas;
import es.uniovi.service.ZonaService;

@Validated
@RestController
@RequestMapping("zonas")
public class ZonaController extends BaseController {
	
	@Autowired
	private ZonaService zonaService;
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<ZonaDto>> getZonas(
			@Valid FiltroZonas filtro,
			Pageable pageable) throws NoAutorizadoException {
		return new ApiResponse<>(pageZonasToDto(zonaService.getZonas(pageable, filtro)), ApiResponseStatus.SUCCESS);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ZonaDto> getZona(@PathVariable(name = "id") @NotNull Long id) throws ServiceException {
		return new ApiResponse<>(toDto(zonaService.getZona(id)), ApiResponseStatus.SUCCESS);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<ZonaDto> addZona(@Valid @RequestBody ZonaDto zonaDto) throws ServiceException {
		return new ApiResponse<>(toDto(zonaService.addZona(toEntity(zonaDto))), ApiResponseStatus.SUCCESS);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ZonaDto> putZona(
			@PathVariable(name = "id") @NotNull Long id,
			@RequestBody @Valid ZonaDto zonaDto) throws ServiceException {
		return new ApiResponse<>(toDto(zonaService.actualizaZona(id, toEntity(zonaDto))), ApiResponseStatus.SUCCESS);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<Void> deleteZona(@PathVariable("id") @NotNull Long id) throws ServiceException {
		zonaService.deleteZona(id);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);
	}
	
}

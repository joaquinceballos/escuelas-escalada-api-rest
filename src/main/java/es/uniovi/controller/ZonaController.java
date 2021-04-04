package es.uniovi.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.api.ListaPaginada;
import es.uniovi.dto.ZonaDto;
import es.uniovi.exception.ServiceException;
import es.uniovi.service.ZonaService;

@RestController
@RequestMapping("zonas")
public class ZonaController extends BaseController {
	
	@Autowired
	private ZonaService zonaService;
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<ZonaDto>> getZonas(
			@RequestParam(name = "page", defaultValue = "0", required = false) @Min(0) Integer page,
			@RequestParam(name = "size", defaultValue = "50", required = false) @Min(1) @Max(100) Integer size) {
		return new ApiResponse<>(pageZonasToDto(zonaService.getZonas(page, size)), ApiResponseStatus.SUCCESS);
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

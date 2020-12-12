package es.uniovi.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.dto.SectorDto;
import es.uniovi.exception.ServiceException;
import es.uniovi.service.EscuelaService;

@RestController
@RequestMapping("escuela")
public class EscuelaController extends BaseController {

	@Autowired
	private EscuelaService escuelaService;

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
		return new ApiResponse<>(toDto(escuelaService.getSectores(id)), ApiResponseStatus.SUCCESS);
	}

	@PostMapping("/{id}/sector")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<SectorDto> addSector(
			@PathVariable(name = "id") @NotNull Long id,
	        @Valid @RequestBody SectorDto sectorDto) throws ServiceException {
		return new ApiResponse<>(toDto(escuelaService.addSector(id, toEntity(sectorDto))), ApiResponseStatus.SUCCESS);
	}

}

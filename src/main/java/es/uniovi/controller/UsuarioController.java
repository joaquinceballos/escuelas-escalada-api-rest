package es.uniovi.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
import es.uniovi.dto.AscensionDto;
import es.uniovi.dto.UsuarioDto;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.service.UsuarioService;

@Validated
@RestController
@RequestMapping("usuarios")
public class UsuarioController extends BaseController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<UsuarioDto>> getUsuarios(
			@RequestParam(name = "page", defaultValue = "0", required = false) @Min(0) Integer page,
			@RequestParam(name = "size", defaultValue = "50", required = false) @Min(1) @Max(100) Integer size){
		return new ApiResponse<>(pageUsuarioToDto(usuarioService.getUsuarios(page, size)), ApiResponseStatus.SUCCESS);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<UsuarioDto> addUsuario(
			@RequestBody @Valid UsuarioDto usuarioDto) throws RestriccionDatosException {
		return new ApiResponse<>(toDto(usuarioService.addUsuario(toEntity(usuarioDto))), ApiResponseStatus.SUCCESS);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<UsuarioDto> updateUsuario(@PathVariable Long id,
			                                     @RequestBody @Valid UsuarioDto usuarioDto) throws ServiceException {
		usuarioDto.setId(id); //En caso de que no se haya informado en la request body o sea distinta...
		return new ApiResponse<>(toDto(usuarioService.updateUsuario(toEntity(usuarioDto))), ApiResponseStatus.SUCCESS);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public ApiResponse<Void> deleteUsuario(@PathVariable Long id) throws NoEncontradoException {
		usuarioService.deleteUsuario(id);
		return new ApiResponse<>(null, ApiResponseStatus.SUCCESS);
	}
	
	/**
	 * Retorna una lista paginada de ascensiones
	 * 
	 * @param page La página
	 * @param size El tamaño de la página
	 * @return Response con la lista paginada de ascensiones
	 * @throws NoEncontradoException 
	 */
	@GetMapping("/{id}/ascensiones")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<AscensionDto>> getAscensiones(
			@RequestParam(name = "page", defaultValue = "0", required = false) @Min(0) Integer page,
			@RequestParam(name = "size", defaultValue = "50", required = false) @Min(1) @Max(100) Integer size,
			@PathVariable(name = "id") Long id) throws NoEncontradoException {
		ListaPaginada<AscensionDto> pageAscension = pageAscensionToDto(usuarioService.getAscensiones(id, page, size));
		return new ApiResponse<>(pageAscension, ApiResponseStatus.SUCCESS);
	}

	/**
	 * Registra una nueva ascensión
	 * 
	 * @param ascensionDto La ascensión
	 * @param idUsuario    La id del usuario
	 * @param idVia        La id de la vía
	 * @return La ascensión persistida
	 * @throws NoEncontradoException
	 */
	@PostMapping("/{idUsuario}/ascensiones/{idVia}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<AscensionDto> addAscension(
			@RequestBody @Valid AscensionDto ascensionDto,
			@PathVariable(name = "idUsuario") Long idUsuario,
			@PathVariable(name = "idVia") Long idVia) throws NoEncontradoException {
		return new ApiResponse<>(toDto(usuarioService.addAscension(idUsuario, idVia, toEntity(ascensionDto))), ApiResponseStatus.SUCCESS);
	}

	/**
	 * Actualiza los campos de una ascesión existente
	 * 
	 * @param ascensionDto La ascensión
	 * @param idUsuario    La id del usuario
	 * @param idVia        La id de la vía
	 * @return La ascensión actualizada
	 * @throws NoEncontradoException
	 */
	@PutMapping("/{idUsuario}/ascensiones/{idVia}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<AscensionDto> updateAscension(
			@RequestBody @Valid AscensionDto ascensionDto,
			@PathVariable(name = "idUsuario") Long idUsuario,
			@PathVariable(name = "idVia") Long idVia) throws NoEncontradoException {
		return new ApiResponse<>(toDto(usuarioService.updateAscension(idUsuario, idVia, toEntity(ascensionDto))), ApiResponseStatus.SUCCESS);
	}
}

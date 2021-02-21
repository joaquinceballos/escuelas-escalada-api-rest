package es.uniovi.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.api.ListaPaginada;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.service.BuscadorService;

@RestController
@RequestMapping("buscador")
public class BuscadorController extends BaseController {

	@Autowired
	private BuscadorService buscadorService;

	/**
	 * Retorna una lista paginada de escuelas
	 * 
	 * @param page La página
	 * @param size El tamaño de la página
	 * @para nombre El nombre de la escuela
	 * @return Response con la lista paginada de escuelas
	 */
	@GetMapping("escuelas")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<EscuelaDto>> getEscuelas(
			@RequestParam(name = "page", defaultValue = "0", required = false) @Min(0) Integer page,
			@RequestParam(name = "size", defaultValue = "50", required = false) @Min(1) @Max(100) Integer size,
			@RequestParam(name = "nombre", required = true) String nombre) {
		return new ApiResponse<>(toDto(buscadorService.getEscuelas(nombre, page, size)), ApiResponseStatus.SUCCESS);
	}

}

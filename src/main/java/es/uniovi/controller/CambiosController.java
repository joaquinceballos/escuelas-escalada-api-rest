package es.uniovi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.api.ListaPaginada;
import es.uniovi.dto.LogModificacionesDto;
import es.uniovi.filtro.FiltroCambios;
import es.uniovi.service.LogModificacionesService;

@Validated
@RestController
@RequestMapping("cambios")
public class CambiosController extends BaseController {
	
	@Autowired
	private LogModificacionesService modificacionesService;
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public ApiResponse<ListaPaginada<LogModificacionesDto>> getZonas(
			@Valid FiltroCambios filtro,
			Pageable pageable) {
		return new ApiResponse<>(pageCambiosToDto(modificacionesService.getUltimosCambios(pageable, filtro)), ApiResponseStatus.SUCCESS);
	}

}

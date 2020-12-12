package es.uniovi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.uniovi.api.ApiResponse;
import es.uniovi.common.Constantes;
import es.uniovi.exception.RestriccionDatosException;

public interface BaseController {
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	default ApiResponse<Void> handleException(Exception e) {
		return new ApiResponse<>("Error general del servidor", Constantes.ERROR_INTERNO);
	}

	@ExceptionHandler(RestriccionDatosException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	default ApiResponse<Void> handleException(RestriccionDatosException e) {
		return new ApiResponse<>(e.getMessage(), Constantes.ERROR_DATOS);
	}
	
}

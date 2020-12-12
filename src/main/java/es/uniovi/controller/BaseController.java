package es.uniovi.controller;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.common.Constantes;
import es.uniovi.domain.Escuela;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.exception.RestriccionDatosException;

public abstract class BaseController {

	@Autowired
	private ModelMapper modelMapper;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	protected ApiResponse<Void> handleException(Exception e) {
		return new ApiResponse<>("Error general del servidor", Constantes.ERROR_INTERNO);
	}

	@ExceptionHandler(RestriccionDatosException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	protected ApiResponse<Map<String, String>> handleException(RestriccionDatosException e) {
		Map<String, String> errors = new HashMap<>();
		errors.put("error", e.getMessage());
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ApiResponse<Map<String, String>> handleException(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}

	protected Escuela toEntity(EscuelaDto escuelaDto) {
		return modelMapper.map(escuelaDto, Escuela.class);
	}

	protected EscuelaDto toDto(Escuela escuela) {
		return modelMapper.map(escuela, EscuelaDto.class);
	}
}

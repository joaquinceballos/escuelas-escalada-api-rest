package es.uniovi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.api.ListaPaginada;
import es.uniovi.common.Constantes;
import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.dto.SectorDto;
import es.uniovi.dto.ViaDto;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;

public abstract class BaseController {
	
    private static final Logger logger = LogManager.getLogger(BaseController.class);

	@Autowired
	private ModelMapper modelMapper;

	///////////////////////////////
	//// manejo de excepciones ////
	///////////////////////////////

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	protected ApiResponse<Void> handleException(Exception e) {
		logger.error(e);
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
			if (error instanceof FieldError) {
				String fieldName = ((FieldError) error).getField();
				String errorMessage = error.getDefaultMessage();
				errors.put(fieldName, errorMessage);
			} else if (error instanceof ObjectError) {
				ObjectError objectError = (ObjectError) error;
				String objectName = objectError.getObjectName();
				String errorMessage = objectError.getDefaultMessage();
				errors.put(objectName, errorMessage);
				logger.info(error.getClass());
			}
		});
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoEncontradoException.class)
	protected ApiResponse<Map<String, Object>> handleException(NoEncontradoException e) {
		Map<String, Object> errors = new HashMap<>();
		errors.put("recurso", e.getRecurso());
		errors.put("valor", e.getValor());
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ApiResponse<Map<String, Object>> handleException(MethodArgumentTypeMismatchException e) {
		Map<String, Object> errors = new HashMap<>();
		errors.put("parámetro", e.getParameter().getParameterName());
		errors.put("valor pasado", e.getValue());
		errors.put("tipo esperado", e.getParameter().getParameterType().getSimpleName());
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ApiResponse<Map<String, Object>> handleException(HttpMessageNotReadableException e){
		Map<String, Object> errors = new HashMap<>();
		if (e.getCause() instanceof InvalidFormatException) {
			InvalidFormatException invalidFormatException = (InvalidFormatException) e.getCause();
			errors.put("campo", invalidFormatException.getPath().get(0).getFieldName());
			errors.put("tipo esperado", invalidFormatException.getTargetType().getSimpleName());
			errors.put("valor pasado", invalidFormatException.getValue());			
		}
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}

	///////////////////////////////
	////// mapeo DTO-Entity ///////
	///////////////////////////////

	protected Escuela toEntity(EscuelaDto escuelaDto) {
		return modelMapper.map(escuelaDto, Escuela.class);
	}

	protected EscuelaDto toDto(Escuela escuela) {
		return modelMapper.map(escuela, EscuelaDto.class);
	}

	private List<EscuelaDto> toEscuelasDto(List<Escuela> escuelas) {
		return escuelas.stream().map(this::toDto).collect(Collectors.toList());
	}

	protected SectorDto toDto(Sector sector) {
		return modelMapper.map(sector, SectorDto.class);
	}

	protected Sector toEntity(SectorDto sectorDto) {
		return modelMapper.map(sectorDto, Sector.class);
	}
	
	protected List<SectorDto> toSectoresDto(Set<Sector> sectores) {
		return sectores.stream().map(this::toDto).collect(Collectors.toList());
	}

	protected ViaDto toDto(Via via) {
		return modelMapper.map(via, ViaDto.class);
	}
	
	protected Via toEntity(ViaDto viaDto ) {
		return modelMapper.map(viaDto, Via.class);
	}
	
	protected List<ViaDto> toViasDto(Set<Via> vias) {
		return vias.stream().map(this::toDto).collect(Collectors.toList());
	}
	
	protected ListaPaginada<EscuelaDto> toDto(Page<Escuela> paginaEscuelas) {
		ListaPaginada<EscuelaDto> listaPaginada = new ListaPaginada<>();
		listaPaginada.setSize(paginaEscuelas.getSize());
		listaPaginada.setPage(paginaEscuelas.getNumber());
		listaPaginada.setContenido(toEscuelasDto(paginaEscuelas.getContent()));
		listaPaginada.setTotalPaginas(paginaEscuelas.getTotalPages());
		return listaPaginada;
	}
}

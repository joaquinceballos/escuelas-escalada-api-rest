package es.uniovi.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.api.ListaPaginada;
import es.uniovi.common.Constantes;
import es.uniovi.domain.Ascension;
import es.uniovi.domain.CierreTemporada;
import es.uniovi.domain.Croquis;
import es.uniovi.domain.Escuela;
import es.uniovi.domain.LogModificaciones;
import es.uniovi.domain.Sector;
import es.uniovi.domain.TrazoVia;
import es.uniovi.domain.Usuario;
import es.uniovi.domain.Via;
import es.uniovi.domain.Zona;
import es.uniovi.dto.AscensionDto;
import es.uniovi.dto.CierreTemporadaDto;
import es.uniovi.dto.CroquisDto;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.dto.LogModificacionesDto;
import es.uniovi.dto.SectorDto;
import es.uniovi.dto.SectorRootDto;
import es.uniovi.dto.TrazoViaDto;
import es.uniovi.dto.UsuarioDto;
import es.uniovi.dto.ViaDto;
import es.uniovi.dto.ViaRootDto;
import es.uniovi.dto.ZonaDto;
import es.uniovi.exception.ImagenNoValidaException;
import es.uniovi.exception.NoAutorizadoException;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.PatchInvalidoException;
import es.uniovi.exception.RecursoYaExisteException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.service.ImagenService;

@Controller
public abstract class BaseController {
	
    private static final Logger logger = LogManager.getLogger(BaseController.class);

	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private ImagenService imagenService;
	
	@Autowired
	private MessageSource messageSource;

	///////////////////////////////
	//// manejo de excepciones ////
	///////////////////////////////

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	protected ApiResponse<Void> handleException(Exception e, Locale locale) throws Exception {
		logger.error(e);
		String message = messageSource.getMessage("error.server-error", null, locale);
		return new ApiResponse<>(message, Constantes.ERROR_INTERNO);
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	protected ApiResponse<List<String>> handleException(BindException e) {
		return getObjectErrors(e);
	}

	private ApiResponse<List<String>> getObjectErrors(BindException e) {
		List<String> errors = e
				.getBindingResult()
				.getAllErrors()
				.stream()
				.map(objectError -> objectError.getDefaultMessage()
						+ (objectError instanceof FieldError 
								? ": " + ((FieldError) objectError).getRejectedValue()
								: "")
				)
				.collect(Collectors.toList());
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	protected ApiResponse<String> handleException(MissingServletRequestParameterException e, Locale locale) {
		String message = messageSource.getMessage(
				"error.parametro-obligatorio",
				new Object[] { e.getParameterName() },
				locale);
		return new ApiResponse<>(message, ApiResponseStatus.FAIL);
	}

	@ExceptionHandler(RestriccionDatosException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	protected ApiResponse<Map<String, String>> handleException(RestriccionDatosException e) {
		Map<String, String> errors = new HashMap<>();
		errors.put("error", e.getMessage());
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}
	
	@ExceptionHandler(RecursoYaExisteException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	protected ApiResponse<String> handleException(RecursoYaExisteException e, Locale locale) {
		String message = messageSource.getMessage("error.recurso-ya-existe", new Object[] { e.getRecurso() }, locale);
		return new ApiResponse<>(message, ApiResponseStatus.FAIL);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ApiResponse<List<String>> handleException(MethodArgumentNotValidException e) {
		return getObjectErrors(e);
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoEncontradoException.class)
	protected ApiResponse<String> handleException(NoEncontradoException e, Locale locale) {
		String message = messageSource.getMessage(
				"error.no-encontrado",
				new Object[] { e.getRecurso(), e.getValor() },
				locale);
		return new ApiResponse<>(message, ApiResponseStatus.FAIL);
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ImagenNoValidaException.class)
	protected ApiResponse<String> handleException(ImagenNoValidaException e, Locale locale) {
		String message = messageSource.getMessage("error.imagen", null, locale);
		return new ApiResponse<>(message, ApiResponseStatus.FAIL);
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
	protected ApiResponse<Map<String, Object>> handleException(HttpMessageNotReadableException e) {
		Map<String, Object> errors = new HashMap<>();
		if (e.getCause() instanceof InvalidFormatException) {
			InvalidFormatException invalidFormatException = (InvalidFormatException) e.getCause();
			errors.put("campo", invalidFormatException.getPath().get(0).getFieldName());
			errors.put("tipo esperado", invalidFormatException.getTargetType().getSimpleName());
			errors.put("valor pasado", invalidFormatException.getValue());
		} else if (e.getCause() instanceof InvalidTypeIdException) {
			InvalidTypeIdException invalidTypeIdException = (InvalidTypeIdException) e.getCause();
			errors.put("tipo", invalidTypeIdException.getTypeId());
		} else if (e.getCause() instanceof JsonParseException) {
			JsonParseException jsonParseException = (JsonParseException) e.getCause();
			errors.put("request payload", jsonParseException.getRequestPayloadAsString());
			errors.put("Mensage original", jsonParseException.getOriginalMessage());
		}
		errors.put("error", e.getLocalizedMessage());
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	protected ApiResponse<Map<String, Object>> handleException(ConstraintViolationException e) {
		Map<String, Object> errors = new HashMap<>();
		e.getConstraintViolations()
			.stream()
			.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getInvalidValue() + " - " + v.getMessage()));
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PatchInvalidoException.class)
	protected ApiResponse<Map<String, Object>> handleException(PatchInvalidoException e) {
		Map<String, Object> errors = Collections.singletonMap("error", e.getMessage());
		return new ApiResponse<>(errors, ApiResponseStatus.FAIL);		
	}
	
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	@ExceptionHandler(NoAutorizadoException.class)
	protected ApiResponse<String> handleException(NoAutorizadoException e, Locale locale) {
		String message = messageSource.getMessage("error.no-autorizado", null, locale);
		return new ApiResponse<>(message, ApiResponseStatus.FAIL);
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PropertyReferenceException.class)
	protected ApiResponse<Map<String, Object>> handleException(PropertyReferenceException e) {
		Map<String, Object> errors = Collections.singletonMap("error",
				"Propiedad " +
				e.getPropertyName() +
				" no se encuentra en " +
				e.getType());
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
	
	protected List<SectorDto> toSectoresDto(Collection<Sector> sectores) {
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
	
	protected Usuario toEntity(UsuarioDto usuarioDto) {
		return modelMapper.map(usuarioDto, Usuario.class);
	}
	
	protected UsuarioDto toDto(Usuario usuario) {
		UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
		usuarioDto.setPassword(null);
		return usuarioDto;
	}
	
	protected ListaPaginada<UsuarioDto> pageUsuarioToDto(Page<Usuario> usuarios) {
		ListaPaginada<UsuarioDto> listaPaginada = new ListaPaginada<>();
		listaPaginada.setSize(usuarios.getSize());
		listaPaginada.setPage(usuarios.getNumber());
		listaPaginada.setContenido(toUsuariosDto(usuarios.getContent()));
		listaPaginada.setTotalPaginas(usuarios.getTotalPages());
		return listaPaginada;
	}

	private List<UsuarioDto> toUsuariosDto(List<Usuario> usuarios) {
		return usuarios.stream().map(this::toDto).collect(Collectors.toList());
	}
	
	protected ListaPaginada<AscensionDto> pageAscensionToDto(Page<Ascension> ascensiones) {
		ListaPaginada<AscensionDto> listaPaginada = new ListaPaginada<>();
		listaPaginada.setSize(ascensiones.getSize());
		listaPaginada.setPage(ascensiones.getNumber());
		listaPaginada.setTotalPaginas(ascensiones.getTotalPages());		
		listaPaginada.setContenido(toAscencionesDto(ascensiones.getContent()));
		return listaPaginada;		
	}

	private List<AscensionDto> toAscencionesDto(List<Ascension> ascensiones) {
		return ascensiones.stream().map(this::toDto).collect(Collectors.toList());
	}
	
	protected AscensionDto toDto(Ascension ascension) {
		return modelMapper.map(ascension, AscensionDto.class);
	}
	
	protected Ascension toEntity(AscensionDto ascensionDto) {
		return modelMapper.map(ascensionDto, Ascension.class);		
	}
	
	protected CroquisDto toDto(Croquis croquis) throws ImagenNoValidaException {
		CroquisDto croquisDto = modelMapper.map(croquis, CroquisDto.class);
		croquisDto.setImagen(imagenService.toBase64(croquis.getImagen()));
		return croquisDto;
	}
	
	protected List<CroquisDto> toCroquisDto(List<Croquis> croquis) throws ImagenNoValidaException {
		List<CroquisDto> croquisList = new ArrayList<>();
		for (Croquis c : croquis) {
			CroquisDto dto = toDto(c);
			//dto.setImagen(null); //TODO las listas de DTO de croquis no tendrán imágen
			croquisList.add(dto);
		}
		return croquisList;
	}
	
	protected Croquis toEntity(CroquisDto croquisDto) throws ImagenNoValidaException {
		Croquis croquis = modelMapper.map(croquisDto, Croquis.class);
		croquis.setImagen(imagenService.toBytes(croquisDto.getImagen()));
		croquis.setFormatoImagen(imagenService.getFormatoImagen(croquisDto.getImagen()));
		return croquis;
	}
	
	protected TrazoVia toEntity(TrazoViaDto trazoViaDto) {
		return modelMapper.map(trazoViaDto, TrazoVia.class);
	}
	
	protected TrazoViaDto toDto(TrazoVia trazoVia) {
		return modelMapper.map(trazoVia, TrazoViaDto.class);
	}

	protected CierreTemporada toEntity(CierreTemporadaDto cierreTemporadaDto) {
		return modelMapper.map(cierreTemporadaDto, CierreTemporada.class);
	}
	
	protected CierreTemporadaDto toDto(CierreTemporada cierreTemporada) {
		return modelMapper.map(cierreTemporada, CierreTemporadaDto.class);
	}

	protected ListaPaginada<ViaRootDto> pageBuscaViaToDto(Page<Via> paginaVias) {
		ListaPaginada<ViaRootDto> listaPaginada = new ListaPaginada<>();
		listaPaginada.setSize(paginaVias.getSize());
		listaPaginada.setPage(paginaVias.getNumber());
		List<ViaRootDto> contenido = new ArrayList<>();
		for (Via via : paginaVias.getContent()) {
			contenido.add(modelMapper.map(via, ViaRootDto.class));
		}
		listaPaginada.setContenido(contenido);
		listaPaginada.setTotalPaginas(paginaVias.getTotalPages());
		return listaPaginada;
	}

	protected ListaPaginada<SectorRootDto> pageSectorToDto(Page<Sector> paginaSectores) {
		ListaPaginada<SectorRootDto> listaPaginada = new ListaPaginada<>();
		listaPaginada.setSize(paginaSectores.getSize());
		listaPaginada.setPage(paginaSectores.getNumber());
		List<SectorRootDto> contenido = new ArrayList<>();
		for (Sector sector : paginaSectores.getContent()) {
			contenido.add(modelMapper.map(sector, SectorRootDto.class));
		}
		listaPaginada.setContenido(contenido);
		listaPaginada.setTotalPaginas(paginaSectores.getTotalPages());
		return listaPaginada;
	}
	
	protected ListaPaginada<ZonaDto> pageZonasToDto(Page<Zona> paginaZonas){
		ListaPaginada<ZonaDto> listaPaginada = new ListaPaginada<>();
		listaPaginada.setSize(paginaZonas.getSize());
		listaPaginada.setPage(paginaZonas.getNumber());
		listaPaginada.setContenido(toZonasDto(paginaZonas.getContent()));
		listaPaginada.setTotalPaginas(paginaZonas.getTotalPages());
		return listaPaginada;
	}
	
	protected ZonaDto toDto(Zona zona) {
		return modelMapper.map(zona, ZonaDto.class);
	}

	private List<ZonaDto> toZonasDto(List<Zona> zonas) {
		return zonas.stream().map(this::toDto).collect(Collectors.toList());
	}
	
	protected Zona toEntity(ZonaDto zonaDto) {
		return modelMapper.map(zonaDto, Zona.class);
	}
	
	protected ListaPaginada<LogModificacionesDto> pageCambiosToDto(Page<LogModificaciones> paginaCambios){
		ListaPaginada<LogModificacionesDto> listaPaginada = new ListaPaginada<>();
		listaPaginada.setSize(paginaCambios.getSize());
		listaPaginada.setPage(paginaCambios.getNumber());
		listaPaginada.setContenido(toCambiosDto(paginaCambios.getContent()));
		listaPaginada.setTotalPaginas(paginaCambios.getTotalPages());
		return listaPaginada;
	}

	private List<LogModificacionesDto> toCambiosDto(List<LogModificaciones> cambios) {
		return cambios.stream().map(this::toDto).collect(Collectors.toList());
	}
	
	private LogModificacionesDto toDto(LogModificaciones cambio) {
		return modelMapper.map(cambio, LogModificacionesDto.class);
	}
	
}

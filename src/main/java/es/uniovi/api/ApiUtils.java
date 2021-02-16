package es.uniovi.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiUtils {
	
	/**
	 * Añade al cuerpo de la repuesta información del error pasado
	 * 
	 * @param response La response
	 * @param map Pares con información del error
	 * @param status El status de la response
	 */
	public static void errorResponse(HttpServletResponse response,
	                          Map<String, String> map,
	                          HttpStatus status) {
		ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(map, ApiResponseStatus.FAIL);
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		try (PrintWriter writer = response.getWriter()) {
			writer.write(new ObjectMapper().writeValueAsString(apiResponse));
		} catch (IOException e) {
			LogManager.getLogger(ApiUtils.class).debug(e);
		}
	}

	private ApiUtils() {}

}

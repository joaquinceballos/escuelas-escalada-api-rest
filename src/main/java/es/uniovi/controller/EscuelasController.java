package es.uniovi.controller;

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
import es.uniovi.domain.Escuela;
import es.uniovi.service.EscuelaService;

@RestController
@RequestMapping("escuela")
public class EscuelasController implements BaseController {

	@Autowired
	private EscuelaService escuelaService;
	
	@GetMapping("/{id}")
	public ApiResponse<Escuela> getEscuela(@PathVariable(name = "id") Long id) throws Exception  {
		return new ApiResponse<>(escuelaService.getEscuela(id), ApiResponseStatus.SUCCESS);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiResponse<Escuela> addEscuela(@RequestBody Escuela escuela) throws Exception {
		return new ApiResponse<>(escuelaService.addEscuela(escuela), ApiResponseStatus.SUCCESS);
	}

}

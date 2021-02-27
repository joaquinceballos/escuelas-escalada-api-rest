package es.uniovi.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class EscuelaDto extends AbstractEscuelaDto {

	@Valid
	@JsonInclude(Include.NON_NULL)
	private List<SectorDto> sectores = new ArrayList<>();

	public List<SectorDto> getSectores() {
		return sectores;
	}

	public void setSectores(List<SectorDto> sectores) {
		this.sectores = sectores;
	}
	
}

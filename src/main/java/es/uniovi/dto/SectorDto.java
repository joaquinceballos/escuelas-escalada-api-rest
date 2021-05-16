package es.uniovi.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SectorDto extends AbstractSectorDto {

	@Valid
	@JsonInclude(Include.NON_NULL)
	private List<ViaDto> vias = new ArrayList<>();

	public List<ViaDto> getVias() {
		return vias;
	}

	public void setVias(List<ViaDto> vias) {
		this.vias = vias;
	}
	
}

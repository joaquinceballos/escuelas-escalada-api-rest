package es.uniovi.dto;

import java.util.ArrayList;
import java.util.List;

public class SectorRootDto extends SectorInvDto {

	private List<ViaDto> vias = new ArrayList<>();

	public List<ViaDto> getVias() {
		return vias;
	}

	public void setVias(List<ViaDto> vias) {
		this.vias = vias;
	}

}

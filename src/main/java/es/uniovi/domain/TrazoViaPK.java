package es.uniovi.domain;

import java.io.Serializable;

public class TrazoViaPK implements Serializable {

	private static final long serialVersionUID = 2086575616185844493L;

	private Long via;
	
	private Long croquis;

	public Long getVia() {
		return via;
	}

	public void setVia(Long via) {
		this.via = via;
	}

	public Long getCroquis() {
		return croquis;
	}

	public void setCroquis(Long croquis) {
		this.croquis = croquis;
	}

}

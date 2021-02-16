package es.uniovi.api;

import java.util.List;

public class ListaPaginada<T> {

	private Integer size;

	private Integer page;

	private List<T> contenido;

	private Integer totalPaginas;

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<T> getContenido() {
		return contenido;
	}

	public void setContenido(List<T> contenido) {
		this.contenido = contenido;
	}

	public Integer getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(Integer total) {
		this.totalPaginas = total;
	}

	@Override
	public String toString() {
		return "ListaPaginada [size=" + size + ", page=" + page + ", contenido=" + contenido + ", total=" + totalPaginas + "]";
	}

}

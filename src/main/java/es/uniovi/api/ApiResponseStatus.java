package es.uniovi.api;

public enum ApiResponseStatus {

	FAIL("fail"), SUCCESS("success"), ERROR("error");

	private String name;

	ApiResponseStatus(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}

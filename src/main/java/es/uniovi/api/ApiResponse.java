package es.uniovi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ApiResponse<T> {

	private ApiResponseStatus status;
	
	@JsonInclude(Include.NON_NULL)
	private T data;
	
	@JsonInclude(Include.NON_NULL)
	private Integer code;
	
	@JsonInclude(Include.NON_NULL)
	private String message;

	public ApiResponse(T data, ApiResponseStatus status) {
		super();
		this.data = data;
		this.status = status;
	}

	public ApiResponse(String message, Integer code) {
		super();
		this.code = code;
		this.message = message;
		this.status = ApiResponseStatus.ERROR;
	}

	public ApiResponseStatus getStatus() {
		return status;
	}

	public T getData() {
		return data;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ApiResponse [status=" + status + ", data=" + data + ", code=" + code + ", messages=" + message + "]";
	}

}

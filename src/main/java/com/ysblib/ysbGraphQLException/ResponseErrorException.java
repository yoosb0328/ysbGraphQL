package com.ysblib.ysbGraphQLException;

@SuppressWarnings("serial")
public class ResponseErrorException extends RuntimeException {
	public ResponseErrorException() {}
	public ResponseErrorException(String message) {
		super(message);
	}
	public ResponseErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}

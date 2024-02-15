package com.lcwd.electronic.store.exceptions;


// its a way to defined own custom exception
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException() {
		super("Resource not found !!");
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
}

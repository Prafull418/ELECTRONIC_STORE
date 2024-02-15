package com.lcwd.electronic.store.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableResponse<T> { // it a generic class of type T in which we can use any type of Object

	// when this field will get then always we get in UserDto object only so we will create this class as generic type with this method 
	 //where we can get any type of Object 
	//private List<UserDto> content;
	private List<T> content;
	private int pageNUmber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean lastPage;
	
}

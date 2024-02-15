package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

public interface CategoryService  {

	CategoryDto create(CategoryDto categoryDto);
	CategoryDto update(CategoryDto category,String catId);
	void delete(String catId);
	
	PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize,String sortBy, String sortDir);
	
	CategoryDto getCategoryById(String catId);
	
	
	
}

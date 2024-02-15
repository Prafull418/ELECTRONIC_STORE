package com.lcwd.electronic.store.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ApiResponseMessage.ApiResponseMessageBuilder;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.services.CategoryService;


@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	
	@Autowired
	private CategoryService categoryService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDto> create(@javax.validation.Valid @RequestBody CategoryDto categoryDto){
		CategoryDto create = categoryService.create(categoryDto);
		return new ResponseEntity<CategoryDto>(create, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{catId}")
	public ResponseEntity<CategoryDto> update(@Valid @RequestBody CategoryDto categoryDto,@PathVariable String catId){
		CategoryDto update = categoryService.update(categoryDto, catId);
		return new ResponseEntity<CategoryDto>(update, HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{catId}")
	public ResponseEntity<ApiResponseMessage> delete(@PathVariable String catId) {
		 categoryService.delete(catId);
		 ApiResponseMessage response = ApiResponseMessage.builder().message("Category deleted succefully !! ").success(true).status(HttpStatus.OK).build();
		 return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{catId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String catId){
		
		CategoryDto categoryById = categoryService.getCategoryById(catId);
		return new ResponseEntity<CategoryDto>(categoryById, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCat(
			@RequestParam(value =  "pageNumber",defaultValue = "0", required = false) int pageNumber, 
			@RequestParam(value =  "pageSize",  defaultValue = "10", required = false) int pageSize, 
			@RequestParam(value =  "sortBy", defaultValue = "title", required = false) String sortBy, 
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){
		PageableResponse<CategoryDto> allCategrories = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<CategoryDto>>(allCategrories, HttpStatus.OK);
	}
	
}

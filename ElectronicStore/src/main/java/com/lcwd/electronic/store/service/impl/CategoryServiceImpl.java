package com.lcwd.electronic.store.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		String randomId = UUID.randomUUID().toString();
		Category category = mapper.map(categoryDto,Category.class);
		category.setCategoryId(randomId);
		Category save= this.categoryRepository.save(category);
		return this.mapper.map(save, CategoryDto.class);
	}

	@Override
	public CategoryDto update(CategoryDto category, String catId) {
        Category cat = this.categoryRepository.findById(catId).orElseThrow(()->new ResourceNotFoundException("category not found by given id !!"));
        cat.setTitle(category.getTitle());
        cat.setDescription(category.getDescription());
        cat.setCoverImage(category.getCoverImage());
        Category saveCat = this.categoryRepository.save(cat);
		return this.mapper.map(saveCat, CategoryDto.class);
	}

	@Override
	public void delete(String catId) {
		 Category cat = this.categoryRepository.findById(catId).orElseThrow(()->new ResourceNotFoundException("category not found by given id !!"));
	     this.categoryRepository.delete(cat);  
	}

	@Override
	public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize,String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		PageRequest pageResponse = PageRequest.of(pageNumber, pageSize,sort);
		Page<Category> allCategories = this.categoryRepository.findAll(pageResponse);
		PageableResponse<CategoryDto> pagebableResponse = Helper.getPagebableResponse(allCategories, CategoryDto.class);
		return pagebableResponse;
	}

	@Override
	public CategoryDto getCategoryById(String catId) {
		 Category cat = this.categoryRepository.findById(catId).orElseThrow(()->new ResourceNotFoundException("category not found by given id !!"));
		 return this.mapper.map(cat, CategoryDto.class);
	}

}

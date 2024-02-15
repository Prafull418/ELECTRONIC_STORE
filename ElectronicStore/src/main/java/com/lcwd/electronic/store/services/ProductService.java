package com.lcwd.electronic.store.services;

import java.util.List;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;

public interface ProductService {

	
	ProductDto create(ProductDto productDto);
	
	ProductDto update(ProductDto productDto,String productId);
	
	void delete(String productId);
	
	ProductDto getProductById(String productId);
	
    PageableResponse<ProductDto> getAllProducts(int pageNUmber, int pageSize, String sortBy,String sortDir);
    
    PageableResponse<ProductDto> getAllLive(int pageNUmber, int pageSize, String sortBy,String sortDir);
    
    PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNUmber, int pageSize, String sortBy,String sortDir);
    
    
    //creating product with category
    ProductDto createProductWithCategory(ProductDto productDto,String categoryId);
    
    ProductDto updateCategoryofProduct(String productId, String categoryId);
	
    PageableResponse<ProductDto> getAllProductsOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
}

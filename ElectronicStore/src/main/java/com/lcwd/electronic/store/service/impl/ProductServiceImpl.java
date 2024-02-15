package com.lcwd.electronic.store.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public ProductDto create(ProductDto productDto) {
		String id = UUID.randomUUID().toString();
	    Product product = this.mapper.map(productDto, Product.class);
	    product.setProductId(id);
	    product.setAddedDate(new Date());
		return this.mapper.map(this.productRepository.save(product), ProductDto.class);
	}

	@Override
	public ProductDto update(ProductDto productDto, String productId) {
		Product product = this.productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product not found with given id !!"));
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setQuantity(productDto.getQuantity());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		product.setProductImageName(productDto.getProductImageName());
		Product save = this.productRepository.save(product);
		return this.mapper.map(save, ProductDto.class);
	}

	@Override
	public void delete(String productId) {
		Product product = this.productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product not found with given id !!"));
		this.productRepository.delete(product);
	}

	@Override
	public ProductDto getProductById(String productId) {
		Product product = this.productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product not found with given id !!"));
		return this.mapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProducts(int pageNUmber, int pageSize, String sortBy,String sortDir) {
		
		Sort sort= (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		
		PageRequest page = PageRequest.of(pageNUmber, pageSize, sort);
		Page<Product> allFindProducts = this.productRepository.findAll(page);
		PageableResponse<ProductDto> pagebableResponse = Helper.getPagebableResponse(allFindProducts, ProductDto.class);
		return pagebableResponse;
	}

	@Override
	public PageableResponse<ProductDto> getAllLive(int pageNUmber, int pageSize, String sortBy,String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		PageRequest page = PageRequest.of(pageNUmber, pageSize, sort);
		Page<Product> allFindProducts = this.productRepository.findByLiveTrue(page);
		PageableResponse<ProductDto> pagebableResponse = Helper.getPagebableResponse(allFindProducts, ProductDto.class);
		return pagebableResponse;
	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNUmber, int pageSize, String sortBy,String sortDir) {
		    Sort sort= (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
			PageRequest page = PageRequest.of(pageNUmber, pageSize, sort);
			Page<Product> allFindProducts = this.productRepository.findByTitleContaining(subTitle,page);
			PageableResponse<ProductDto> pagebableResponse = Helper.getPagebableResponse(allFindProducts, ProductDto.class);
			return pagebableResponse;
	}

	@Override
	public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
		String id = UUID.randomUUID().toString();
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Given category id not found !!"));
	    Product product = this.mapper.map(productDto, Product.class);
	    product.setProductId(id);
	    product.setAddedDate(new Date());
	    product.setCategory(category);
		return this.mapper.map(this.productRepository.save(product), ProductDto.class);
	}


	@Override
	public ProductDto updateCategoryofProduct(String productId, String categoryId) {
		Product product = this.productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Given product id not found !!"));
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Given category id not found !!"));
		product.setCategory(category);
		Product save = this.productRepository.save(product);
		return this.mapper.map(save, ProductDto.class);
	
	}

	@Override
	public PageableResponse<ProductDto> getAllProductsOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Given category id not found !!"));
		Sort sort= (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		PageRequest page = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> allProductsOfCategory = this.productRepository.findByCategory(category,page);
		return Helper.getPagebableResponse(allProductsOfCategory, ProductDto.class);
	}
	
	
	
}

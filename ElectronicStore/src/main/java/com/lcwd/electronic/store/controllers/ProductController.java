package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

	private Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Value("${product.image.path}")
	private String productImagePath;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto){
	
		ProductDto create = this.productService.create(productDto);
		return new ResponseEntity<ProductDto>(create, HttpStatus.CREATED);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto, @PathVariable String productId){
	
		ProductDto update = this.productService.update(productDto,productId);
		return new ResponseEntity<ProductDto>(update, HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage>  delete(@PathVariable String productId){
		this.productService.delete(productId);
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage.builder().message("Product deleted successfully !!").status(HttpStatus.OK).success(true).build(),HttpStatus.OK);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProudctById(@PathVariable String productId){
		ProductDto productById = this.productService.getProductById(productId);
		return new ResponseEntity<ProductDto>(productById, HttpStatus.OK);
	}
	
	
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value =  "pageNumber",defaultValue = "0", required = false) int pageNumber, 
			@RequestParam(value =  "pageSize",  defaultValue = "10", required = false) int pageSize, 
			@RequestParam(value =  "sortBy", defaultValue = "title", required = false) String sortBy, 
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){
	        PageableResponse<ProductDto> allProducts = this.productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
	        return  new ResponseEntity<PageableResponse<ProductDto>>(allProducts, HttpStatus.OK);
	}
	
	//get All live products
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductsLive(
			@RequestParam(value =  "pageNumber",defaultValue = "0", required = false) int pageNumber, 
			@RequestParam(value =  "pageSize",  defaultValue = "10", required = false) int pageSize, 
			@RequestParam(value =  "sortBy", defaultValue = "title", required = false) String sortBy, 
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){
	        PageableResponse<ProductDto> allProducts = this.productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
	        return  new ResponseEntity<PageableResponse<ProductDto>>(allProducts, HttpStatus.OK);
	}
	
	
	@GetMapping("/search/{query}")
	public ResponseEntity<PageableResponse<ProductDto>> searchProducts(
			@PathVariable String query,
			@RequestParam(value =  "pageNumber",defaultValue = "0", required = false) int pageNumber, 
			@RequestParam(value =  "pageSize",  defaultValue = "10", required = false) int pageSize, 
			@RequestParam(value =  "sortBy", defaultValue = "title", required = false) String sortBy, 
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){
	        PageableResponse<ProductDto> allProducts = this.productService.searchByTitle(query,pageNumber, pageSize, sortBy, sortDir);
	        return  new ResponseEntity<PageableResponse<ProductDto>>(allProducts, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(
			@PathVariable String productId, @RequestParam("productImage") MultipartFile file) throws IOException{
           		ProductDto product = this.productService.getProductById(productId);
		String uploadImage = this.fileService.uploadImage(file, productImagePath);
		product.setProductImageName(uploadImage);
		this.productService.update(product, productId);
		ImageResponse imageResponse = ImageResponse.builder().message("Product Image uploaded successfully !!").status(HttpStatus.CREATED).success(true).imageName(uploadImage).build();
		return new ResponseEntity<ImageResponse>(imageResponse, HttpStatus.CREATED);
	}
	
	@GetMapping("/image/{productId}")
	public void serveProductImage(@PathVariable String productId ,HttpServletResponse response) throws IOException {
		ProductDto product = this.productService.getProductById(productId);
		String productImageName = product.getProductImageName();
		InputStream resource = this.fileService.getResource(productImagePath, productImageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		logger.info("product Image name "+productImageName);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{categoryId}")
	public ResponseEntity<ProductDto> createProductWithCategory(@RequestBody ProductDto productDto, @PathVariable String categoryId){
		ProductDto product = this.productService.createProductWithCategory(productDto, categoryId);
		return new ResponseEntity<ProductDto>(product, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}/{categoryId}")
	public ResponseEntity<ProductDto> updateCategoryofProduct(@PathVariable String productId, @PathVariable String categoryId){
		ProductDto updateCategoryofProduct = this.productService.updateCategoryofProduct(productId, categoryId);
		return new ResponseEntity<ProductDto>(updateCategoryofProduct, HttpStatus.OK);
	}
	
	@GetMapping("/categories/{categoryId}")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductsOfCategory(
			@PathVariable String categoryId,
			@RequestParam(value =  "pageNumber",defaultValue = "0", required = false) int pageNumber, 
			@RequestParam(value =  "pageSize",  defaultValue = "10", required = false) int pageSize, 
			@RequestParam(value =  "sortBy", defaultValue = "title", required = false) String sortBy, 
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){
		PageableResponse<ProductDto> allProductsOfCategory = this.productService.getAllProductsOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(allProductsOfCategory, HttpStatus.OK);
	}
}

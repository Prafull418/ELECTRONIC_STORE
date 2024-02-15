package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	private CartService cartService;
	
	//add items to cart
	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest addItemToCartRequest){
		CartDto cartDto = cartService.addItemToCart(userId, addItemToCartRequest);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/items/{cartItemId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable int cartItemId ){
		cartService.removeItemFromCart(cartItemId);
		ApiResponseMessage response = ApiResponseMessage.builder()
		.message("item is removed !!")
		.status(HttpStatus.OK)
		.success(true)
		.build();
		return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}") // we can take here cart id as well 
	public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
		cartService.clearCart(userId);
		ApiResponseMessage response = ApiResponseMessage.builder()
		.message("Now cart is blank !!")
		.status(HttpStatus.OK)
		.success(true)
		.build();
		return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.OK);
	} 
	
	@GetMapping("/{userId}") // to find a user how much cart items he has
	public ResponseEntity<CartDto> getCart(@PathVariable String userId){
		CartDto cartDto = cartService.getCartByUser(userId);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}
}

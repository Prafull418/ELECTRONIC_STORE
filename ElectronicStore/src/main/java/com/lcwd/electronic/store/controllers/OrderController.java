package com.lcwd.electronic.store.controllers;

import java.util.List;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.services.OrderService;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	
	@Autowired
	private OrderService orderService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
		OrderDto createOrder = this.orderService.createOrder(request);
		return new ResponseEntity<OrderDto>(createOrder, HttpStatus.CREATED);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId ){
		this.orderService.removeOrder(orderId);
		ApiResponseMessage response = ApiResponseMessage.builder().status(HttpStatus.OK)
		.success(true)
		.message("Order is removed !!")
		.build();
		return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.OK);
	}
	
	
	//get orders of the user
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
		
		List<OrderDto> orderOfUser = orderService.getOrderOfUser(userId);
		return new ResponseEntity<List<OrderDto>>(orderOfUser, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getOrders(
		@RequestParam(value =  "pageNumber",defaultValue = "0", required = false) int pageNumber, 
		@RequestParam(value =  "pageSize",  defaultValue = "10", required = false) int pageSize, 
		@RequestParam(value =  "sortBy", defaultValue = "orderedDate", required = false) String sortBy, 
		@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)
	{
      
		PageableResponse<OrderDto> orders = this.orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<OrderDto>>(orders, HttpStatus.OK);
	}
}

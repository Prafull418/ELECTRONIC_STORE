package com.lcwd.electronic.store.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.OrderItem;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.OrderService;

import lombok.Builder;

@Service
public class OrderServiceImpl implements OrderService {

	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ModelMapper mapper;

	
	
	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		String userId = orderDto.getUserId();
		String cartId = orderDto.getCartId();
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found by given id !!"));
		Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("Cart with given id not found in database"));
		
		List<CartItem> items = cart.getItems();
		
		if (items.size()<=0) {
			throw new BadApiRequestException("Invalid number of items in cart !!");
		}
		
		//now we have user and cart then we can order
		
		Order order = Order.builder().billingName(orderDto.getBillingName())
		.billingPhone(orderDto.getBillingPhone())
		.billingAddress(orderDto.getBillingAddress())
		.orderedDate(new Date())
		.deliveredDate(null)
		.paymentStatus(orderDto.getPaymentStatus())
		.orderStatus(orderDto.getOrderStatus())
		.orderId(UUID.randomUUID().toString())
		.user(user)
		.build();
		//orderitems and amount are left
        //by deafult is 0 initial value
		AtomicReference<Integer> orderAmount = new AtomicReference<Integer>(0); 
        		
		//here we are converting or traversing cart items list to Order items list for that we use only return converter list object
		List<OrderItem> orderItems = items.stream().map(cartitem -> {
			//CartItem->>OrderItem  traversing
			OrderItem orderItem = OrderItem.builder().quantity(cartitem.getQuantity())
		   	.product(cartitem.getProduct())
		   	.totalPrice(cartitem.getQuantity()*cartitem.getProduct().getDiscountedPrice())
		   	.order(order)
		   	.build();
			
			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
			return orderItem;
			//return new OrderItem();  //previously it was  //return converter list
		}).collect(Collectors.toList());
		
		order.setOrderItems(orderItems);
		//amount is left only now
		order.setOrderAmount(orderAmount.get());
		
		//clear the cart
		cart.getItems().clear();
		cartRepository.save(cart);
		Order savedOrder = orderRepository.save(order);
		
		return mapper.map(savedOrder, OrderDto.class);
	}

	//when we delete order its orderitems will deleted automatically
	@Override
	public void removeOrder(String orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceAccessException("order not found by given id !!"));
		orderRepository.delete(order);
	}

	@Override
	public List<OrderDto> getOrderOfUser(String userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found by given id !!"));
		List<Order> orders = orderRepository.findByUser(user);
		List<OrderDto> orderDtos = orders.stream().map(order->mapper.map(order, OrderDto.class)).collect(Collectors.toList());
		return orderDtos;
	}

	@Override
	public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort= (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		PageRequest pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Order> page = orderRepository.findAll(pageable);
		return Helper.getPagebableResponse(page, OrderDto.class);
	}
	
	
	

}

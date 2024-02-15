package com.lcwd.electronic.store.service.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.ISBN;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;
import com.lcwd.electronic.store.services.CategoryService;

@Service
public class CartServiceImpl  implements CartService{

	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
		
		int quantity = request.getQuantity();
		String productId = request.getProductId();
		
		if (quantity<=0) {
			throw new BadApiRequestException("Requested quantity is not valid !!");
		}
		
		//fetch the product
		Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product not found in database"));
		//fetch the user from db
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found in database"));
		Cart cart = null;  // this concept is used when an object or element is not in table to check if not then create in catch block
		try {
			cart= cartRepository.findByUser(user).get();
		} catch (NoSuchElementException e) {  
			cart = new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreateAt(new Date());
			cart.setUser(user);
		}
		//perform cart operations
		//if cart items already present; then update that item
		AtomicReference<Boolean> updated=new AtomicReference<>(false);
		// if user add same product(which he added previously) in cart it means user wants to update his product.
		List<CartItem> items = cart.getItems();
		 items = items.stream().map(item->{
			if (item.getProduct().getProductId().equals(productId)) {
				//item or product already present in cart item table then update
				item.setQuantity(quantity);
				item.setTotalPrice(quantity*product.getDiscountedPrice());
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());
		
		//cart.setItems(updatedItems);
		//create items
	  if (!updated.get()) { // it will come here when product is not  present in cart item table. 
			CartItem cartItem = CartItem.builder().quantity(quantity)
					.totalPrice(quantity*product.getDiscountedPrice())
					.cart(cart)
					.product(product)
					.build();
			
			cart.getItems().add(cartItem); //cart item add in list of cart items in cart to get cart item list.
	}
		
		//it saves the cart and cart item as well
		Cart updatedCart = cartRepository.save(cart);
		return mapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(int cartItemId) {
		CartItem cartItem1 = cartItemRepository.findById(cartItemId).orElseThrow(()->new ResourceNotFoundException("Cart item not found"));
		cartItemRepository.delete(cartItem1);
	}

	@Override
	public void clearCart(String userId) {

     	//fetch the user from db
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found in database"));
	    Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart of given user not found !!"));
	    cart.getItems().clear();   // it clear the cart items of that user from cart item table for this we will use orphanremoval in cart entity
	    cartRepository.save(cart);   // to perform database operations in tables 
		
		
	}

	@Override
	public CartDto getCartByUser(String userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found in database"));
	    Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Cart of given user not found !!"));
	    return mapper.map(cart, CartDto.class);
	}
	
	
}

package com.lcwd.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderItemDto {

	private int orderItemId;
	private int quantity;
	private int totalPrice;
	private ProductDto product;
}

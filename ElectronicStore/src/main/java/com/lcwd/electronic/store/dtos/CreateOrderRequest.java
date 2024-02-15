package com.lcwd.electronic.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

	@NotBlank(message = "Cart id is required !!")
	private String cartId;
	
	@NotBlank(message = "Cart id is required !!")
	private String userId;
	
	private String orderStatus="PENDING";
	private String paymentStatus="NOTPAID";
	
	@NotBlank(message = "Billing address id is required !!")
	private String billingAddress;	
	
	@NotBlank(message = "Billing phone id is required !!")
	private String billingPhone;		
	
	@NotBlank(message = "Billing name is required !!")
	private String billingName;
	
	
}

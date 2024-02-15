package com.lcwd.electronic.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lcwd.electronic.store.entities.OrderItem;
import com.lcwd.electronic.store.entities.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderDto {
	

	private String orderId;
	private String orderStatus="PENDING";
	private String paymentStatus="NOTPAID";
	private int orderAmount;
	private String billingAddress;	
	private String billingPhone;		
	private String billingName;
	private Date orderedDate=new Date();
	private Date deliveredDate;
	//private UserDto user;
	private List<OrderItemDto> orderItems  = new ArrayList<>();
	

}

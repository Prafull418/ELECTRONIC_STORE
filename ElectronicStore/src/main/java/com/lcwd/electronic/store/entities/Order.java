package com.lcwd.electronic.store.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "orders")
public class Order {

	@Id
	private String orderId;
	
	//pending -->dispatched-->> delivered,
	//we can do this with the help of enum
	private String orderStatus;
	
	//not paid, paid
	//enum or boolean- false->not-paid,true->paid we can use 
	private String paymentStatus;
	
	private int orderAmount;
	
	@Column(length = 1000)
	private String billingAddress;
	
	
	private String billingPhone;
	
	//if we want to deliver to someone
	private String billingName;
	
	private Date orderedDate;
	
	private Date deliveredDate;
	
	//order user
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	private List<OrderItem> orderItems  = new ArrayList<>();
	
}

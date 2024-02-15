package com.lcwd.electronic.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {


    private String cartId;
	
    private Date createAt;
    
    private UserDto user;
    
    //mapping cart-items

    private List<CartItemDto> items = new ArrayList<>();
}

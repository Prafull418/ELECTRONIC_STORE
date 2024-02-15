package com.lcwd.electronic.store.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.lcwd.electronic.store.dtos.PageableResponse;

public class Helper {
//this is common so in whole application we will use this for every object.its reusable code
	public static<U,V> PageableResponse<V> getPagebableResponse(Page<U> page, Class<V> type){
		List<U> objects = page.getContent();
		List<V> allUsers = objects.stream().map(object->(new ModelMapper().map(object, type))).collect(Collectors.toList());
		PageableResponse<V> response = new PageableResponse<>();
		response.setContent(allUsers);
		response.setPageNUmber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setLastPage(page.isLast());
		return response;
	}
	
}

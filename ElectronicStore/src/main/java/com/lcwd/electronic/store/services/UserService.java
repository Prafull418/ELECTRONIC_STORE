package com.lcwd.electronic.store.services;

import java.io.IOException;
import java.util.List;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;

public interface UserService {

	UserDto createUser(UserDto user);
	UserDto updateUser(UserDto userDto,String userId);
	
	void deleteUser(String userId) throws IOException;
	
	PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize,String sortBy, String sortDir);
	//List<UserDto> getAllUser(int pageNumber, int pageSize,String sortBy, String sortDir);
	
	UserDto getUser(String userId);
	
	UserDto getUserByEmail(String email);
	
	List<UserDto> searchUser(String keyword);
}

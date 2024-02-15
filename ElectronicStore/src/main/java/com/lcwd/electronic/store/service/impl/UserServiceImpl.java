package com.lcwd.electronic.store.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	Logger logger  = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	
	  @Autowired 
	  private PasswordEncoder passwordEncoder;
	  
	  @Autowired
	  private RoleRepository roleRepository;
	 
	  @Value("${normal.role.id}")
	  private String role_normal_id;
	  
	  @Value("${admin.role.id}")
	  private String role_admin_id;

	@Override
	public UserDto createUser(UserDto userDto) {
		//generate unique id in string format
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		//	dto>>entity conversion
		User user = dtoToEntity(userDto);
		//user.setPassword(userDto.getPassword());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
       // Role role = this.roleRepository.findById(role_normal_id).get();
        Role role = this.roleRepository.findById(role_admin_id).get();
        user.getRoles().add(role);
		User savedUser=	userRepository.save(user);
		//entity to dto conversion
		UserDto newDto= entityToDto(savedUser);
		return newDto;
	}



	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		// TODO Auto-generated method stub
		//User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found with given id !!"));
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with given id !!"));
		user.setName(userDto.getName());
		//email update if required
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());
		User saveUser = userRepository.save(user);
		UserDto updatedDto = entityToDto(saveUser);
		return updatedDto;
	}

	@Override
	public void deleteUser(String userId) throws IOException {
		// TODO Auto-generated method stub
		//User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found with given id !!"));
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with given id !!"));
		String userImageName = user.getImageName();
		//  image/users/abc.png
		String fullUserImagePath = imagePath+userImageName;
		logger.info("user image path name "+fullUserImagePath);
		try {
			Files.delete(Paths.get(fullUserImagePath));
		} catch (NoSuchFileException e) {
			e.getStackTrace();
		}
		userRepository.delete(user);
	}

	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize,String sortBy,String sortDir) {
		
	
		//Sort sort = Sort.by(sortBy);
		//using ternary operator 
		Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		
		//pageable is interface so pagerequest provide implementation of it
		//pageNumber default starts from 0
		//Pageable pageable = PageRequest.of(pageNumber, pageSize); 
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		
		Page<User> page = userRepository.findAll(pageable);
		//List<User> users = page.getContent();
		//List<User> users = userRepository.findAll();
		//conversion all users to user dtos
		/*
		 * List<UserDto> allUsers =
		 * users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
		 * 
		 * PageableResponse<UserDto> response = new PageableResponse<>();
		 * response.setContent(allUsers); response.setPageNUmber(page.getNumber());
		 * response.setPageSize(page.getSize());
		 * response.setTotalElements(page.getTotalElements());
		 * response.setTotalPages(page.getTotalPages());
		 * response.setLastPage(page.isLast());
		 */
		PageableResponse<UserDto> pagebableResponse = Helper.getPagebableResponse(page, UserDto.class);
		return pagebableResponse;
	}

	@Override
	public UserDto getUser(String userId) {
		// TODO Auto-generated method stub

		//User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found with given id !!"));
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found with given id !!"));
		UserDto userDto = entityToDto(user);
		return userDto;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub
		//User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found with given email"));
		User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found with given email"));
		return entityToDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = userRepository.findByNameContaining(keyword);
		List<UserDto> allUsers = users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
		return allUsers;
	}


	private UserDto entityToDto(User savedUser) {
		// TODO Auto-generated method stub
		/*
		 * UserDto userDto = UserDto.builder() .userId(savedUser.getUserId())
		 * .name(savedUser.getName()) .email(savedUser.getEmail())
		 * .password(savedUser.getPassword()) .about(savedUser.getAbout())
		 * .gender(savedUser.getGender()) .imageName(savedUser.getImageName()).build();
		 */
		
		return mapper.map(savedUser, UserDto.class);
	}

	private User dtoToEntity(UserDto userDto) {
		// TODO Auto-generated method stub
		/*
		 * User user = User.builder() .userId(userDto.getUserId())
		 * .name(userDto.getName()) .email(userDto.getEmail())
		 * .password(userDto.getPassword()) .about(userDto.getAbout())
		 * .gender(userDto.getGender()) .imageName(userDto.getImageName()).build();
		 */
		return mapper.map(userDto, User.class);
	}
}

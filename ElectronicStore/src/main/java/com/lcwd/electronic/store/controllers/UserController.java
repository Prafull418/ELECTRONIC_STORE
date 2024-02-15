package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Api(value = "UserController",description = "REST APIs related to perform user operations !!")// for swagger-ui
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	
	@Autowired
	private FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	@ApiOperation(value = "create a new user !!")
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "Success | OK"),
			@ApiResponse(code = 401,message = "Not Authorized !!"),
			@ApiResponse(code = 201,message = "New User Created !!")
	})
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto createUser = userService.createUser(userDto);
		return new ResponseEntity<>(createUser, HttpStatus.CREATED);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId){
		UserDto updateUser = userService.updateUser(userDto, userId);
		return new ResponseEntity<>(updateUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException{
         userService.deleteUser(userId);
         ApiResponseMessage message = ApiResponseMessage.builder()
        		 .message("User is deleted successfulley !!")
        		 .success(true).status(HttpStatus.OK)
        		 .build();
         return new ResponseEntity<>(message,HttpStatus.OK);
	}
	
	 // we can also have tags={"user-controller","user-apis"}
	@ApiOperation(value = "get all users",tags = "user-controller")   // for swagger-ui 
	@GetMapping // pagenumber and pagesize we defined as optional
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber, 
			@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
			@RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,//by default I am taking name field
			@RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){
	       return new ResponseEntity<PageableResponse<UserDto>>(userService.getAllUser(pageNumber, pageSize,sortBy,sortDir), HttpStatus.OK);	
	}
	
	@ApiOperation(value = "get single user by id !!")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId){
		return new ResponseEntity<UserDto>(userService.getUser(userId), HttpStatus.OK);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
		return new ResponseEntity<UserDto>(userService.getUserByEmail(email),HttpStatus.OK);
	}
	
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
		return new ResponseEntity<List<UserDto>>(userService.searchUser(keywords),HttpStatus.OK);
	}
	
	//upload user Image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(
		   @RequestParam("userImage") MultipartFile uploadImage,
		   @PathVariable String userId ) throws IOException{
		
		String imageName = fileService.uploadImage(uploadImage, imageUploadPath);
		
		UserDto user = userService.getUser(userId);
		user.setImageName(imageName);
		UserDto userDto = userService.updateUser(user, userId);
		ImageResponse imageResponse = ImageResponse.builder().message("image upload successfully !!").imageName(imageName).success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<ImageResponse>(imageResponse, HttpStatus.CREATED);
	}
	
	
	////serve the file means to show the user image in response means on client such as postman or browser 
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable String userId,HttpServletResponse response) throws IOException {
            UserDto user = userService.getUser(userId);
            logger.info("User image name "+user.getImageName());
            InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource,response.getOutputStream());
	}
	
	
}
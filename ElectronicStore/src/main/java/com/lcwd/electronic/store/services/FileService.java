package com.lcwd.electronic.store.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	//creating an image like userimage or productimage in a folder on our local application from our 
	//local machine drive
	String uploadImage(MultipartFile file,String path) throws IOException;
	
	//serve means to show the image in response means on client such as postman or browser 
	InputStream getResource(String path,String name) throws IOException;
	
}

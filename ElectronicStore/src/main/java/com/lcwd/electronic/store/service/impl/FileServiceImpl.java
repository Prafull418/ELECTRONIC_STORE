package com.lcwd.electronic.store.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	
	
	@Override
	public String uploadImage(MultipartFile file, String path) throws IOException {
	
		//abc.png
	String originalFileName = file.getOriginalFilename();
    logger.info("filename: "+originalFileName);
    
    String fileName = UUID.randomUUID().toString();
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    String fileNameWithExtension = fileName + extension;
    String fullPathWithFileName = path + fileNameWithExtension;
    if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
		
    	//file save in application folder
    	File folder = new File(path);
    	
    	if (!folder.exists()) {
			//create the folder
    		folder.mkdirs();  // it create till the more than two level
		}
    	
    	//copy the file into folder
    	//InputStream inputStream = file.getInputStream();
    	Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
    	return fileNameWithExtension;
	}else {
		throw new BadApiRequestException("file this "+extension+" not allowed");
	}
	}

	//serve the file means to show the image in response means on client such as postman or browser 
	@Override
	public InputStream getResource(String path, String name) throws IOException {
		String fullPath = path+File.separator+name; // image/users/
		
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}

	
}

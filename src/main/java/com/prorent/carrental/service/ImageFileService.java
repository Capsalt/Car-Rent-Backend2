package com.prorent.carrental.service;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.prorent.carrental.domain.ImageFile;
import com.prorent.carrental.exception.ResourceNotFoundException;
import com.prorent.carrental.repository.ImageFileRepository;



@Service
public class ImageFileService {

	@Autowired
	private ImageFileRepository imageFileRepository;
	
	
	public ImageFile save(MultipartFile file) throws IOException {
		String fileName=StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		ImageFile imageFile=new ImageFile(fileName,file.getContentType(),file.getBytes());
		imageFileRepository.save(imageFile);
		return imageFile;
	}
	
	public ImageFile getFile(String id) {
		return imageFileRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException("ImageFile not found with id:"+id));
	}
	
	
	public Stream<ImageFile> getAllImageFiles(){
		return imageFileRepository.findAll().stream();
	}
	
}

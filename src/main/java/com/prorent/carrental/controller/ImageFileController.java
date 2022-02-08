package com.prorent.carrental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prorent.carrental.domain.ImageFile;
import com.prorent.carrental.exception.UploadImageException;
import com.prorent.carrental.service.ImageFileService;
import com.prorent.carrental.service.dto.ImageFileDTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/files")
public class ImageFileController {

	private final ImageFileService imageFileService;

	@PostMapping("/upload")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			ImageFile imageFile = imageFileService.save(file);
			Map<String, String> map = new HashMap<>();
			map.put("imageId", imageFile.getId());
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			throw new UploadImageException("Could not upload image file");
		}

	}
	
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ImageFileDTO>> getAllFiles(){
		List<ImageFileDTO> imageFiles = imageFileService.getAllImageFiles().map(imageFile->{
			String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/files/")
			.path(imageFile.getId())
			.toUriString();
			
			return new ImageFileDTO(imageFile.getName(),downloadUri,imageFile.getType(),
					imageFile.getData().length);
			
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(imageFiles);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id){
		ImageFile imageFile=imageFileService.getFile(id);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename="+imageFile.getName()+ "")
				.body(imageFile.getData());
	}
	
	
	@GetMapping("/display/{id}")
	public ResponseEntity<byte[]> displayImage(@PathVariable String id){
		ImageFile imageFile=imageFileService.getFile(id);
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(imageFile.getData(),headers,HttpStatus.OK);
	}
	
	
	

}

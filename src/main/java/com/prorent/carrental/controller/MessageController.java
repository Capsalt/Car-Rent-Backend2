package com.prorent.carrental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prorent.carrental.domain.Message;
import com.prorent.carrental.service.MessageService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

	private MessageService messageService;

	private static Logger logger = LoggerFactory.getLogger(MessageController.class);

	@PostMapping
	public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message) {
		Message savedMessage = messageService.createMessage(message);

		return new ResponseEntity<Message>(savedMessage, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Message> getMessage(@PathVariable Long id) {
		Message foundMessage = messageService.getMessage(id);
		return ResponseEntity.ok(foundMessage);
	}

	@GetMapping("/request")
	public Message getMessageByRequest(@RequestParam Long id) {
		return messageService.getMessage(id);
	}

	@GetMapping
	public ResponseEntity<List<Message>> getAllMessage() {
		List<Message> messageList = messageService.getAllMessage();
		return ResponseEntity.ok(messageList);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteMessage(@PathVariable Long id) {

		messageService.deleteMessage(id);
		Map<String, String> map = new HashMap<>();
		map.put("Success", String.valueOf(true));
		map.put("id", String.valueOf(id.longValue()));
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@PutMapping("/{id}") 
	public ResponseEntity<Message> updateMessage(@Valid @PathVariable Long id,@RequestBody Message message){
		Message updatedMessage=messageService.updateMessage(id,message);
		return new ResponseEntity<Message>(updatedMessage, HttpStatus.CREATED);
	}
}

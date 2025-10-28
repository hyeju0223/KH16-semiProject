package com.muzic.restcontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.error.TargetNotFoundException;
import com.muzic.service.AttachmentService;

@CrossOrigin
@RestController
@RequestMapping("/rest/post")
public class PostRestController {

	@Autowired
	private AttachmentService attachmentService;
	
	//임시 파일
	@PostMapping("/temp")
	public int temp(@RequestParam MultipartFile attach) throws IllegalStateException, IOException {
		if(attach.isEmpty()) {
			throw new TargetNotFoundException("파일이 존재하지 않습니다.");
		}
		return attachmentService.saveTemp(attach);
	}
	
	@PostMapping("/temps")
	public List<Integer> temps(@RequestParam(value = "attach") List<MultipartFile> attachList) 
			throws IllegalStateException, IOException {
		List<Integer> number = new ArrayList<>();
		
		for(MultipartFile attach : attachList) {
			if(attach.isEmpty() == false) {
				int attachmentNo = attachmentService.saveTemp(attach);
				number.add(attachmentNo);
			}
		}
		
		return number;
	}
}

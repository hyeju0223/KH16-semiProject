package com.muzic.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.dao.AttachmentDao;
import com.muzic.dto.AttachmentDto;
import com.muzic.error.TargetNotFoundException;
import com.muzic.service.AttachmentService;

@Controller
@RequestMapping("/attachment")
public class AttachmentController {
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	@GetMapping("/download")
	public ResponseEntity<ByteArrayResource> download(@RequestParam int attachmentNo) throws IOException{
		 
		// 데이터베이스에서 정보를 조회
		AttachmentDto attachmentDto = attachmentDao.selectOne(attachmentNo);
		if(attachmentDto == null) throw new TargetNotFoundException("존재하지 않는 파일입니다.");
		
		// 서비스에서 파일을 불러오기
		ByteArrayResource resource = attachmentService.load(attachmentNo);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_ENCODING , StandardCharsets.UTF_8.name())
				.header(HttpHeaders.CONTENT_TYPE, attachmentDto.getAttachmentType())
				.contentLength(attachmentDto.getAttachmentSize())
				.header(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition
						.attachment()
						.filename(attachmentDto.getAttachmentOriginalName(), StandardCharsets.UTF_8)
						.build().toString()
						)
				.body(resource);
	}
}

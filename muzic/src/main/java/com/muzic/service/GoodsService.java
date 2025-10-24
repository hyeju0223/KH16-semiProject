package com.muzic.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.GoodsDao;
import com.muzic.dto.GoodsDto;

@Service
public class GoodsService {

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private GoodsDao goodsDao;
	
	@Transactional
	public void insert(GoodsDto goodsDto, MultipartFile attach) throws IOException {
		int goodsNo = goodsDao.sequence();
		goodsDto.setGoodsNo(goodsNo);
		goodsDao.insert(goodsDto);
		if(attach.isEmpty()==false) {
			attachmentService.save(attach, "goods", goodsNo);
		}
		
	}
}

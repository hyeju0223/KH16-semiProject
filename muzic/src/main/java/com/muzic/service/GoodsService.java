package com.muzic.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.GoodsDao;
import com.muzic.domain.AttachmentCategory;
import com.muzic.dto.GoodsDto;
import com.muzic.error.TargetNotFoundException;

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
		if (attach.isEmpty() == false) {
			attachmentService.save(attach, "goods", goodsNo);
		}

	}

	@Transactional
	public boolean delete(int goodsNo) {

		
		//상품 삭제해도 주문내역에는 상품이미지가 보여야하기 때문에 이미지 안지우기로
		// 1. 첨부 파일 정보 조회 및 파일 삭제 (Goods 카테고리 사용)
//		String categoryValue = AttachmentCategory.GOODS.getCategoryName();
//		int attachmentNo = attachmentService.getAttachmentNoByParent(goodsNo, categoryValue);

		// 첨부 파일이 존재하면
//		if (attachmentNo != -1) {
//			attachmentService.delete(attachmentNo);
//		}

		// 2. 굿즈 정보 삭제 (DAO 사용)
		boolean result = goodsDao.delete(goodsNo);
		return result;
	}

	@Transactional
	public void edit(GoodsDto goodsDto, MultipartFile attach) throws IOException {

		// 1. 상품 정보 업데이트
		boolean updateSuccess = goodsDao.update(goodsDto);

		if (!updateSuccess) {
			throw new TargetNotFoundException("수정 대상 상품이 존재하지 않습니다.");
		}

		// 2. 첨부 파일 교체 처리 (새 첨부 파일이 있을 경우에만)
		if (attach != null && !attach.isEmpty()) {

			String categoryValue = AttachmentCategory.GOODS.getCategoryName();

			//기존 파일 번호 찾기 및 기존 첨부 파일 삭제
			int originalAttachmentNo = attachmentService.getAttachmentNoByParent(goodsDto.getGoodsNo(), categoryValue);

			if (originalAttachmentNo != -1) {
				attachmentService.delete(originalAttachmentNo);
			}

			//새 파일 저장 및 연결
			attachmentService.save(attach, categoryValue, goodsDto.getGoodsNo());

		}

	}
}

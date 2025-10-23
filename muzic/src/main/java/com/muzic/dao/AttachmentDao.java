package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.AttachmentDto;
import com.muzic.mapper.AttachmentMapper;

@Repository
public class AttachmentDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private AttachmentMapper attachmentMapper;
	
	public int sequence() {
		String sql = "select attachment_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public boolean insert(AttachmentDto attachmentDto) {
		String sql = "insert into attachment("
		        + "attachment_no, attachment_type, attachment_path, attachment_category, "
		        + "attachment_parent, attachment_original_name, attachment_stored_name, attachment_size"
		        + ") values ("
		        + " ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { 
				attachmentDto.getAttachmentNo(), attachmentDto.getAttachmentType(),
				attachmentDto.getAttachmentPath(), attachmentDto.getAttachmentCategory(), 
				attachmentDto.getAttachmentParent(), attachmentDto.getAttachmentOriginalName(), 
				attachmentDto.getAttachmentStoredName(), attachmentDto.getAttachmentSize()
			};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public AttachmentDto selectOne(int attachmentNo) {
		String sql = "select * from attachment where attachment_no = ?";
		Object[] params = { attachmentNo };
		List<AttachmentDto> list = jdbcTemplate.query(sql, attachmentMapper, params);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public boolean delete(int attachmentNo) {
		String sql = "delete from attachment where attachment_no = ?";
		Object[] params = { attachmentNo };
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	// 메소드에 매개변수 주실때 
	// AttachmentCategory category = AttachmentCategory.GOODS; // 해당하는 카테고리
	// String categoryValue = category.getValue(); // "goods"
	// 이렇게하고 findAttachmentNoByParent(goodsNo, categoryValue) 이렇게 메소드 호출해주세요
	public int findAttachmentNoByParent(int parentSeq, String category) {	
	    String sql = "select attachment_no from attachment where attachment_parent = ? and attachment_category = ?";
	    Object[] params = { String.valueOf(parentSeq), category };
	    try {
	        return jdbcTemplate.queryForObject(sql, Integer.class, params);	// queryForObject는 조회 컬럼의 데이터가 0건이거나 1건 이상일 시 에러가 생김.
	    } catch (EmptyResultDataAccessException e) {
	        return -1;	// 조회결과 없음 의미없는값(시퀀스는 음수일수 없으니)
	    }
	}
	
	public int findAttachmentNoByMemberId(String memberId, String category) {
	    String sql = "select attachment_no from attachment where attachment_parent = ? and attachment_category = ?";
	    Object[] params = { memberId, category};
	    try {
	        return jdbcTemplate.queryForObject(sql, Integer.class, params);
	    } catch (EmptyResultDataAccessException e) {
	        return -1;	
	    }
	}
	
	public AttachmentDto findAttachment(int attachmentNo) {
	    String sql = "select * from attachment where attachment_no = ?";
	    Object[] params = { attachmentNo };
	    List<AttachmentDto> list = jdbcTemplate.query(sql, attachmentMapper, params);
	    return list.isEmpty() ? null : list.get(0);
	}
	
}

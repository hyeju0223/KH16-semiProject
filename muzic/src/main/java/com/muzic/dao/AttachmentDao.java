package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		        + "attachment_no, attachment_type, attachment_category, attachment_parent, "
		        + "attachment_original_name, attachment_stored_name, attachment_size"
		        + ") values ("
		        + " ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { attachmentDto.getAttachmentNo(), attachmentDto.getAttachmentType(), 
				attachmentDto.getAttachmentCategory(), attachmentDto.getAttachmentParent(), 
				attachmentDto.getAttachmentOriginalName(), attachmentDto.getAttachmentStoredName(), 
				attachmentDto.getAttachmentSize()
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
}

package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.CommentsDto;
import com.muzic.mapper.CommentsMapper;

@Repository
public class CommentsDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CommentsMapper commentsMapper;
	
	//댓글 번호 시퀀스 조회
	public int sequence() {
		String sql = "select comments_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	//회원이 쓴 댓글 목록 조회 (관리자, 마이페이지 용)
//	public List<CommentsDto> selectList(String commentWriter) {
//		
//		String sql = "select * from comments where comment_writer = ? "
//				+ "order by comment_no desc";
//		Object[] params = {commentWriter};
//		return jdbcTemplate.query(sql, commentsMapper, params);
//	}
	
	//게시글에 달린 댓글 목록 조회
	public List<CommentsDto> selectList(int commentPost) {
		
		String sql = "select * from comments where comment_post = ? "
				+ "order by comment_no desc";
		Object[] params = {commentPost};
		return jdbcTemplate.query(sql, commentsMapper, params);
	}
	
	//댓글 삭제
	public boolean delete(int commentNo) {
		String sql = "delete from comments where comment_no = ?";
		Object[] params = {commentNo};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	//댓글 등록
	public void insert(CommentsDto commentsDto) {
		String sql = "insert into comments(comment_no, comment_post,"
				+ "comment_writer, comment_content, comment_wtime) "
				+ " values (?, ?, ?, ?, systimestamp)";
		Object[] params = {
				commentsDto.getCommentNo(), commentsDto.getCommentPost(),
				commentsDto.getCommentWriter(), commentsDto.getCommentContent()};
		jdbcTemplate.update(sql, params);
	}
	
	//댓글 수정
	public boolean update(CommentsDto commentsDto) {
		String sql = "update comments set "
				+ "comment_content = ?, comment_etime = systimestamp"
				+ " where comment_no = ?";
		Object[] params = {commentsDto.getCommentContent(), commentsDto.getCommentNo()};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	//댓글 조회
	public CommentsDto selectOne(int commentNo) {
		String sql = "select * from comments where comment_no = ?";
		Object[] params = {commentNo};
		List<CommentsDto> commentsList = jdbcTemplate.query(sql, commentsMapper, params);
		return commentsList.isEmpty() ? null : commentsList.get(0);
	}
	
	//좋아요 설정
	public boolean updateCommentsLike(int commentsNo, int count) {
		String sql = "update comments set comment_like = ? where comment_no = ?";
		Object[] params = {count, commentsNo};
		
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	//댓글 좋아요 수 업데아트
	public boolean updateCommentsLike(int commentsNo) {
		String sql = "update comments set comment_no = "
				+ "(select count(*) from comment_like where comment_no = ?) "
				+ "where comment_no = ?";
		Object[] params = {commentsNo, commentsNo};
		return jdbcTemplate.update(sql, params) > 0;
	}
}

package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.mapper.PostCommentMapper;
import com.muzic.vo.PostCommentsVO;

//게시글과 댓글이 함께 보이는 게시판용 데이터 불러오기
@Repository
public class PostCommentsDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PostCommentMapper postCommentMapper;
	
	//자유게시판에서 조회수, 좋아요 조회
	public List<PostCommentsVO> selectListByFree() {
		String sql = "select * from v_post_comments where post_mbti is null order by post_read desc, post_like desc";
		return jdbcTemplate.query(sql, postCommentMapper);
	}
	
	//mbti게시판에서 조회수, 좋아요 조회
	public List<PostCommentsVO> selectListByMbti() {
		String sql = "select * from v_post_comments where post_mbti is not null order by post_read desc, post_like desc";
		return jdbcTemplate.query(sql, postCommentMapper); 
	}
	
}

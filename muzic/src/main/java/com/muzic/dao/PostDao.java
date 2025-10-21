package com.muzic.dao;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.PostDto;
import com.muzic.mapper.PostMapper;

@Repository
public class PostDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PostMapper postMapper;
	
	public int sequence() {
		String sql = "select post_seq.nextval from dual";
		
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	//목록(자유게시판)
	public List<PostDto> selectList() {
		
		//모든 게시글의 정보를 조회하는 SQL 구문
		//post_no를 기준으로 내림차순(desc) 정렬하여 최신 글이 먼저 보이도록 정렬
		String sql = "select post_no, post_title, post_writer, post_mbti, post_content, "
	               + "post_music, post_wtime, post_etime, post_like, post_read "
	               + "from post where post_mbti is null or post_mbti = '' "
	               + "order by post_no desc";
		
		return jdbcTemplate.query(sql, postMapper);
	}
	
	//검색(전체 검색 쿼리)
	public List<PostDto> selectList(String column, String keyword) {
		
		//검색 가능한 칼럼 이름을 Set으로 정의
		Set<String> list = Set.of("post_title", "post_writer", "post_music");
		
		//유효성 검사
		//만약 사용자가 보낸 cloumn이 set에 포함되어있지 않다면
		if(!list.contains(column)) {
			//빈 리스트를 반환
			return List.of();
		}
		
		//instr로 칼럼 안 키워드 검색 조회 SQL 구문
		String sql = "select post_no, post_title, post_writer, post_mbti, post_content,"
				+"post_music, post_wtime, post_etime, post_like, post_read "
				+ "from post where instr(#1, ?) > 0 "
				+ "order by post_no desc";
		
		//사용자가 입력한 칼럼 이름 삽입
		sql = sql.replace("#1", column);
		
		//파라미터 키워드 삽입
		Object[] params = {keyword};
		
		//SQl구문 실행, 키워드를 파라미터로 넘기기
		// 과를 PostDto 객체 리스트로 매핑하여 반환합니다.
		return jdbcTemplate.query(sql, postMapper, params);
	}
	
	//목록(mbti개시판)
	public List<PostDto> selectMbtiList(String memberMbti) {
		String sql = "select post_no, post_title, post_writer, post_mbti, post_content, "
				+ "post_music, post_wtime, post_etime, post_like, post_read "
				+ "from post where post_mbti = ? "
				+ "order by post_no desc";
		
		Object[] params = {memberMbti};
		
		return jdbcTemplate.query(sql, postMapper, params);
	}
	
	//검색(mbti 게시판에서 검색)
	public List<PostDto> selectMbtiList(String mbti, String column, String keyword) {
	    
	    Set<String> allowedColumns = Set.of("post_title", "post_writer", "post_music");
	    
	    // 유효성 검사
	    if (!allowedColumns.contains(column)) {
	        return List.of();
	    }
	    
	    // SQL에 mbti 조건과 검색 조건을 모두 포함
	    String sql = "select post_no, post_title, post_writer, post_mbti, post_content, "
	            + "post_music, post_wtime, post_etime, post_like, post_read "
	            + "from post where post_mbti = ? and instr(#1, ?) > 0 "
	            + "order by post_no desc";
	    
	    sql = sql.replace("#1", column);
	    Object[] params = { mbti, keyword };
	    
	    return jdbcTemplate.query(sql, postMapper, params);
	}

	
	//등록
	public void insert(PostDto postDto) {
		String sql = "insert into post(post_no, post_title, post_writer, "
				+ "post_mbti, post_music, post_wtime, post_content, post_norice) "
				+ "values (post_seq nocache, ?, ?, ?, ?, ?, ?, ?)";
		
		Object[] params = {postDto.getPostTitle(), postDto.getPostWriter(),
				postDto.getPostMbti(), postDto.getPostMusic(), postDto.getPostWtime(),
				postDto.getPostContent(), postDto.getPostNotice()};
		
		jdbcTemplate.update(sql, params);
	}

	//상세보기
	public PostDto selectOne(int postNo) {
		
		 // 게시글 번호를 기준으로 게시글 정보를 조회하는 SQL 쿼리문
		String sql = "select * from post where post_no=?";
		Object[] params = {postNo};
		
		List<PostDto> postList = jdbcTemplate.query(sql, postMapper, params);
		
		return postList.isEmpty() ? null : postList.get(0);
	}

	//수정
	//수정 성공 여부를 명확하게 확인하기 위해 논리형으로 반환(true/ false)
	public boolean update(PostDto postDto) {

		String sql = "update post set post_title=?, post_music=?, post_content=?,"
				+ "post_notice=?, post_etime=systimestamp "
				+ "where post_no=?";
		
		Object[] params = {postDto.getPostTitle(), postDto.getPostMusic(),
				postDto.getPostContent(), postDto.getPostNotice(), 
				postDto.getPostNo()};
		
		return jdbcTemplate.update(sql, params) > 0;
	}

	//삭제
	//삭제 성공 여부를 명확하게 확인하기 위해 논리형으로 반환(true/ false)
	public boolean delete(int postNo) {
		
		String sql = "delete post where post_no = ?";
		
		Object[] params = {postNo};
		
		return jdbcTemplate.update(sql, params) > 0;
	}

}

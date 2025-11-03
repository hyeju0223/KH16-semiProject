package com.muzic.dao;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.condition.SearchCondition;
import com.muzic.dto.PostDto;
import com.muzic.mapper.PostListMapper;
import com.muzic.mapper.PostMapper;
import com.muzic.vo.PostVO;

@Repository
public class PostDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PostMapper postMapper;
	@Autowired
	private PostListMapper postListMapper;
	
	//게시글 번호시쿠너스 조회
	public int sequence() {
		String sql = "select post_seq.nextval from dual";
		
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	//공지사항 게시글 목록 조회
	public List<PostVO> selectListNotice(SearchCondition searchCondition) {
		//만약 칼럼, 키워드 중 하나라도 null이면
		if(searchCondition.isList()) {
			//전체 목록 조회
			String sql = "select * from post_list "
					+ "where post_notice = 'Y' order by post_no desc";
			return jdbcTemplate.query(sql,  postListMapper);
		}
		
		//검색 목록 조회
		String sql = "select * from post_list where post_notice = 'Y' "
				+ "and instr(#1, ?) order by post_no desc";
		sql = sql.replace("#1", searchCondition.getColumn());
		
		Object[] params = {searchCondition.getKeyword()};
		
		return jdbcTemplate.query(sql, postListMapper, params);
	}
	
	//자유게시판 전체 글 개수
	public int count(SearchCondition searchCondition) {
		String column = searchCondition.getColumn();
	    
	    //검색 조건이 없을 때 (column 또는 keyword가 null/빈값)
	    if (column == null || searchCondition.getKeyword() == null || searchCondition.getKeyword().isBlank()) {
	        //자유 게시판 조건 (post_mbti is null or post_mbti = '') 추가
	        String sql = "select count(*) from post where post_mbti is null or post_mbti = ''";
	        return jdbcTemplate.queryForObject(sql, Integer.class);
	    }
	    
	    //검색 조건이 있을때 조건에 맞는 글만 카운드
	    String sql = "select count(*) from post where (post_mbti is null or post_mbti = '') and instr(#1, ?) > 0";
	    
	    sql = sql.replace("#1", column);
	    Object[] params = { searchCondition.getKeyword() };
	    return jdbcTemplate.queryForObject(sql, Integer.class, params);
	}
	
    //게시판 두개에서 공통으로 사용할 코드 빼두기 (유지보수 / 가독성때문에)
	//post와 member 테이블 join
    String columnList = "P.post_no, P.post_title, P.post_writer, P.post_mbti, P.post_content, "
            + "P.post_music, P.post_wtime, P.post_etime, P.post_like, P.post_read, P.post_notice, "
            + "M.member_id, M.member_nickname, M.member_mbti, M.member_role";

    //from - 어디서 데이터를 가져올지
    //멤버 테이블을 가져와서 게시판 작성자가 멤버 아이디랑 같다는 뜻 
    String fromClause = "from post P left join member M on P.post_writer = M.member_id";
	
	//자유게시판 목록보기, 검색하기 , 페이징 기능 통합
	public List<PostVO> selectFreeList(SearchCondition searchCondition) {

		if(searchCondition.isList()) { // 목록 모드
			String sql = 
				    "select * from ("
				    	+ "select rownum rn, TMP.* from ("
				  			+ "select " + columnList + " "//조인한 테이블 데이터 조회
				  			+ fromClause
				  			+ " where P.post_mbti IS NULL OR P.post_mbti = '' "
				  			+ "order by P.post_notice desc, P.post_no desc "
				  		+ ") TMP"
				  + ") where rn between ? and ?";
			
			Object[] params = {searchCondition.getStart(), searchCondition.getEnd()};
			
			return jdbcTemplate.query(sql, postListMapper, params);
		}
		else { // 검색 모드
			String sql = 
				    "select * from ("
				    	+ "select rownum rn, TMP.* from ("
				  			+ "select " + columnList + " "
				  			+ fromClause
				  			+ " where (P.post_mbti is null or P.post_mbti = '') "
				  			+ " and instr(#1, ?) > 0 "
				  			+ "order by P.post_notice desc, P.post_no desc "
				  		+ ") TMP"
				  + ") where rn between ? and ?";
			
			//사용자가 입력한 칼럼 이름 삽입 (테이블 별칭 P. 추가)
			sql = sql.replace("#1", "P." + searchCondition.getColumn());
			
			//파라미터 키워드 삽입
			Object[] params = {searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd()};
			
			return jdbcTemplate.query(sql, postListMapper, params);
		}
		
	}
    
	// mbti게시판 목록보기, 검색하기, 페이징 기능 통합
	public List<PostVO> selectMbtiList(String mbti, SearchCondition searchCondition) {
	    
	    //검색 조건 유무 확인 및 SQL 구조 분리
	    if (searchCondition.getKeyword() != null && !searchCondition.getKeyword().isBlank()) {

	    	//검색 결과 보는 구문
	        String sql = 
	            "select * from ("
	                + "select rownum rn, TMP.* from ("
	                    + "select " + columnList + " "
	                    + fromClause
	                    + " where P.post_mbti = ? " //MBTI 필터링
	                    + " and instr(#1, ?) > 0 " //검색 키워드 필터링
	                    + " order by P.post_notice desc, P.post_no desc "
	                + ") TMP"
	            + ") where rn between ? and ?";

	        // 사용자가 입력한 칼럼 이름 삽입
	        sql = sql.replace("#1", "P." + searchCondition.getColumn());

	        Object[] params = {
	            mbti, 
	            searchCondition.getKeyword(), 
	            searchCondition.getStart(), 
	            searchCondition.getEnd()
	        };
	        
	        return jdbcTemplate.query(sql, postListMapper, params);
	        
	    } else {
	    	//목록 보는 구문
	        String sql = 
	            "select * from ("
	                + "select rownum rn, TMP.* from ("
	                    + "select " + columnList + " "
	                    + fromClause
	                    + " where P.post_mbti = ? " //MBTI 필터링만 적용
	                    + " order by P.post_notice desc, P.post_no desc "
	                + ") TMP"
	            + ") where rn between ? and ?";
	        
	        Object[] params = {
	            mbti, 
	            searchCondition.getStart(), 
	            searchCondition.getEnd()
	        };
	        
	        return jdbcTemplate.query(sql, postListMapper, params);
	    }
	}
	
    // MBTI 게시판의 전체 글 카운트 (목록 및 검색 카운트 통합 처리)
    public int countMbti(String mbti, SearchCondition searchCondition) { 
        String column = searchCondition.getColumn();
        
        if (column == null || searchCondition.getKeyword() == null || searchCondition.getKeyword().isBlank()) {
            String sql = "select count(*) from post where post_mbti = ?";
            Object[] params = {mbti};
            return jdbcTemplate.queryForObject(sql, Integer.class, params);
        }

        String sql = "select count(*) from post where post_mbti = ? and instr(#1, ?) > 0";
        sql = sql.replace("#1", column);
        Object[] params = { mbti, searchCondition.getKeyword() };
        return jdbcTemplate.queryForObject(sql, Integer.class, params);
    }

	
	//등록
	public void insert(PostDto postDto) {
		String sql = "insert into post(post_no, post_title, post_writer, "
				+ "post_mbti, post_music, post_wtime, post_content, post_notice) "
				+ "values (?, ?, ?, ?, ?, SYSTIMESTAMP, ?, ?)";
		
		Object[] params = {postDto.getPostNo() ,postDto.getPostTitle(), postDto.getPostWriter(),
				postDto.getPostMbti(), postDto.getPostMusic(),
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
	
	//좋아요 설정
	public boolean updatePostLike(int postNo, int count) {
		String sql = "update post set post_like = ? where post_no = ?";
		Object[] params = {count, postNo};
		
		return jdbcTemplate.update(sql, params) > 0;
	}
	public boolean updatePostLike(int postNo) {
		String sql = "update post set post_like = "
				+ "(select count(*) from post_like where post_no = ?) "
				+ "where post_no = ?";
		Object[] params = {postNo, postNo};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	//조회수
	public boolean postRead(int postNo) {
		String sql = "update post set post_read = post_read + 1 where post_no=?";
		Object[] params = {postNo};
		return jdbcTemplate.update(sql, params) > 0;
	}
}




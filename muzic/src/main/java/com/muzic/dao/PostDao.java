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
import com.muzic.vo.PageVO;
import com.muzic.vo.PostVO;

@Repository
public class PostDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PostMapper postMapper;
	@Autowired
	private PostListMapper postListMapper;
	
	public int sequence() {
		String sql = "select post_seq.nextval from dual";
		
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public List<PostVO> selectListNotice(SearchCondition searchCondition) {
		//공지사항 검색용 매핑
		if(searchCondition.isList()) {
			String sql = "select * from board_list "
					+ "where post_notice = 'Y' order by post_no desc";
			return jdbcTemplate.query(sql,  postListMapper);
		}
		
		String sql = "select * from post_list where post_notice = 'Y' "
				+ "and instr(#1, ?) orber by post_no desc";
		sql = sql.replace("#1", searchCondition.getColumn());
		
		Object[] params = {searchCondition.getKeyword()};
		
		return jdbcTemplate.query(sql, postListMapper, params);
	}
	
	public int count(SearchCondition searchCondition) {
		String column = searchCondition.getColumn();
	    
	    // 1. 검색 조건이 없을 때 (column 또는 keyword가 null/빈값)
	    if (column == null || searchCondition.getKeyword() == null || searchCondition.getKeyword().isBlank()) {
	        // [수정된 부분] 자유 게시판 조건 (post_mbti is null or post_mbti = '') 추가
	        String sql = "select count(*) from post where post_mbti is null or post_mbti = ''";
	        return jdbcTemplate.queryForObject(sql, Integer.class);
	    }

	    String sql = "select count(*) from post where (post_mbti is null or post_mbti = '') and instr(#1, ?) > 0";
	    
	    sql = sql.replace("#1", column);
	    Object[] params = { searchCondition.getKeyword() };
	    return jdbcTemplate.queryForObject(sql, Integer.class, params);
	}
	
    //게시판 두개에서 공통으로 사용할 코드 빼두기
    String columnList = "P.post_no, P.post_title, P.post_writer, P.post_mbti, P.post_content, "
            + "P.post_music, P.post_wtime, P.post_etime, P.post_like, P.post_read, P.post_notice, "
            + "M.member_id, M.member_nickname, M.member_mbti, M.member_role";

    String fromClause = "from post P left join member M on P.post_writer = M.member_id";
	
	//목록보기, 검색하기 , 페이징 기능 통합
	public List<PostVO> selectFreeList(SearchCondition searchCondition) {

		if(searchCondition.isList()) { // 목록 모드
			String sql = 
				    "select * from ("
				    	+ "select rownum rn, TMP.* from ("
				  			+ "select " + columnList + " "
				  			+ fromClause
				  			+ " where P.post_mbti IS NULL OR P.post_mbti = '' "
				  			+ "order by P.post_no desc"
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
				  			+ " where P.post_mbti IS NULL OR P.post_mbti = '' "
				  			+ "and instr(#1, ?) > 0 "
				  			+ "order by P.post_no desc"
				  		+ ") TMP"
				  + ") where rn between ? and ?";
			
			//사용자가 입력한 칼럼 이름 삽입 (테이블 별칭 P. 추가)
			sql = sql.replace("#1", "P." + searchCondition.getColumn());
			
			//파라미터 키워드 삽입
			Object[] params = {searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd()};
			
			return jdbcTemplate.query(sql, postListMapper, params);
		}
		
	}
	
	public List<PostVO> selectMbtiList(String mbti, SearchCondition searchCondition) {
        
        String sql = 
                "select * from ("
                    + "select rownum rn, TMP.* from ("
                        + "select " + columnList + " "
                        + fromClause
                        + " where P.post_mbti = ? " // MBTI 조건
                        + " order by P.post_no desc"
                    + ") TMP"
                + ") where rn between ? and ?";
        
        // 파라미터: [mbti, 시작 번호, 끝 번호]
        Object[] params = {mbti, searchCondition.getStart(), searchCondition.getEnd()};
        
        return jdbcTemplate.query(sql, postListMapper, params);
    }
    
    // 검색하기 + 페이징 (MBTI 게시판)
    public List<PostVO> selectMbtiListSearch(String mbti, SearchCondition searchCondition) {
        
    	String search = "";
    	
        if (searchCondition != null && 
            searchCondition.getColumn() != null && 
            !searchCondition.getColumn().isBlank() && 
            searchCondition.getKeyword() != null && 
            !searchCondition.getKeyword().isBlank()) {

            search = " and instr(P." + searchCondition.getColumn() + ", ?) > 0 ";
        }

        String sql = 
                "select * from ("
                    + "select rownum rn, TMP.* from ("
                        + "select " + columnList + " "
                        + fromClause
                        + " where P.post_mbti = ? "
                        + search
                        + " order by P.post_no desc"
                    + ") TMP"
                + ") where rn between ? and ?";

        Object[] params = {mbti, searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd()};
        
        return jdbcTemplate.query(sql, postListMapper, params);
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

}




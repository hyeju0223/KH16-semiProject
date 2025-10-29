package com.muzic.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SearchCondition {
	@Builder.Default
	private int page = 1;  //현재 페이지 번호
    @Builder.Default
    private int size = 10; //한 페이지에 표시되는 게시글 수
    private int allData; //총 데이터 수
    @Builder.Default
    private int blockNum = 10; // 표시될 블럭 개수
    private String sortType;
    private String column;
    private String keyword;
    private boolean mixedJamo;
    
    //페이지 네비게이터
    public int getTotalPage() {
        int totPage = (int) Math.ceil((double) allData/ size); //총 페이지 개수
        return totPage;
    }
    public int getStartPage() {
        int strPage = ((page - 1) / blockNum) * blockNum + 1; //블럭 시작 번호
        return strPage;
    }
    public int getEndPage() {
        int endPage = ((page-1) / blockNum) * blockNum + blockNum; //블럭 종료 번호
        return endPage;
    }

  //DB에서 띄울 게시글 범위 계산
    public int getStart() {
        return page * size - (size - 1); //첫 게시물
    }
    public int getEnd() {
        return page * size; //마지막 게시물
    }

	//  검색용 메소드
	  public boolean isSearch() {
	      return column != null && keyword != null; //둘 다 null이 아니라면 true 반환
	  }
	  public boolean isList() {
	      return column == null || keyword == null; //둘 중 하나라도 null이면
	  }
	  
	  //주소에 추가 될 파라미터
	    public String getParams() {
	    	StringBuffer param = new StringBuffer("size=" + size);
	        if(isSearch()) {
	            //search()가 true라면 주소에 파라미터도 반환
	        	param.append("&column=").append(column)
	            .append("&keyword=").append(keyword);
	        }
	          return param.toString();
	    }
}
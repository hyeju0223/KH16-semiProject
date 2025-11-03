package com.muzic.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SearchCondition {
    @Builder.Default // 해당 필드에 선언 시 정의된 초기값을 적용하도록 지시
    private int page = 1;  // 현재 페이지 번호
    @Builder.Default
    private int size = 10; // 한 페이지 게시글 수
    private int allData;   // 총 데이터 개수
    @Builder.Default
    private int blockNum = 10; // 페이지 네비게이터에 보여줄 블럭 크기 (10페이지씩)
    private String sortType;
    private String column;
    private String keyword;
    private boolean mixedJamo;

    // 총 페이지 개수 계산
    public int getTotalPage() {
        if (allData == 0) return 1;
        return (int) Math.ceil((double) allData / size);
    }

    // 현재 블럭의 시작 페이지 번호
    public int getStartPage() {
        return ((page - 1) / blockNum) * blockNum + 1;
    }

    // 현재 블럭의 끝 페이지 번호 (총 페이지 수보다 클 수 있으므로 조정)
    public int getEndPage() {
        int endPage = getStartPage() + blockNum - 1;
        int totalPage = getTotalPage();
        return Math.min(endPage, totalPage);
    }

    // DB에서 띄울 게시글 범위
    public int getStart() {
        return (page - 1) * size + 1;
    }

    public int getEnd() {
        return page * size;
    }

    // 검색 관련
    public boolean isSearch() {
        return column != null && keyword != null;
    }

    public boolean isList() {
        return column == null || keyword == null;
    }

    // URL 파라미터
    public String getParams() {
        StringBuilder param = new StringBuilder("size=" + size);
        if (isSearch()) {
            param.append("&column=").append(column)
                 .append("&keyword=").append(keyword);
        }
        return param.toString();
    }

    // 현재 블럭이 첫 번째 블럭인지
    public boolean isFirstBlock() {
        return getStartPage() == 1;
    }

    // 이전 블럭 마지막 페이지 번호
    public int getPrevPage() {
        int prev = getStartPage() - 1;
        return Math.max(prev, 1);
    }

    // 현재 블럭이 마지막 블럭인지
    public boolean isLastBlock() {
        return getEndPage() >= getTotalPage();
    }

    // 다음 블럭 시작 페이지 번호
    public int getNextPage() {
        int next = getEndPage() + 1;
        int total = getTotalPage();
        return Math.min(next, total);
    }
}

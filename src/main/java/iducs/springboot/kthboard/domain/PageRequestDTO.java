package iducs.springboot.kthboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {   // 페이지 처리를 위한 객체
    private int page;   // 요청하는 페이지
    private int size;   // 한 페이지에 나타나는 수
    private String type;    // e - email, p - phone, a - address로 검색 항목
    private String keyword; // 검색어
    private String sorting;     // 정렬상태

    public PageRequestDTO() {
        this.page = 1;
        this.size = 5;  // 5개의 사용자 정보, 게시물, 기사(article)가 나타남
        this.sorting = "";
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);    // page 자리는 2임. 그래서 page - 1을 함
    }
}

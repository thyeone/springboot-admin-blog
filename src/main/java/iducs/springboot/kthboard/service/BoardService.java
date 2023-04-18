package iducs.springboot.kthboard.service;

import iducs.springboot.kthboard.domain.Board;
import iducs.springboot.kthboard.domain.PageRequestDTO;
import iducs.springboot.kthboard.domain.PageResultDTO;
import iducs.springboot.kthboard.entity.BoardEntity;
import iducs.springboot.kthboard.entity.MemberEntity;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {
    Long register(Board dto);   // Board : DTO or Domain, create
    PageResultDTO<Board, Object[]> getList(PageRequestDTO pageRequestDTO);  // read list
    Board getById(Long bno);
    Long modify(Board dto);
    void deleteWithRepliesById(Long bno);

    /**
     * 문법이 추가된 것 (Java8 ~)
     * 저 인터페이스로부터 구현하는 메서드들은
     */
    default BoardEntity dtoToEntity(Board dto) {
        MemberEntity member = MemberEntity.builder()
                .seq(dto.getWriterSeq())
                .build();
        BoardEntity boardEntity = BoardEntity.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .views(dto.getViews())
                .category(dto.getCategory())
                .writer(member)
                .build();
        return boardEntity;
    }
    default Board entityToDto(BoardEntity entity, MemberEntity member, Long replyCount) {
        Board dto = Board.builder()
                .bno(entity.getBno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .views(entity.getViews())
                .category(entity.getCategory())
                .writerSeq(member.getSeq())
                .writerId(member.getId())
                .writerName(member.getName())
                .writerEmail(member.getEmail())
                .writerDeny(member.getDeny())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .replyCount(replyCount.intValue())
                .build();
        return dto;
    }
}

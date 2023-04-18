package iducs.springboot.kthboard.service;


import iducs.springboot.kthboard.domain.Board;
import iducs.springboot.kthboard.domain.PageRequestDTO;
import iducs.springboot.kthboard.domain.PageResultDTO;
import iducs.springboot.kthboard.entity.BoardEntity;
import iducs.springboot.kthboard.entity.MemberEntity;
import iducs.springboot.kthboard.repository.BoardRepository;
import iducs.springboot.kthboard.repository.ReplyRepository;
import iducs.springboot.kthboard.repository.SearchBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2 // 화면에 출력되지 않고 로그 창에 뜸 -> 로그 파일에 저장됨, 시스템에 오류가 생겼을 때 등 오류를 남기기 위해서 사용하는 Annotation(도구)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    SearchBoardRepository searchBoardRepository;

    @Override   // From JpaRepository
    public Long register(Board dto) {   // Controller -> DTO 객체 -> Service
        log.info("board register : " + dto);
        BoardEntity boardEntity = dtoToEntity(dto);
        boardRepository.save(boardEntity);
        return boardEntity.getBno();    // 게시물 번호
    }

    @Override
    public PageResultDTO<Board, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(">>>>>" + pageRequestDTO);
        Function<Object[], Board> fn =
                (entities -> entityToDto((BoardEntity) entities[0], // entities를 entity로 이름을 바꿔도 상관없음
                        (MemberEntity) entities[1], (Long) entities[2]));
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        return new PageResultDTO<>(result, fn);
    }

    @Transactional
    @Override
    public Board getById(Long bno) {
        boardRepository.updateView(bno);
        Object result = boardRepository.getBoardByBno(bno);
        Object[] en = (Object[]) result;
        return entityToDto((BoardEntity) en[0],
                (MemberEntity) en[1], (Long) en[2]);    // Board
    }

    @Override
    public Long modify(Board dto) {
        Optional<BoardEntity> result = boardRepository.findById(dto.getBno());
        BoardEntity boardEntity = null;
        if(result.isPresent()) {
            boardEntity = (BoardEntity) result.get();
            boardEntity.changeTitle(dto.getTitle());
            boardEntity.changeContent(dto.getContent());
            boardRepository.save(boardEntity);
        }
        return boardEntity.getBno();
    }

    @Transactional
    @Override
    public void deleteWithRepliesById(Long bno) {
        replyRepository.deleteByBno(bno);
        boardRepository.deleteById(bno);
    }
}

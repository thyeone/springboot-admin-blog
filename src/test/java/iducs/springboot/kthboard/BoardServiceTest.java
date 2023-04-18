package iducs.springboot.kthboard;

import iducs.springboot.kthboard.domain.Board;
import iducs.springboot.kthboard.entity.BoardEntity;
import iducs.springboot.kthboard.repository.BoardRepository;
import iducs.springboot.kthboard.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;

    @Test
    public void testRegister() {
        IntStream.rangeClosed(1, 47).forEach(i -> {
            Board dto = Board.builder()
                    .title("title" + i)
                    .content("Content...")
                    .views(0L)
                    .writerSeq(Long.valueOf("" + i))
                    .build();
            boardService.register(dto);
        });
    }

    @Transactional
    @Test
    public void testLazyLoading() {
        Optional<BoardEntity> result = boardRepository.findById(10L);
        BoardEntity boardEntity = result.get();
        System.out.println(boardEntity.getTitle());
        System.out.println(boardEntity.getContent());
    }

    @Test
    public void testDeleteWithRepliesById() {
        Long bno = 3L;
        boardService.deleteWithRepliesById(bno);
    }
}

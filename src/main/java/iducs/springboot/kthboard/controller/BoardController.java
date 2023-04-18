package iducs.springboot.kthboard.controller;

import iducs.springboot.kthboard.domain.Board;
import iducs.springboot.kthboard.domain.PageRequestDTO;
import iducs.springboot.kthboard.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/boards")
public class BoardController {

    public final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("")
    public String getBoards(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("list", boardService.getList(pageRequestDTO));
        return "boards/list";    // view resolving
    }

    @GetMapping("/regform")
    public String getRegform(Model model) {
        model.addAttribute("board", Board.builder().build());
        return "boards/regform";  // view resolving
    }

    @PostMapping("")
    public String postBoard(@ModelAttribute("board") Board board, PageRequestDTO pageRequestDTO, Model model) {
        boardService.register(board);
        model.addAttribute("list", boardService.getList(pageRequestDTO));
        return "boards/list";
    }

    @GetMapping("/{bno}")
    public String getBoardView(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.getById(bno);
        model.addAttribute("board", board);
        return "boards/read";
    }

    @GetMapping("/{bno}/upform")
    public String getUpForm(@PathVariable("bno") Long bno, Model model) {
        // 정보를 전달받을 빈(empty) 객체를 보냄
        Board board = boardService.getById(bno);
        model.addAttribute("board", board);
        return "boards/upform";
    }

    // PutMapping - PUT, DELETE는 REST 규약에서 보안에 취약한 method이므로 POST로 변경
    @PostMapping("/{bno}/upform")
    public String putBoard(@ModelAttribute("board") Board board, Model model) { // ModelAttribute는 form에서 가져오는 거
        boardService.modify(board);
        model.addAttribute("board", board);
        return "boards/read";
    }

    @GetMapping("/{idx}/delform")
    public String getDelForm(@PathVariable("idx") Long bno, Model model) {
        // 정보를 전달받을 빈(empty) 객체를 보냄
        Board board = boardService.getById(bno);
        model.addAttribute("board", board);
        return "boards/delform";
    }

    // DeleteMapping - PUT, DELETE는 REST 규약에서 보안에 취약한 method이므로 POST로 변경
    @PostMapping("/{bno}/delform")
    public String deleteBoard(@ModelAttribute("bno") Long bno) {
        boardService.deleteWithRepliesById(bno);
        return "redirect:/";
    }
}

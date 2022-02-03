package com.study.board.controller;

import com.study.board.entitiy.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/board/write") //localhost:8080/board/write 이 주소로 접속하면 boardwrite.html을 보여줌
    public String boardWriteForm(){
        return "boardwrite";
    }

    @PostMapping("/board/writepro") //작성을 완료했을 시
    public String boardWritePro(Board board, Model model, MultipartFile file) throws IOException {

        boardService.boardwrite(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    //Model 은 HashMap의 형태를 띄고있다.
    //addAttribute를 통해 모델에 원하는 속성과 그것에 대한 값을 주어 전달할 뷰에 데이터를 전송할 수 있다.

    //리스트에 페이징처리
    @GetMapping("/board/list")
    public String boardlist(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword){
        Page<Board> list = null;

        if(searchKeyword == null){
            list = boardService.boardList(pageable);
        }
        else{
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        //게시글을 전부 리턴받은걸 list라는 이름으로 전부 보냄        //보낸걸 boardlist.html에서 받음
        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardlist";
    }

    @GetMapping("/board/view/{id}") //localhost:8080/board/view?id=1
    public String boardView(Model model, @PathVariable("id") Integer id){
        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    //PathVariable : 경로에 변수를 넣어주는 어노테이션
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardView(id));
        return  "boardmodify";
    }


    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws IOException {
        Board boardTemp = boardService.boardView(id);

        System.out.println(boardTemp.getTitle());
        System.out.println(boardTemp.getContent());

//        boardTemp.setTitle(board.getTitle());
//        boardTemp.setContent(board.getContent());


        //수정했던 Board값을 받아와서 재작성
        boardService.boardwrite(boardTemp, file);

        model.addAttribute("message", "수정이 완료되었습니다");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }
}

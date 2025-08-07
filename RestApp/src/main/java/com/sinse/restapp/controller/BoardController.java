package com.sinse.restapp.controller;


import com.sinse.restapp.domain.Board;
import com.sinse.restapp.model.board.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class BoardController {

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/test")
    public String test() {
        return "minzino";
    }

    //게시판 목록 요청 처리
    @GetMapping("/boards")
    public List selectAll(){
        log.debug("목록 요청 받음");
        List list = new ArrayList();
        list.add("apple");
        list.add("banana");
        list.add("grape");
        return list;
    }

    //글쓰기 요청
    @PostMapping("/boards")
    //json 문자열로 전송된 파라미터와 서버측의 모델과의 자동 매핑 (주의 고전적 스프링에도 지원되었었음)
    public ResponseEntity<String> regist(@RequestBody Board board){
        boardService.insert(board);
        return ResponseEntity.ok("success");
    }


}

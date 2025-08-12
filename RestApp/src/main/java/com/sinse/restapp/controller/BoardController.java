package com.sinse.restapp.controller;


import com.sinse.restapp.domain.Board;
import com.sinse.restapp.model.board.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        List list = boardService.selectAll();

        return list;
    }

    //글쓰기 요청
    @PostMapping("/boards")
    //json 문자열로 전송된 파라미터와 서버측의 모델과의 자동 매핑 (주의 고전적 스프링에도 지원되었었음)
    public ResponseEntity<String> regist(@RequestBody Board board){
        boardService.insert(board);
        return ResponseEntity.ok("success");
    }

    //결국 아래의 url 에 대해 Restful 한 URL을 이해하고 있음
    @GetMapping("/boards/{board_id}")
    public Board select(@PathVariable int board_id) {
        Board board = boardService.select(board_id);
        return board;
    }

    //수정요청 처리
    @PutMapping("/boards/{board_id}")
    public ResponseEntity<String> update(@RequestBody Board board, @PathVariable("board_id") int board_id) {
        board.setBoard_id(board_id);//경로로 전송된 파라미터를 다시한번 확인 차 모델에 대입
        boardService.update(board);
        return ResponseEntity.ok("success");
    }

    //수정요청 처리
    @DeleteMapping("/boards/{board_id}")
    public ResponseEntity<String> delete(@PathVariable("board_id") int board_id) {
        boardService.delete(board_id);
        return ResponseEntity.ok("success");
    }

}










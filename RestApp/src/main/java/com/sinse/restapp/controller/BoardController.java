package com.sinse.restapp.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class BoardController {

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
    public ResponseEntity<String> regist(){

        return ResponseEntity.ok("success");
    }


}

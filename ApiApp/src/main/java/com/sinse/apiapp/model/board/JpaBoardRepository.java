package com.sinse.apiapp.model.board;

import com.sinse.apiapp.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBoardRepository extends JpaRepository<Board, Integer> {
    //목록 생략 findAll()
    //한건 가져오기  findById()
    //글쓰기 save()
    //수정 save()
    //삭제
}

package com.sinse.restapp.model.board;

//고전적으로 사용해왔던 SqlSessionTemplate 없이, mybatis를 실행할 수 있는 방법

import com.sinse.restapp.domain.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    public List selectAll();
    public Board select(int board_id);
    public void insert(Board board);
    public void update(Board board);
    public void delete(int board_id);
}

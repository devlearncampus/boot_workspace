package com.sinse.xmlapp.model.board;

import com.sinse.xmlapp.domain.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//MyBatis 의 xml을 직접 제어
@Mapper
public interface BoardMapper {
    public List selectAll();
    public Board select(int board_id);
    public void insert(Board board);
    public void update(Board board);
    public void delete(int board_id);
}

package com.sinse.apiapp.model.board;

import com.sinse.apiapp.domain.Board;

import java.util.List;

public interface BoardService {
    public List<Board> selectAll();
    public Board select(int boardId);
    public void regist(Board board);
    public void update(Board board);
    public void delete(int boardId);
}

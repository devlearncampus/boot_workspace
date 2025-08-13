package com.sinse.xmlapp.model.board;

import com.sinse.xmlapp.domain.Board;

import java.util.List;

public interface BoardService {
    public List<Board> selectAll();
    public Board select(int board_id);
    public void insert(Board board);
    public void update(Board board);
    public void delete(int board_id);
}

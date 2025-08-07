package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;

import java.util.List;

public interface BoardDAO {
    public List selectAll();
    public Board select(int board_id);
    public void insert(Board board);
    public Board update(Board board);
    public void delete(int board_id);
}

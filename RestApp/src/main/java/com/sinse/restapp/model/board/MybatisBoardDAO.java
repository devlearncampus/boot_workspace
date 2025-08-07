package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;
import com.sinse.restapp.exception.BoardException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mybatisBoardDAO")
public class MybatisBoardDAO implements BoardDAO{

    private BoardMapper boardMapper;

    public MybatisBoardDAO(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public List selectAll() {
        return boardMapper.selectAll();
    }

    @Override
    public Board select(int board_id) {
        return boardMapper.select(board_id);
    }

    @Override
    public void insert(Board board) throws DataAccessException {
        boardMapper.insert(board);
    }

    @Override
    public Board update(Board board) throws DataAccessException {
        boardMapper.update(board);
        return board;
    }

    @Override
    public void delete(int board_id) throws DataAccessException {
        boardMapper.delete(board_id);
    }
}

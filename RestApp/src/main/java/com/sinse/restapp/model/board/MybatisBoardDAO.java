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
        try {
            boardMapper.insert(board);
        } catch (DataAccessException e) {
            throw new BoardException("글쓰기 실패",e);
        }
    }

    @Override
    public Board update(Board board) throws DataAccessException {
        try {
            boardMapper.update(board);
        } catch (DataAccessException e) {
            throw new BoardException("글수정 실패",e);
        }
        return board;
    }

    @Override
    public void delete(int board_id) throws DataAccessException {
        try {
            boardMapper.delete(board_id);
        } catch (DataAccessException e) {
            throw new BoardException("삭제 실패",e);
        }
    }
}

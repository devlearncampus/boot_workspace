package com.sinse.xmlapp.model.board;

import com.sinse.xmlapp.domain.Board;
import com.sinse.xmlapp.exception.BoardException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mybatisBoardDAO")
public class MybatisBoardDAO implements BoardDAO {
    private BoardMapper boardMapper;

    public MybatisBoardDAO(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public List<Board> selectAll() {
        return boardMapper.selectAll();
    }

    @Override
    public Board select(int board_id) {
        return boardMapper.select(board_id);
    }

    @Override
    public void insert(Board board) throws BoardException {
        try {
            boardMapper.insert(board);
        } catch (DataAccessException e) {
            throw new BoardException("등록실패",e);
        }
    }

    @Override
    public void update(Board board) throws BoardException {
        try {
            boardMapper.update(board);
        }  catch (DataAccessException e) {
            throw new BoardException("수정실패",e);
        }
    }

    @Override
    public void delete(int board_id) throws BoardException {
        try {
            boardMapper.delete(board_id);
        }  catch (DataAccessException e) {
            throw new BoardException("삭제실패",e);
        }
    }
}

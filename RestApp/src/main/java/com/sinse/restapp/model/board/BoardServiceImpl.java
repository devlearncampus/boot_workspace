package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;
import com.sinse.restapp.exception.BoardException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    //근래의 스프링에서는 @Autowired 가 지양 대상이다..
    //따라서 생성자를 통해, 정확히 어떤 자료형을 사용할지 명시하는게 좋다..
    private BoardDAO boardDAO;

    public BoardServiceImpl(@Qualifier("jpaBoardDAO") BoardDAO boardDAO) {
        this.boardDAO = boardDAO;
    }


    @Override
    public List selectAll() {
        return List.of();
    }

    @Override
    public Board select(int board_id) {
        return null;
    }

    @Override
    public void insert(Board board) throws BoardException {
        try {
            boardDAO.insert(board);
            //이 서비스 객체가 특정 DB 연동 기술에 국한된 것이 아니라,
            //모든 기술에 중립적이어야 하므로, 예외 객체 조차도 상위의 중립적인 예외일수록
            //서비스가 계층이 유연해 질 수 있다..
        } catch (DataAccessException e) {
            throw new BoardException("글등록 실패",e);
        }
    }

    @Override
    public Board update(Board board) {
        return null;
    }

    @Override
    public void delete(int board_id) {

    }
}

package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jpaBoardDAO")
public class JpaBoardDAO implements BoardDAO{

    //Hibernate 의 경우 Session 객체가 쿼리 수행 객체이지만
    //JPA 에서는 EntityManager 를 쓴다.. 최상위 인터페이스이므로, 하이버네티를 사용할 경우에도
    //EntityManager를 쓸 수 있다
    @PersistenceContext //JPA의 EntityManager를 자동으로 주입받음
    private EntityManager em;

    @Override
    public List selectAll() {
        return em.createQuery("select b from Board b").getResultList();
    }

    @Override
    public Board select(int board_id) {
        return em.find(Board.class, board_id);
    }

    @Override
    public void insert(Board board) {
        em.persist(board);
    }

    @Override
    public Board update(Board board) {
        return em.merge(board);
    }

    @Override
    public void delete(int board_id) {
        //삭제 시 무조건 일치하는 pk를 지우지 말고, 실제적으로 존재하는 데이터인지 먼저
        //체크 한 후 삭제하는게 안전
        Board board=em.find(Board.class, board_id);
        if(board !=null){ //데이터가 존재한다면..
            em.remove(board);
        }
    }
}

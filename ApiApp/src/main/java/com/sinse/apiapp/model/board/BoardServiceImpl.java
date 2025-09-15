package com.sinse.apiapp.model.board;

import com.sinse.apiapp.domain.Board;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BoardServiceImpl implements BoardService {

    private final JpaBoardRepository jpaBoardRepository;

    public BoardServiceImpl(JpaBoardRepository jpaBoardRepository) {
        this.jpaBoardRepository = jpaBoardRepository;
    }

    @Override
    public List<Board> selectAll() {
        return jpaBoardRepository.findAll();
    }

    @Override
    public Board select(int boardId) {
        return jpaBoardRepository.findById(boardId).orElse(null);
    }

    @Override
    public void regist(Board board) {
        jpaBoardRepository.save(board);
    }

    @Override
    public void update(int boardId,Board board) {
        Board obj=jpaBoardRepository.findById(boardId).orElse(null);

        obj.setTitle(board.getTitle());
        obj.setWriter(board.getWriter());
        obj.setContent(board.getContent());
    }

    @Override
    public void delete(int boardId) {
        jpaBoardRepository.deleteById(boardId);
    }
}

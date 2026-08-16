package com.likelion.seminar.service;

import com.likelion.seminar.dto.BoardDTO;
import com.likelion.seminar.entity.Board;
import com.likelion.seminar.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 1. Board 생성 (Create)
    @Transactional
    public void createBoard(BoardDTO boardDTO) {
        Board board = new Board(boardDTO.getName());
        boardRepository.save(board);
    }

    // 2. Board 단건 조회 (Read)
    public BoardDTO getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시판을 찾을 수 없습니다."));
        return new BoardDTO(board.getId(), board.getName());
    }

    // 3. Board 전체 조회 (Read)
    public List<BoardDTO> getBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardDTO> dtoList = new ArrayList<>();
        for (Board board : boards) {
            dtoList.add(new BoardDTO(board.getId(), board.getName()));
        }
        return dtoList;
    }

    // 4. Board 수정 (Update - 변경 감지)
    @Transactional
    public void updateBoard(Long id, BoardDTO boardDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시판을 찾을 수 없습니다."));
        board.setName(boardDTO.getName());
    }

    // 5. Board 삭제 (Delete)
    @Transactional
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시판을 찾을 수 없습니다."));
        boardRepository.delete(board);
    }
}

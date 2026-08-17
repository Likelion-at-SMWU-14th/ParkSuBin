package com.likelion.seminar.controller;

import com.likelion.seminar.dto.BoardDTO;
import com.likelion.seminar.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // 생성 (POST /api/board)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBoard(@RequestBody BoardDTO boardDTO) {
        boardService.createBoard(boardDTO);
    }

    // 개별 조회 (GET /api/board/{id})
    @GetMapping("/{id}")
    public BoardDTO getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 전체 조회 (GET /api/board)
    @GetMapping
    public List<BoardDTO> getBoards() {
        return boardService.getBoards();
    }

    // 수정 (PUT /api/board/{id})
    @PutMapping("/{id}")
    public void updateBoard(@PathVariable Long id, @RequestBody BoardDTO boardDTO) {
        boardService.updateBoard(id, boardDTO);
    }

    // 삭제 (DELETE /api/board/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }
}
package com.example.pilot2.Controller;


import com.example.pilot2.Service.BoardService;
import com.example.pilot2.dto.BoardDto;
import com.example.pilot2.dto.request.BoardPostRequest;
import com.example.pilot2.dto.request.BoardUpdateRequest;
import com.example.pilot2.dto.response.BoardResponse;
import com.example.pilot2.dto.response.BoardTitleResponse;
import com.example.pilot2.dto.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;

    @GetMapping
    public Page<BoardTitleResponse> getPagedList(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<BoardTitleResponse> boardTitleResponses =
                boardService.findAll(pageable)
                .map(BoardTitleResponse::from);
        return boardTitleResponses;
    }

    /**
     * RestController 방식 채택, 사라질 예정
     */
    @Deprecated
    @GetMapping("/board/form")
    public BoardPostRequest getPostForm() {
        BoardPostRequest board = createBoardForm();
        return board;
    }

    @PostMapping("/board")
    public Long post(
            @RequestBody BoardPostRequest boardPostRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        BoardDto boardDto = boardPostRequest.toDto();
        Long savedId = boardService.save(boardDto, userPrincipal.getUsername());
        return savedId;
    }

    @DeleteMapping("/{boardId}")
    public String delete(
            @PathVariable("boardId") Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        boardService.deleteBoard(id, userPrincipal.getUsername());
        return "ok"; // TODO : 추후 ApiResponse 객체 만들어 Success 에 대해 통일하자
    }

    @GetMapping("/{boardId}")
    public BoardResponse getBoardDetails(
            @PathVariable("boardId") Long id
    ) {
        BoardDto boardDto = boardService.findOne(id);
        return BoardResponse.from(boardDto);
    }

    // TODO : 업데이트와 삭제는 생성자가 일치해야 가능하도록 해야함
    @PutMapping
    public BoardResponse update(
            @RequestBody BoardUpdateRequest boardUpdateRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        BoardDto boardDto = boardUpdateRequest.toDto();
        BoardDto updatedBoardDto = boardService.update(boardDto, userPrincipal.getUsername());
        return BoardResponse.from(updatedBoardDto);
    }

    @GetMapping("/{boardId}/check")
    public boolean checkUser(
            @PathVariable("boardId") Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return boardService.checkUsername(id, userPrincipal.getUsername());
    }

    private BoardPostRequest createBoardForm() {
        return new BoardPostRequest();
    }

}

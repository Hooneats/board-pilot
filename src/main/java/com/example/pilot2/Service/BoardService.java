package com.example.pilot2.Service;

import com.example.pilot2.Entity.BoardEntity;
import com.example.pilot2.Repository.BoardRepository;
import com.example.pilot2.dto.BoardDto;
import com.example.pilot2.dto.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * pageable 은 0번 부터이다.
     */
    @Transactional(readOnly = true)
    public Page<BoardDto> findAll(final Pageable pageable){
        return boardRepository.findAll(pageable)
                .map(BoardDto::from);
    }

    public Long save(final BoardDto boardDto) {
        final BoardEntity boardEntity = boardDto.toEntity();
        final BoardEntity savedEntity = boardRepository.save(boardEntity);
        return savedEntity.getId();
    }

    @Transactional(readOnly = true)
    public BoardDto findOne(final Long id){
        return boardRepository.findById(id)
                .map(BoardDto::from)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다.")); //TODO 추후 커스텀 Exception 및 ExceptionHandler 필요
    }

    public void deleteBoard(final Long id, String username){
        BoardEntity boardEntity = checkedUsernameBoardEntity(id, username);
        boardRepository.delete(boardEntity);
    }

    public BoardDto update(final BoardDto boardDto, String username) {
        BoardEntity boardEntity = checkedUsernameBoardEntity(boardDto.getId(), username);
        boardEntity.setTitle(boardDto.getTitle());
        boardEntity.setContents(boardDto.getContents());
        BoardDto updatedBoardDto = BoardDto.from(boardEntity);
        return updatedBoardDto;
    }

    public boolean checkUsername(Long id, String username) {
        checkedUsernameBoardEntity(id, username);
        return true;
    }

    private BoardEntity checkedUsernameBoardEntity(Long id, String username) {
        final BoardEntity boardEntity = boardRepository.getReferenceById(id);
        if (!boardEntity.getUserEntity().getUsername().equals(username)) {
            throw new RuntimeException("본인의 게시글만 수정 가능합니다."); // TODO 추후 커스텀 Exception 필요
        }
        return boardEntity;
    }
}

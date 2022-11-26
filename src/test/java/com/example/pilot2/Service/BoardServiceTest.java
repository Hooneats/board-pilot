package com.example.pilot2.Service;

import com.example.pilot2.Entity.BoardEntity;
import com.example.pilot2.Entity.UserEntity;
import com.example.pilot2.Repository.BoardRepository;
import com.example.pilot2.Repository.UserRepository;
import com.example.pilot2.dto.BoardDto;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비스니스 로직 - 게시판")
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService sut;

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;


    @DisplayName("페이지된 게시글 목록 전체 조회")
    @Test
    void findAllTest() {
        //given
        Pageable pageable = Pageable.ofSize(20);
        given(boardRepository.findAll(pageable)).willReturn(Page.empty());

        //when
        Page<BoardDto> boardDtos = sut.findAll(pageable);

        //then
        assertThat(boardDtos).isEmpty();
        then(boardRepository).should().findAll(pageable);
    }

    @DisplayName("게시글 저장")
    @Test
    void saveTest() {
        //given
        BoardDto boardDto = BoardDto.builder()
                .title("테스트_제목")
                .contents("테스트_본문")
                .build();
        Long saveId = 999L;
        UserEntity userEntity = new UserEntity("테스트_유저", "테스트_pw");
        BoardEntity boardEntity = new BoardEntity(saveId, boardDto.getTitle(), boardDto.getContents(), userEntity);
        given(boardRepository.save(any(BoardEntity.class))).willReturn(boardEntity);
        given(userRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(userEntity));

        //when
        Long savedId = sut.save(boardDto,userEntity.getUsername());

        //then
        assertThat(savedId).isEqualTo(saveId);
        then(boardRepository).should().save(any(BoardEntity.class));
        then(userRepository).should().findByUsername(anyString());
    }

    @DisplayName("게시글 단건 조회")
    @Test
    void findOneTest() {
        //given
        Long findId = 999L;
        BoardEntity boardEntity = new BoardEntity(findId, "테스트_제목", "테스트_본문", null);
        given(boardRepository.findById(findId)).willReturn(Optional.of(boardEntity));

        //when
        BoardDto boardDto = sut.findOne(findId);

        //then
        assertThat(boardDto.getId()).isEqualTo(findId);
        then(boardRepository).should().findById(findId);
    }

    @DisplayName("게시글 삭제")
    @Test
    void deleteBoard() {
        //given
        Long boardId = 999L;
        String username = "테스트_유저";
        BoardEntity boardEntity = new BoardEntity(boardId, "테스트_제목", "테스트_본문", new UserEntity(username, Strings.EMPTY));
        given(boardRepository.getReferenceById(boardId)).willReturn(boardEntity);
        willDoNothing().given(boardRepository).delete(boardEntity);


        //when
        sut.deleteBoard(boardId, username);

        //then
        then(boardRepository).should().getReferenceById(boardId);
        then(boardRepository).should().delete(boardEntity);
    }

    @DisplayName("게시글 업데이트")
    @Test
    void update() {
        //given
        Long boardId = 999L;
        String username = "테스트_유저";
        BoardDto boardDto = BoardDto.builder()
                .id(boardId)
                .title("테스트_제목")
                .contents("테스트_본문")
                .build();
        BoardEntity boardEntity = new BoardEntity(boardDto.getId(), boardDto.getTitle(), boardDto.getContents(), new UserEntity(username, Strings.EMPTY));
        given(boardRepository.getReferenceById(boardDto.getId())).willReturn(boardEntity);

        //when
        BoardDto updatedBoardDto = sut.update(boardDto, username);

        //then
        assertThat(updatedBoardDto.getId()).isEqualTo(boardDto.getId());
        assertThat(updatedBoardDto.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(updatedBoardDto.getContents()).isEqualTo(boardDto.getContents());
        then(boardRepository).should().getReferenceById(boardDto.getId());
    }

    @Test
    void checkUsername() {
        //given
        Long boardId = 999L;
        String username = "테스트_유저";
        BoardEntity boardEntity = new BoardEntity(boardId, "테스트_제목", "테스트_본문", new UserEntity(username, Strings.EMPTY));
        given(boardRepository.getReferenceById(boardId)).willReturn(boardEntity);

        //when
        boolean isCheck = sut.checkUsername(boardId, username);

        //then
        assertThat(isCheck).isTrue();
        then(boardRepository).should().getReferenceById(boardId);
    }
}
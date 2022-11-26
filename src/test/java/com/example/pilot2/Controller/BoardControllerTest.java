package com.example.pilot2.Controller;

import com.example.pilot2.Service.BoardService;
import com.example.pilot2.config.TestSecurityConfig;
import com.example.pilot2.dto.BoardDto;
import com.example.pilot2.dto.request.BoardPostRequest;
import com.example.pilot2.dto.request.BoardUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@DisplayName("게시판 테스트")
@Import(TestSecurityConfig.class)
@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BoardService boardService;

    @WithMockUser
    @DisplayName("GET - 게시글 리스트 조회(페이징)")
    @Test
    void 게시글_페이징된_리스트_조회() throws Exception {
        // given
        String sortProperty = "createdAt";
        int pageNum = 0;
        int pageSize = 30;
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "createdAt");
        Page<BoardDto> pagedBoardDtos = Page.empty(pageRequest);
        given(boardService.findAll(any(Pageable.class))).willReturn(pagedBoardDtos);

        MockHttpServletRequestBuilder queryParam = MockMvcRequestBuilders.get("/boards")
                .queryParam("page", String.valueOf(pageRequest.getPageNumber()))
                .queryParam("sort", sortProperty)
                .queryParam("direction", Sort.Direction.DESC.name());

        // when & then
        mvc.perform(queryParam)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("size").value(pageSize))
                .andExpect(MockMvcResultMatchers.jsonPath("number").value(pageNum))
                .andExpect(MockMvcResultMatchers.jsonPath("sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("content").exists());
        then(boardService).should().findAll(any(Pageable.class));
    }

    @Disabled("Rest 방식이여서 삭제 해야함")
    @WithMockUser
    @DisplayName("GET - 게시글 리스트 조회(페이징)")
    @Test
    void 게시글_업로드를_위한_form() throws Exception {
        // given
        BoardPostRequest boardPostRequest = new BoardPostRequest();

        // when & then
        mvc.perform(MockMvcRequestBuilders.get("/boards/board/form"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("title").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("contents").isEmpty());
    }

    @WithUserDetails(value = "superAccount", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("POST - 게시글 업로드")
    @Test
    void 게시글_업로드() throws Exception {
        // given
        String title = "테스트_제목";
        String contents = "테스트_본문";
        Long savedId = 999L;
        BoardPostRequest boardPostRequest = new BoardPostRequest(title, contents);
        given(boardService.save(any(BoardDto.class), anyString())).willReturn(savedId);

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(boardPostRequest);

        MockHttpServletRequestBuilder postURI =
                MockMvcRequestBuilders
                        .post("/boards/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf());

        // when & then
        mvc.perform(postURI)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(savedId)));
        then(boardService).should(times(1)).save(any(BoardDto.class), anyString());
    }

    @WithUserDetails(value = "superAccount", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("DELETE - 게시글 삭제")
    @Test
    void 게시글_삭제() throws Exception {
        // given
        willDoNothing().given(boardService).deleteBoard(anyLong(), anyString());


        MockHttpServletRequestBuilder deleteUrl =
                MockMvcRequestBuilders
                        .delete("/boards/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf());

        // when & then
        mvc.perform(deleteUrl)
                .andExpect(MockMvcResultMatchers.status().isOk());
        then(boardService).should().deleteBoard(anyLong(), anyString());
    }

    @WithMockUser
    @DisplayName("GET - 게시글 단건 조회")
    @Test
    void 게시글_단건_조회() throws Exception {
        // given
        Long boardId = 999L;
        BoardDto boardDto = BoardDto.builder()
                .id(boardId)
                .title("테스트_제목")
                .contents("테스트_본문")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .createdBy("테스트_유저")
                .modifiedBy("테스트_유저")
                .build();
        given(boardService.findOne(anyLong())).willReturn(boardDto);


        MockHttpServletRequestBuilder deleteUrl =
                MockMvcRequestBuilders
                        .get("/boards/" + boardId);

        // when & then
        mvc.perform(deleteUrl)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(boardId))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(boardDto.getTitle()));
        then(boardService).should().findOne(anyLong());
    }

    @WithUserDetails(value = "superAccount", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("PUT - 게시글 업데이트")
    @Test
    void 게시글_업데이트() throws Exception {
        // given
        Long boardId = 999L;
        BoardUpdateRequest updateBoardForm = BoardUpdateRequest.builder()
                .id(boardId)
                .title("업데이트_제목")
                .contents("업데이트_본문")
                .build();
        BoardDto boardDto = updateBoardForm.toDto();
        given(boardService.update(any(BoardDto.class), anyString())).willReturn(boardDto);

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder putURI =
                MockMvcRequestBuilders
                        .put("/boards")
                        .content(mapper.writeValueAsString(updateBoardForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf());

        // when & then
        mvc.perform(putURI)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(boardDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(boardDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("contents").value(boardDto.getContents()));
        then(boardService).should().update(any(BoardDto.class), anyString());
    }

    @WithUserDetails(value = "superAccount", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("GET - 유저이름과 게시글 작성자가 일치하는지 확인")
    @Test
    void 게시글_작성자_일치_여부() throws Exception {
        // given
        Long boardId = 1L;
        given(boardService.checkUsername(anyLong(), anyString())).willReturn(true);

        MockHttpServletRequestBuilder putURI =
                MockMvcRequestBuilders
                        .get("/boards/" + boardId + "/check");

        // when & then
        mvc.perform(putURI)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(true)));
        then(boardService).should().checkUsername(anyLong(), anyString());
    }
}

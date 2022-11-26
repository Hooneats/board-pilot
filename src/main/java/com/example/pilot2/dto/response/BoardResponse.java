package com.example.pilot2.dto.response;

import com.example.pilot2.dto.BoardDto;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse implements Serializable {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private Long id;
    private String title;
    private String contents;

    public static BoardResponse from(BoardDto boardDto) {
        return BoardResponse.builder()
                .id(boardDto.getId())
                .title(boardDto.getTitle())
                .contents(boardDto.getContents())
                .modifiedAt(boardDto.getModifiedAt())
                .createdAt(boardDto.getCreatedAt())
                .createdBy(boardDto.getCreatedBy())
                .build();
    }
}

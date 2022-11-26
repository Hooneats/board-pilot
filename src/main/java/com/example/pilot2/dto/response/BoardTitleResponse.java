package com.example.pilot2.dto.response;

import com.example.pilot2.Entity.BoardEntity;
import com.example.pilot2.dto.BoardDto;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardTitleResponse implements Serializable {
    private String createdBy;
    private LocalDateTime modifiedAt;
    private Long id;
    private String title;

    public static BoardTitleResponse from(BoardEntity entity) {
        return BoardTitleResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

    public static BoardTitleResponse from(BoardDto boardDto) {
        return BoardTitleResponse.builder()
                .id(boardDto.getId())
                .title(boardDto.getTitle())
                .createdBy(boardDto.getCreatedBy())
                .modifiedAt(boardDto.getModifiedAt())
                .build();
    }
}

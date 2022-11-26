package com.example.pilot2.dto;

import com.example.pilot2.Entity.BoardEntity;
import com.example.pilot2.Entity.UserEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto implements Serializable {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private Long id;
    private String title;
    private String contents;

    public BoardDto(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public static BoardDto from(BoardEntity entity) {
        return BoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }

    public BoardEntity toEntity(UserEntity userEntity) {
        return new BoardEntity(this.title, this.contents, userEntity);
    }
}
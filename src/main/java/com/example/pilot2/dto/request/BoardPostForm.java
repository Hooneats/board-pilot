package com.example.pilot2.dto.request;

import com.example.pilot2.Entity.BoardEntity;
import com.example.pilot2.dto.BoardDto;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostForm implements Serializable {
    private String title;
    private String contents;

    public BoardEntity toEntity() {
        return new BoardEntity(this.title, this.contents);
    }

    public BoardDto toDto() {
        return BoardDto.builder()
                .title(this.title)
                .contents(this.contents)
                .build();
    }
}

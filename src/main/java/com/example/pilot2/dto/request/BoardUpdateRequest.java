package com.example.pilot2.dto.request;

import com.example.pilot2.dto.BoardDto;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest implements Serializable {
    private Long id;
    private String title;
    private String contents;

    public BoardDto toDto() {
        return new BoardDto(this.id, this.title, this.contents);
    }
}

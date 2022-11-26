package com.example.pilot2.dto.request;

import com.example.pilot2.dto.BoardDto;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostRequest implements Serializable {
    private String title;
    private String contents;

    public BoardDto toDto() {
        return BoardDto.builder()
                .title(this.title)
                .contents(this.contents)
                .build();
    }
}

package com.example.pilot2.Entity;


import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "title")
})
@Entity(name = "board")
public class BoardEntity extends AuditingFields {

    /**
     * 일반적으로 ID 는 Integer 보다 큰 수 Long 을 담는다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Setter 는 필드 전부를 열어주기보다 정말 필요한곳을 열어주는것이 불변데이터를 유지하는데 도움이된다.
     */
    @Setter
    @Column(nullable = false,length = 64)
    private String title;

    @Setter
    @Lob
    private String contents;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public BoardEntity(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEntity that = (BoardEntity) o;
        return this.id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

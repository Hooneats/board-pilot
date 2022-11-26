package com.example.pilot2.Entity;


import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {
    @Index(columnList = "username", unique = true)
})
@Entity(name = "users")
public class UserEntity extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany
    private Set<BoardEntity> boardEntities = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRoleEntity> userRoleEntities = new LinkedHashSet<>();

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserEntity(String username, String password, RoleEntity roleEntity) {
        this.username = username;
        this.password = password;
        userRoleEntities.add(new UserRoleEntity(this, roleEntity));
    }

    public void addRoleEntity(RoleEntity roleEntity) {
        userRoleEntities.add(new UserRoleEntity(this, roleEntity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return this.id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package com.example.pilot2.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "user_role")
public class UserRoleEntity extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id")
    private RoleEntity role;

    public UserRoleEntity(UserEntity userEntity, RoleEntity roleEntity) {
        this.user = userEntity;
        this.role = roleEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleEntity that = (UserRoleEntity) o;
        return (user != null && role != null) && user.equals(that.getUser()) && role.equals(that.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role);
    }
}

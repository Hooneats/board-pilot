package com.example.pilot2.dto;

import com.example.pilot2.Entity.UserEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private Long id;
    private String username;
    private String password;
    private Set<RoleDto> roleDtos;

    public static UserDto from(UserEntity entity) {
        Set<RoleDto> roleDtoSet = entity.getUserRoleEntities()
                .stream()
                .map(userRoleEntity -> RoleDto.from(userRoleEntity.getRole()))
                .collect(Collectors.toUnmodifiableSet());

        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .roleDtos(roleDtoSet)
                .build();
    }

    public UserEntity toEntity() {
        return new UserEntity(this.username, this.password);
    }

}
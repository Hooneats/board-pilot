package com.example.pilot2.dto;

import com.example.pilot2.Entity.UserRoleEntity;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto implements Serializable {
    private UserDto userDto;
    private RoleDto roleDto;

    public static UserRoleDto from(UserRoleEntity userRoleEntity) {
        return UserRoleDto.builder()
                .userDto(UserDto.from(userRoleEntity.getUser()))
                .roleDto(RoleDto.from(userRoleEntity.getRole()))
                .build();
    }
}

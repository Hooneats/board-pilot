package com.example.pilot2.dto.request;

import com.example.pilot2.dto.RoleDto;
import com.example.pilot2.dto.UserDto;
import com.example.pilot2.dto.constant.Authority;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRequest implements Serializable {
    private String username;
    private String password;
    private Authority authority;

    public UserDto toUserDto(String encodedPwd) {
        return UserDto.builder()
                .username(this.username)
                .password(encodedPwd)
                .build();
    }

    public RoleDto toRoleDto() {
        return RoleDto.builder()
                .authority(this.authority)
                .build();
    }
}
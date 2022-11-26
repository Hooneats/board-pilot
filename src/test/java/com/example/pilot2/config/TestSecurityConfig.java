package com.example.pilot2.config;

import com.example.pilot2.Service.UserService;
import com.example.pilot2.dto.RoleDto;
import com.example.pilot2.dto.UserDto;
import com.example.pilot2.dto.constant.Authority;
import com.example.pilot2.dto.request.UserRoleRequest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserService userService;

    @BeforeTestMethod
    public void securitySetup() {
        given(userService.findUser(anyString()))
                .willReturn(Optional.of(createTestAccountDto()));
        given(userService.saveUser(any(UserRoleRequest.class)))
                .willReturn(createTestAccountDto());
    }

    private UserDto createTestAccountDto() {
        return UserDto.builder()
                .username("superAccount")
                .password("superAccount")
                .roleDtos(
                        Set.of(
                                RoleDto.builder()
                                        .authority(Authority.ADMIN)
                                        .build(),
                                RoleDto.builder()
                                        .authority(Authority.USER)
                                        .build()
                        )
                )
                .build();
    }
}

package com.example.pilot2.Service;

import com.example.pilot2.Entity.RoleEntity;
import com.example.pilot2.Entity.UserEntity;
import com.example.pilot2.Repository.RoleRepository;
import com.example.pilot2.Repository.UserRepository;
import com.example.pilot2.dto.RoleDto;
import com.example.pilot2.dto.UserDto;
import com.example.pilot2.dto.request.UserRoleForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<UserDto> findUser(final String username) {
        return userRepository.findByUsername(username)
                .map(UserDto::from);
    }

    public UserDto saveUser(final UserRoleForm userRoleForm) {
        // get foundRoleEntity
        RoleDto roleDto = userRoleForm.toRoleDto();
        RoleEntity foundRoleEntity = findRoleEntity(roleDto);

        // get saveEntity
        String encodedPwd = encodePassword(userRoleForm.getPassword());
        UserDto userDto = userRoleForm.toUserDto(encodedPwd);
        UserEntity userEntity = userDto.toEntity();
        userEntity.addRoleEntity(foundRoleEntity);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        return UserDto.from(savedUserEntity);
    }

    private RoleEntity findRoleEntity(RoleDto roleDto) {
        RoleEntity roleEntity = roleRepository.findByAuthority(roleDto.getAuthority())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 권한입니다."));// TODO : 추후 ExceptionHandler 필요
        return roleEntity;
    }

    private String encodePassword(String password) {
        String encodedPwd = passwordEncoder.encode(password);
        return encodedPwd;
    }
}

package com.example.pilot2;

import com.example.pilot2.Entity.BoardEntity;
import com.example.pilot2.Entity.RoleEntity;
import com.example.pilot2.Entity.UserEntity;
import com.example.pilot2.Entity.UserRoleEntity;
import com.example.pilot2.Repository.BoardRepository;
import com.example.pilot2.Repository.RoleRepository;
import com.example.pilot2.Repository.UserRepository;
import com.example.pilot2.Repository.UserRoleRepository;
import com.example.pilot2.dto.constant.Authority;
import com.example.pilot2.dto.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Profile("local")
@Component
@EnableJpaAuditing
@AllArgsConstructor
public class InitTestData {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("test");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        RoleEntity adminRoleEntity = new RoleEntity(Authority.ADMIN);
        RoleEntity userRoleEntity = new RoleEntity(Authority.USER);
        RoleEntity savedAdminRoleEntity = roleRepository.save(adminRoleEntity);
        RoleEntity savedUserRoleEntity = roleRepository.save(userRoleEntity);

        UserEntity adminEntity = new UserEntity("admin", passwordEncoder.encode("admin"), savedAdminRoleEntity);
        UserEntity userEntity = new UserEntity("user", passwordEncoder.encode("user"), savedUserRoleEntity);
        UserEntity savedAdminEntity = userRepository.save(adminEntity);
        UserEntity savedUserEntity = userRepository.save(userEntity);

//        UserRoleEntity userRoleEntity1 = new UserRoleEntity(savedAdminEntity, savedAdminRoleEntity);
//        UserRoleEntity userRoleEntity2 = new UserRoleEntity(savedUserEntity, savedUserRoleEntity);
//        userRoleRepository.save(userRoleEntity1);
//        userRoleRepository.save(userRoleEntity2);


        List<BoardEntity> boardEntityList = new ArrayList<>();
        IntStream.range(0, 45)
                .forEach(num ->
                        boardEntityList.add(
                                new BoardEntity("title" + num, "contents" + num)
                        )
                );
        boardRepository.saveAll(boardEntityList);
    }
}


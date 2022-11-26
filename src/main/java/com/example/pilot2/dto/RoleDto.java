package com.example.pilot2.dto;

import com.example.pilot2.Entity.RoleEntity;
import com.example.pilot2.dto.constant.Authority;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.example.pilot2.Entity.RoleEntity} entity
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Serializable {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private Long id;
    private Authority authority;

    public static RoleDto from(RoleEntity entity) {
        return RoleDto.builder()
                .id(entity.getId())
                .authority(entity.getAuthority())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}
package com.example.football_championship.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @NotNull
    @CreatedBy
    @Column(length = 255, nullable = false, updatable = false)
    String createdBy = "Admin";

    @NotNull
    @LastModifiedBy
    @Column(length = 255, nullable = false)
    String lastModifiedBy = "Admin";

    @NotNull
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @NotNull
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;
}
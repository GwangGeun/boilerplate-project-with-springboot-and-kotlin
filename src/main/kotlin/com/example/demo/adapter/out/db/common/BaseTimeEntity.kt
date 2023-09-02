package com.example.demo.adapter.out.db.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseTimeEntity(
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name = "modified_date")
    var modifiedDate: LocalDateTime = LocalDateTime.now()
) {

}
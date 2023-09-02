package com.example.demo.adapter.out.db.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseEntity(
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: String = "",

    @LastModifiedBy
    @Column(name = "modified_by")
    var modifiedBy: String = ""
) : BaseTimeEntity()


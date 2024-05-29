package com.example.blog.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@EntityListeners(value=[AuditingEntityListener::class])
@MappedSuperclass
abstract class AuditingEntity(
    id:Long
):AuditingEntityId(id){

    @CreatedDate
    @Column(name="create_at",nullable = false,updatable = false)
    var createAt:LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    @Column(name="update_at",nullable = false,updatable = false)
   var updateAt:LocalDateTime= LocalDateTime.now()
        protected set
}


@EntityListeners(value=[AuditingEntityListener::class])
@MappedSuperclass
abstract class AuditingEntityId(
    id:Long
):Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = id
        protected set
}
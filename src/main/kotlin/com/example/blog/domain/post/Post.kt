package com.example.blog.domain.post

import com.example.blog.domain.AuditingEntity
import jakarta.persistence.*

@Entity
@Table(name="Post")
class Post(
    title:String,
    content:String

):AuditingEntity(){
    @Column(name="title",nullable = false)
     var title:String = title
        protected set

    @Column(name="content")
    var content:String = content
        protected set
}
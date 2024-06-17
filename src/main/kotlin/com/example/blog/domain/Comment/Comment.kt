package com.example.blog.domain.Comment

import com.example.blog.domain.AuditingEntity
import com.example.blog.domain.member.Member
import com.example.blog.domain.post.Post
import jakarta.persistence.*

@Entity
@Table(name="Comment")
class Comment (
    id:Long = 0,
    title:String,
    content:String,
    post: Post
): AuditingEntity(id)
    {
        @Column(name = "title", nullable = false)
        var title: String = title
        protected set

        @Column(name = "content")
        var content: String = content
        protected set

        @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post::class)
        var post: Post = post
            protected set
    }

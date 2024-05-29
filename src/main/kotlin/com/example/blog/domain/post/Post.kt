package com.example.blog.domain.post

import com.example.blog.domain.AuditingEntity
import com.example.blog.domain.member.Member
import com.example.blog.domain.member.toDto
import jakarta.persistence.*

@Entity
@Table(name="Post")
class Post(
    id:Long = 0,
    title:String,
    content:String,
    member:Member

):AuditingEntity(id){
    @Column(name="title",nullable = false)
     var title:String = title
        protected set

    @Column(name="content", length = 1000)
    var content:String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member:Member = member
        protected set

    override fun toString(): String {
        return "Post(id='$id', title='$title', content='$content', member=$member)"
    }

}

fun Post.toDto():PostRes{
    return PostRes(
        id=this.id!!,
        title=this.title,
        content=this.content,
        member=this.member.toDto()
    )
}
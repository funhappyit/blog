package com.example.blog.domain.member

import com.example.blog.domain.AuditingEntity
import com.example.blog.domain.post.Post
import jakarta.persistence.*

@Entity
@Table(name="Member")
class Member(
    email:String,
    password:String,
    role:Role
) : AuditingEntity(){
    @Column(name="email",nullable = false)
    var email:String = email
        protected set

    @Column(name="password")
    var password:String = password
        protected set

    @Enumerated(EnumType.STRING)
    var roles:Role = role
        protected set

    @OneToMany(mappedBy = "member", targetEntity = Post::class)
    var posts = mutableListOf<Post>()


    override fun toString(): String {
        return "Member(email='$email', password='$password', roles=$roles)"
    }

    companion object{
        fun createFakeMember(memberId: Long):Member{
            val member = Member("","",Role.USER)
            member.id = memberId
            return member
        }
    }


}
fun Member.toDto(): MemberRes {
    return MemberRes(
        id = this.id!!,
        email = this.email,
        password = this.password,
       role = this.roles
    )
}

enum class Role{
    USER,ROLE
}
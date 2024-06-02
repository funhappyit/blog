package com.example.blog.domain.member

import com.example.blog.domain.AuditingEntity
import com.example.blog.domain.post.Post
import jakarta.persistence.*

@Entity
@Table(name="Member")
class Member(
    id:Long = 0,
    email:String,
    password:String,
    role:Role = Role.USER,
) : AuditingEntity(id){
    @Column(name="email",nullable = false)
    var email:String = email
        protected set

    @Column(name="password")
    var password:String = password
        protected set

    @Enumerated(EnumType.STRING)
    var roles:Role = role
        protected set

    fun toDto(): MemberRes {
        return MemberRes(
            id = this.id!!,
            email = this.email,
            password = this.password,
            role = this.roles,
            createAt = this.createAt,
            updateAt = this.updateAt
        )
    }


    override fun toString(): String {
        return "Member(email='$email', password='$password', roles='$roles', createAt='$createAt')"
    }

    companion object{
        fun createFakeMember(memberId: Long):Member{
            val member = Member(memberId,"test@test.com","1234")
            return member
        }
    }


}


enum class Role{
    USER,ADMIN
}
package com.example.blog.domain.member

import com.example.blog.domain.AuditingEntity
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

    override fun toString(): String {
        return "Member(email='$email', password='$password', roles=$roles)"
    }


}
enum class Role{
    USER,ROLE
}
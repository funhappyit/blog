package com.example.blog.config.security

import com.example.blog.domain.member.Member
import com.fasterxml.jackson.annotation.JsonIgnore
import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PrincipalDetails(
    //기본 생성자 옵션을 만들어줌
    member: Member = Member.createFakeMember(0L),
): UserDetails{

    var member:Member = member
        private set
    private val log = KotlinLogging.logger {  }
    @JsonIgnore
    val collection:MutableCollection<GrantedAuthority> = ArrayList()

    init {
        this.collection.add(GrantedAuthority { "ROLE_"+member.roles })
    }

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        log.info { "Role 검증" }
        val collection:MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { "ROLE_"+ member.roles})
        return collection
    }

    override fun getPassword(): String {
       return member.password
    }

    override fun getUsername(): String {
       return member.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
       return true
    }

    override fun isCredentialsNonExpired(): Boolean {
       return true
    }

    override fun isEnabled(): Boolean {
       return true
    }


}
package com.example.blog.service

import com.example.blog.config.security.PrincipalDetails
import com.example.blog.domain.member.LoginDto
import com.example.blog.domain.member.MemberRepository
import com.example.blog.domain.member.MemberRes


import mu.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,

): UserDetailsService {

    val log = KotlinLogging.logger {}

    override fun loadUserByUsername(email: String): UserDetails {
        log.info { "loadUserByUsername 호출!" }
        val member = memberRepository.findMemberByEmail(email)
        return PrincipalDetails(member)
    }
    @Transactional
    fun saveMember(dto: LoginDto): MemberRes {
        return memberRepository.save(dto.toEntity()).toDto()
    }
}
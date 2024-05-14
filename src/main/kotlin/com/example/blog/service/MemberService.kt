package com.example.blog.service

import com.example.blog.domain.member.Member
import com.example.blog.domain.member.MemberRepository
import com.example.blog.domain.member.MemberRes
import com.example.blog.domain.member.toDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

//    @Transactional(readOnly = true)
//    fun findAll():MutableList<Member> = memberRepository.findAll()

    @Transactional(readOnly = true)
    fun findAll(): List<MemberRes> =
        memberRepository.findAll().map {
            it.toDto()
        }

}
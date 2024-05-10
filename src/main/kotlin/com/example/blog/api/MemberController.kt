package com.example.blog.api

import com.example.blog.domain.member.Member
import com.example.blog.service.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(): MutableList<Member> {

        return memberService.findAll()
    }


}
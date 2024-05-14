package com.example.blog.api

import com.example.blog.domain.member.Member
import com.example.blog.service.MemberService
import com.example.blog.util.value.CntResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(): CntResDto<*>{
        return CntResDto(HttpStatus.OK,"find All Members",memberService.findAll())
    }

}
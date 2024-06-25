package com.example.blog.api

import com.example.blog.domain.member.LoginDto
import com.example.blog.domain.member.Member

import com.example.blog.service.MemberService
import com.example.blog.util.value.CntResDto
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.io.Serializable

@RequestMapping("/v1")
@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(@PageableDefault(size = 10) pageable: Pageable,session: HttpSession): CntResDto<*>{
        return CntResDto(HttpStatus.OK,"find All Members",memberService.findAll(pageable))
    }

    @GetMapping("/member/{id}")
    fun findById(@PathVariable("id") id:Long): CntResDto<Any>{
        return CntResDto(HttpStatus.OK,"find Member by id",memberService.findMemberById(id))
    }

    @DeleteMapping("/member/{id}")
    fun deleteById(@PathVariable("id") id:Long): CntResDto<Any> {
        return CntResDto(HttpStatus.OK,"delete Member by id",memberService.deleteMember(id))
    }



}
package com.example.blog.api

import com.example.blog.domain.member.LoginDto
import com.example.blog.service.AuthService
import com.example.blog.util.value.CntResDto
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/auth")
@RestController
class AuthController(
    private val authService: AuthService

) {
    val log = KotlinLogging.logger {}

    @GetMapping("/login")
    fun login(session: HttpSession){
        session.setAttribute("principal", "pass")
    }

    @PostMapping("/member")
    fun joinApp(@Valid @RequestBody dto: LoginDto): CntResDto<*> {
        return CntResDto(HttpStatus.OK, resultMsg = "회원가입",authService.saveMember(dto))
    }

}
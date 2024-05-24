package com.example.blog.config.security

import com.example.blog.domain.member.LoginDto
import com.fasterxml.jackson.databind.ObjectMapper

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


class CustomUserNameAuthenticationFilter(
    private val ob: ObjectMapper
): UsernamePasswordAuthenticationFilter() {
    private val log = KotlinLogging.logger {}

    private val jwtManager = JwtManager()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        log.info { "login 요청중" }
        lateinit var loginDto:LoginDto
        try{
            loginDto =  ob.readValue(request?.inputStream, LoginDto::class.java)
            log.info{"login Dto: $loginDto "}
        }catch(ex: Exception){
            log.error("loginFilter : 로그인 요청 Dto 생성 중 실패 ! $ex")
        }


       val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email,loginDto.password)

        return this.authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ){
        log.info { "로그인 완료되어서 JWT 토큰 만들어서 response" }
        val princopalDetails = authResult?.principal as PrincipalDetails
        val jwtToken = jwtManager.generateAccessToken(princopalDetails)
        response?.addHeader(jwtManager.jwtHeader, "Bearer $jwtToken")

    }



}
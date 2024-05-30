package com.example.blog.config.security

import com.example.blog.domain.member.LoginDto
import com.example.blog.util.func.responseData
import com.example.blog.util.value.CntResDto
import com.fasterxml.jackson.databind.ObjectMapper

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.transaction.annotation.Transactional


class CustomUserNameAuthenticationFilter(
    private val ob: ObjectMapper
): UsernamePasswordAuthenticationFilter() {
    private val log = KotlinLogging.logger {}

    private val jwtManager = JwtManager()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        log.debug { "login 요청중" }
        lateinit var loginDto:LoginDto
        try{
            loginDto =  ob.readValue(request?.inputStream, LoginDto::class.java)
            log.debug{"login Dto: $loginDto "}
        }catch(ex: Exception){
            log.error("loginFilter : 로그인 요청 Dto 생성 중 실패 ! $ex")
        }


       val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email,loginDto.password)

        return this.authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ){
        log.info { "로그인 완료되어서 JWT 토큰 만들어서 response" }
        val prinpalDetails = authResult?.principal as PrincipalDetails
        val jwtToken = jwtManager.generateAccessToken(ob.writeValueAsString(prinpalDetails))
        response?.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader+jwtToken)
        println(prinpalDetails)

       val jsonResult = ob.writeValueAsString(CntResDto(HttpStatus.OK,"login success",prinpalDetails))

       responseData(response,jsonResult)

    }
}
package com.example.blog.config.security

import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.blog.domain.member.MemberRepository
import com.example.blog.util.CookieProvider
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class CustomBasicAuthenticationFilter(
    private val memberRepository: MemberRepository,
    private val om: ObjectMapper,
    authenticationManager: AuthenticationManager
): BasicAuthenticationFilter(authenticationManager) {

    val log = KotlinLogging.logger {}

    private val jwtManager = JwtManager()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
       log.info { "권한이나 인증이 필요한 요청이 들어옴" }

        val accessToken = request.getHeader(jwtManager.authorizationHeader).replace("Bearer ","")

        if(accessToken == null || accessToken == "") {
            log.info{"token이 없습니다"}
            chain.doFilter(request, response)
            return
        }

        log.debug { "access token: $accessToken" }

        val accessTokenResult: TokenValidResult = jwtManager.validAccessToken(accessToken)

        if(accessTokenResult is TokenValidResult.Failure) {
            handleTokenException(accessTokenResult){
                log.info{"getClass=====>${accessTokenResult.exception.javaClass}"}
                val refreshToken = CookieProvider.getCookie(request, CookieProvider.CookieName.REFRESH_COOKIE).orElseThrow()
                val refreshTokenResult = jwtManager.validRefreshToken(refreshToken)
                if(refreshTokenResult is TokenValidResult.Failure) {
                    throw RuntimeException("Invalid refreshToken")
                }


                val principalString = jwtManager.getPrincipalStringByRefreshToken(refreshToken)
                val details = om.readValue(principalString, PrincipalDetails::class.java)

                reissueAccessToken(details, response)
                setAuthentication(details, chain, request, response)
            }
            return
        }
        val principalJsonData = jwtManager.getPrincipalStringByAccessToken(accessToken)
        val principalDetails = om.readValue(principalJsonData, PrincipalDetails::class.java)

        setAuthentication(principalDetails, chain, request, response)
    }

    private fun setAuthentication(
        details: PrincipalDetails,
        chain: FilterChain,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val authentication: Authentication =
            UsernamePasswordAuthenticationToken(
                details,
                details.password,
                details.authorities
            )
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun reissueAccessToken(
        details: PrincipalDetails?,
        response: HttpServletResponse
    ) {
        log.info { "accessToken 재발급" }
        val accessToken = jwtManager.generateAccessToken(om.writeValueAsString(details))
        response?.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader + accessToken)
    }

    private fun handleTokenException(tokenValidResult: TokenValidResult.Failure, func:()->Unit){
            when(tokenValidResult.exception){
                is TokenExpiredException -> func()
                else -> {
                    log.error(tokenValidResult.exception.stackTraceToString())
                    throw tokenValidResult.exception
                }
            }
    }


}
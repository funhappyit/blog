package com.example.blog.config.security

import com.auth0.jwt.exceptions.JWTVerificationException
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

            if(accessTokenResult.exception is TokenExpiredException){
                log.info{"getClass=====>${accessTokenResult.javaClass}"}
            }else{
                log.error{accessTokenResult.exception.stackTraceToString()}
            }

        }

        //val memberEmail = jwtManager.getMemberEmail(token) ?:throw RuntimeException("memberEmail을 찾을 수 없습니다")
        val principalJsonData = jwtManager.getPrincipalStringByAccessToken(accessToken)





        val principalDetails = om.readValue(principalJsonData, PrincipalDetails::class.java)

        //DB로 호출하잖아요.
        //val member = memberRepository.findMemberByEmail(details.member.email)

       // val principalDetails = PrincipalDetails(member)


        val authentication:Authentication =
        UsernamePasswordAuthenticationToken(
            principalDetails,
            principalDetails.password,
            principalDetails.authorities
        )
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

//    private fun reissueAccessToken(
//        e: JWTVerificationException,
//        req: HttpServletRequest?
//    ) {
//        if (e is TokenExpiredException) {
//            val refreshToken = CookieProvider.getCookie(req!!, "refreshCookie").orElseThrow()
//            val validatedJwt = validatedJwt(refreshToken)
//
//            val principalString = getPrincipalStringByAccessToken(refreshToken)
//
//            val principalDetails = ObjectMapper().readValue(principalString, PrincipalDetails::class.java)
//
//            //요거 문제였다
//            val authentication: Authentication =
//                UsernamePasswordAuthenticationToken(
//                    principalDetails,
//                    principalDetails.password,
//                    principalDetails.authorities
//                )
//            SecurityContextHolder.getContext().authentication = authentication //인증 처리 끝
//        }
//    }

}
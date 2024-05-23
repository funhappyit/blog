package com.example.util

import com.example.blog.config.security.JwtManager
import com.example.blog.config.security.PrincipalDetails
import com.example.blog.domain.member.Member
import mu.KotlinLogging
import org.junit.jupiter.api.Test

class UtilTest {
    val log = KotlinLogging.logger {}
    @Test
    fun generateJwtTest(){
        val jwtManager = JwtManager()

        val details = PrincipalDetails(Member.createFakeMember(1))
        val accessToken = jwtManager.generateToken(details)

        val email = jwtManager.getMemberEmail(accessToken)

        log.info { "access token: $accessToken" }
        log.info { "email: $email" }



    }

}
package com.example.util

import com.example.blog.config.security.JwtManager
import com.example.blog.config.security.PrincipalDetails
import com.example.blog.domain.member.Member
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UtilTest {
    val log = KotlinLogging.logger {}
    @Test
    fun generateJwtTest(){
        val jwtManager = JwtManager()

        val details = PrincipalDetails(Member.createFakeMember(1))
        val accessToken = jwtManager.generateAccessToken(details)

        val email = jwtManager.getMemberEmail(accessToken)

        log.info { "access token: $accessToken" }
        log.info { "email: $email" }

    }

    @Test
    fun bcryptEncodeTest(){
        val encoder = BCryptPasswordEncoder()
        val encpassword = encoder.encode("1234")
        log.info { "enc password: $encpassword" }
    }

}
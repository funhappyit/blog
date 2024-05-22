package com.example.util

import com.example.blog.config.security.JwtManager
import com.example.blog.config.security.PrincipalDetails
import com.example.blog.domain.member.Member
import org.junit.jupiter.api.Test

class UtilTest {

    @Test
    fun generateJwtTest(){
        val jwtManager = JwtManager()

        val details = PrincipalDetails(Member.createFakeMember(1))
        val accessToken = jwtManager.generateToken(details)


    }

}
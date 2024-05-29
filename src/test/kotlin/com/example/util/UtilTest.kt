package com.example.util

import com.example.blog.config.security.JwtManager
import com.example.blog.config.security.PrincipalDetails
import com.example.blog.domain.member.Member
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UtilTest {
    val log = KotlinLogging.logger {}
    val mapper = ObjectMapper()

    @Test
    fun generateJwtTest(){
        mapper.registerModule(JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
        val jwtManager = JwtManager(accessTokenExpireSecond = 60)

        val details = PrincipalDetails(Member.createFakeMember(1))
        val jsonPrincipal = mapper.writeValueAsString(details)
        val accessToken = jwtManager.generateAccessToken(jsonPrincipal)
        val decodedJWT = jwtManager.validatedJwt(accessToken)
        val principalString = decodedJWT.getClaim(jwtManager.claimPrincipal).asString()
        val principalDetails:PrincipalDetails =  mapper.readValue(principalString, PrincipalDetails::class.java)

        log.info { "result=>${principalDetails.member}" }
    }

    @Test
    fun bcryptEncodeTest(){
        val encoder = BCryptPasswordEncoder()
        val encpassword = encoder.encode("1234")
        log.info { "enc password: $encpassword" }
    }

}
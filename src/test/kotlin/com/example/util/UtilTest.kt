package com.example.util

import com.example.blog.config.security.JwtManager
import com.example.blog.config.security.PrincipalDetails
import com.example.blog.domain.HashMapRepositoryImpl
import com.example.blog.domain.InMemoryRepository
import com.example.blog.domain.member.Member
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.jvm.Throws

class UtilTest {
    val log = KotlinLogging.logger {}
    val mapper = ObjectMapper()

//    @Test
//    fun generateJwtTest(){
//        mapper.registerModule(JavaTimeModule())
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
//        val jwtManager = JwtManager(accessTokenExpireSecond = 1)
//
//        val details = PrincipalDetails(Member.createFakeMember(1))
//        val jsonPrincipal = mapper.writeValueAsString(details)
//        val accessToken = jwtManager.generateAccessToken(jsonPrincipal)
//
//        Thread.sleep(6000)
//
//        val decodedJWT = jwtManager.validatedJwt(accessToken)
//        val principalString = decodedJWT.getClaim(jwtManager.claimPrincipal).asString()
//        val principalDetails:PrincipalDetails =  mapper.readValue(principalString, PrincipalDetails::class.java)
//
//        log.info { "result=>${principalDetails.member}" }
//    }

    @Test
    fun bcryptEncodeTest(){
        val encoder = BCryptPasswordEncoder()
        val encpassword = encoder.encode("1234")
        log.info { "enc password: $encpassword" }
    }

    @Test
    fun hashMapRepoTest(){
        val repo:InMemoryRepository = HashMapRepositoryImpl()

        val numberOfThreads = 1000

        val service = Executors.newFixedThreadPool(10)
        val latch = CountDownLatch(numberOfThreads)

        for(index in 1..numberOfThreads){
            service.submit {
                repo.save(index.toString(), index)
                latch.countDown()
            }
        }
        latch.await()
        val results = repo.findAll()
        Assertions.assertEquals(results.size,numberOfThreads)
    }



}
package com.example.blog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging
import java.security.Principal
import java.util.*
import javax.crypto.SecretKey

class JwtManager {

    private val log = KotlinLogging.logger {}

    private val secretKey: String = "SFSDFSDF"
    private val claimEmail = "email"
    private val claimPassword = "password"
    private val expireTime = 1000*60*60


    fun generateAccessToken(principal:PrincipalDetails):String {

        val sign = JWT.create()
            .withSubject(principal.username)
            .withExpiresAt(Date(System.nanoTime()+expireTime))
            .withClaim("email",principal.username)
            .withClaim("password",principal.password)
            .sign(Algorithm.HMAC512(secretKey))
        return sign

    }

    fun getMemberEmail(token:String) : String? {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getClaim("email").asString()
    }




}
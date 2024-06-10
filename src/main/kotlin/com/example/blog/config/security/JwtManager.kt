package com.example.blog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.blog.util.CookieProvider
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.util.HashedWheelTimer
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.security.Principal
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey

class JwtManager(
    accessTokenExpireSecond:Long = 30, //1분
    refreshTokenExpireDay:Long = 7
) {

    private val log = KotlinLogging.logger {}

    private val accessSecretKey: String = "myAccessSecretKey"
    private val refreshSecretKey:String = "myRefreshSecretKey"

    private val claimEmail = "email"
    val claimPrincipal =  "principal"


    private val accessTokenExpireSecond:Long = accessTokenExpireSecond
    val refreshTokenExpireDay:Long = refreshTokenExpireDay

    val authorizationHeader = "Authorization"
    val jwtHeader = "Bearer "
    private val jwtSubject = "my-token"

    fun generateRefreshToken(principal: String):String{
        val expireDate = Date(System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(refreshTokenExpireDay));

        log.info{"refreshToken ExpireDate=>$expireDate"}

        return doGenerateToken(expireDate,principal,accessSecretKey)
    }

    private fun doGenerateToken(
        expireDate: Date,
        principal: String,
        secretKey: String
    ) = JWT.create()
        .withSubject(jwtSubject)
        .withExpiresAt(expireDate)
        .withClaim(claimPrincipal,principal)
        .sign(Algorithm.HMAC512(secretKey))


    fun generateAccessToken(principal:String):String {

        val expireDate = Date(System.currentTimeMillis()+ TimeUnit.SECONDS.toMillis(accessTokenExpireSecond));

        log.info{"accessToken ExpireDate=>$expireDate"}

//        val sign = JWT.create()
//            .withSubject(jwtSubject)
//            .withExpiresAt(expireDate)
//            .withClaim(claimPrincipal,principal)
//            .sign(Algorithm.HMAC512(accessSecretKey))
        return doGenerateToken(expireDate,principal,accessSecretKey)

    }

    fun getMemberEmail(token:String) : String? {
        return JWT.require(Algorithm.HMAC512(accessSecretKey)).build().verify(token).getClaim("email").asString()
    }

    fun getPrincipalStringByAccessToken(accessToken:String) : String? {
        val tokenResult = validatedJwt(accessToken)

        if(tokenResult.isValid){
            return tokenResult.decodeJWT?.getClaim(claimPrincipal)?.toString()?:throw RuntimeException("")
        }
        return ""
    }

    /*
    Kotlin에서 unionType이란 게
    String:Int
    */

    // jwt 유효한지 확인
    fun validatedJwt(token:String): TokenValidResult {
        try{
            val verifier: JWTVerifier = JWT.require(Algorithm.HMAC512(accessSecretKey)).build()
            val decodeJWT: DecodedJWT = verifier.verify(token)
           return TokenValidResult(isValid = true, decodeJWT = decodeJWT)
        } catch(e:JWTVerificationException){
            log.error("error=>${e.stackTraceToString()}")

            return TokenValidResult(isValid = false, exception = e)

        }
    }
    /*
       우리가 이미 발급한 refreshToken(쿠리로 감싸져있음) 이걸 꺼내가지고 요걸 토대로 다시 accessToken을 발급하는거에요
    */

}
/*
코틀린으로 unit type 흉내
*/

//sealed class TokenValidResult{
//    class Success(val value:Boolean = true, val decodedJwt:DecodedJWT): TokenValidResult()
//    class Failure(val value:JWTVerificationException): TokenValidResult()
//}

class TokenValidResult(
    val isValid:Boolean,
    val exception: JWTVerificationException? = null,
    val decodeJWT: DecodedJWT? = null
){


}

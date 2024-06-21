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
    accessTokenExpireSecond:Long = 1, //1분
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

        return doGenerateToken(expireDate,principal,refreshSecretKey)
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

    fun getPrincipalStringByAccessToken(accessToken:String) : String{
        val decodedJWT = getDecodeJwt(accessSecretKey,accessToken)


        return decodedJWT.getClaim(claimPrincipal).asString()
    }

    fun getPrincipalStringByRefreshToken(refreshToken:String) : String{
        val decodedJWT = getDecodeJwt(refreshSecretKey,refreshToken)

        return decodedJWT.getClaim(claimPrincipal).asString()
    }



    private fun getDecodeJwt(secretKey: String,token: String):DecodedJWT{
        val verifier: JWTVerifier = JWT.require(Algorithm.HMAC512(secretKey)).build()
        val decodeJWT: DecodedJWT = verifier.verify(token)
        return decodeJWT
    }


    fun validAccessToken(token:String): TokenValidResult {

        return validatedJwt(token,accessSecretKey)
    }

    fun validRefreshToken(token:String): TokenValidResult {

        return validatedJwt(token,refreshSecretKey)
    }



    /*
    Kotlin에서 unionType이란 게
    String:Int
    */

    // jwt 유효한지 확인
    private fun validatedJwt(token:String,secretKey: String): TokenValidResult {// TRUE : JWTVerificationException
        return try{
            getDecodeJwt(secretKey,token)
           //return TokenValidResult(isValid = true, decodeJWT = decodeJWT)
            TokenValidResult.Success()
        } catch(e:JWTVerificationException){
            log.error("error=>${e.stackTraceToString()}")

           // return TokenValidResult(isValid = false, exception = e)
            TokenValidResult.Failure(e)

        }
    }
    /*
       우리가 이미 발급한 refreshToken(쿠리로 감싸져있음) 이걸 꺼내가지고 요걸 토대로 다시 accessToken을 발급하는거에요
    */

}
/*
코틀린으로 unit type 흉내
*/

sealed class TokenValidResult{
    class Success(val successValue:Boolean = true): TokenValidResult()
    class Failure(val exception: JWTVerificationException): TokenValidResult()
}

//class TokenValidResult(
//    val isValid:Boolean,
//    val exception: JWTVerificationException? = null,
//    val decodeJWT: DecodedJWT? = null
//){
//
//
//}

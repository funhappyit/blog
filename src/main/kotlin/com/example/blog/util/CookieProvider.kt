package com.example.blog.util

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.ResponseCookie
import java.util.*

object CookieProvider {
    private val log = KotlinLogging.logger{}

    fun createNullCookie(cookieName:String):String{
        TODO()
    }

    fun createCookie(cookieName: CookieName,value:String,maxAge:Long){

        ResponseCookie.from(cookieName.name,value)
            .httpOnly(true)
            .secure(false) // http 허용
            .path("/")
            .maxAge(maxAge)
            .build()

    }

    fun getCookie(req:HttpServletRequest, cookieName: CookieName): Optional<String> {
        val cookieValue = req.cookies.filter { cookie->
            cookie.name == cookieName.name
        }.map{ cookie->
            cookie.value
        }.firstOrNull()

        log.info { "cookieValue=> $cookieValue" }

        return Optional.ofNullable(cookieValue)
    }

    enum class CookieName(
        val info:String
    ){
        REFRESH_COOKIE("refresh-cookie"),
    }

}
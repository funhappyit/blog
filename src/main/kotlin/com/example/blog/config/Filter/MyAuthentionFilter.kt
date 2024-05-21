package com.example.blog.config.Filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging

class MyAuthentionFilter :Filter{
    val log = KotlinLogging.logger { }
    /*
    인증처리를 하고 싶다.
    */
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val servletRequest = request as HttpServletRequest

        val principal = servletRequest.getSession().getAttribute("principal")
        if(principal == null) {
            throw RuntimeException("session not found!!")
        }else{
            chain?.doFilter(request, response)
        }
    }
}
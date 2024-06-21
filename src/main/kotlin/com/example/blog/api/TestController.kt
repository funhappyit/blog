package com.example.blog.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController() {
    @GetMapping("/health")
    fun healthTest():String = "hello blog"

    @GetMapping("/error") //시큐리티 default
    fun errorTest(): String{
        return "error"
    }

}
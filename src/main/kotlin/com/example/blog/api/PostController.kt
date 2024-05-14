package com.example.blog.api

import com.example.blog.service.PostService
import com.example.blog.util.value.CntResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun findPosts():CntResDto<*>{
        return CntResDto(HttpStatus.OK,"find posts",postService.findPost())
    }



}
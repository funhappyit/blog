package com.example.blog.service

import com.example.blog.domain.post.Post
import com.example.blog.domain.post.PostRepository
import com.example.blog.domain.post.PostRes
import com.example.blog.domain.post.toDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository
) {

    @Transactional(readOnly = true)
    fun findPost():List<PostRes> {
        return postRepository.findAll().map { it.toDto() }
    }
}
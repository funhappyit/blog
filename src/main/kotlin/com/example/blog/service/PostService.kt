package com.example.blog.service

import com.example.blog.domain.member.*
import com.example.blog.domain.post.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository
) {

    @Transactional(readOnly = true)
    fun findPost(pageable: Pageable): Page<PostRes> {
        return postRepository.findPosts(pageable).map{it.toDto()}
    }
    @Transactional
    fun save(dto: PostSaveReq): PostRes {
        return postRepository.save(dto.toEntity()).toDto()
    }

    @Transactional
    fun deletePost(id:Long){
        return postRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findById(id:Long): PostRes {
        return postRepository.findById(id).orElseThrow().toDto()
    }
}
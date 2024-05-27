package com.example.blog.api


import com.example.blog.domain.post.PostSaveReq
import com.example.blog.service.PostService
import com.example.blog.util.value.CntResDto
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RequestMapping("/v1")
@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun findPosts(@PageableDefault(size = 10) pageable: Pageable):CntResDto<*>{
        return CntResDto(HttpStatus.OK,"find posts",postService.findPost(pageable))
    }

    @GetMapping("/post/{id}")
    fun findById(@PathVariable("id") id:Long): CntResDto<Any>{

        return CntResDto(HttpStatus.OK,"find Post by id",postService.findById(id))
    }

    @DeleteMapping("/post/{id}")
    fun deleteById(@PathVariable("id") id:Long): CntResDto<Any> {
        return CntResDto(HttpStatus.OK,"delete Post by id",postService.deletePost(id))
    }

    @PostMapping("/post")
    fun save(@Valid @RequestBody dto: PostSaveReq): CntResDto<*> {
        return CntResDto(HttpStatus.OK, resultMsg = "save Member",postService.save(dto))
    }

}
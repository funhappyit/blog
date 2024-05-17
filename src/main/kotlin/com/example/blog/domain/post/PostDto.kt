package com.example.blog.domain.post

import com.example.blog.domain.member.Member
import com.example.blog.domain.member.MemberRes
import faker.com.ibm.icu.text.CaseMap.Title
import jakarta.validation.constraints.NotNull
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy.Content

data class PostSaveReq(
    @field:NotNull(message = "require title")
    val title: String?,
    val content: String?,
    val memberId:Long?
)

fun PostSaveReq.toEntity(): Post {
    return Post(
        title = this.title?:"",
        content = this.content?:"",
        member = Member.createFakeMember(this.memberId!!)
    )
}

data class PostRes(
    val id:Long,
    val title: String,
    val content: String,
    val member:MemberRes
)
package com.example.blog.domain.post

import com.example.blog.domain.member.Member
import faker.com.ibm.icu.text.CaseMap.Title
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy.Content

data class PostSaveReq(
    val title: String,
    val content: String,
    val memberId:Long
)

fun PostSaveReq.toEntity(): Post {
    return Post(
        title = this.title,
        content = this.content,
        member = Member.createFakeMember(this.memberId)
    )
}
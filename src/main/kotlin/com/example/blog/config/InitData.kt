package com.example.blog.config

import com.example.blog.domain.member.Member
import com.example.blog.domain.member.MemberRepository
import com.example.blog.domain.member.Role
import com.example.blog.service.MemberService
import io.github.serpro69.kfaker.faker
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository : MemberRepository
) {

    val faker = faker {  }

    @EventListener(ApplicationReadyEvent::class)
    private fun init(){
        val member = Member(
            email = faker.internet.safeEmail(),
            password = "1234",
            role=Role.USER
        )

        memberRepository.save(member)
    }

}
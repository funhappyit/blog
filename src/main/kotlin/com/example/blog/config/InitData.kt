package com.example.blog.config

import com.example.blog.domain.member.*
import com.example.blog.domain.post.Post
import com.example.blog.domain.post.PostRepository
import com.example.blog.domain.post.PostSaveReq
import com.example.blog.domain.post.toEntity
import io.github.serpro69.kfaker.faker
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository

) {

    val faker = faker {  }
    private val log = KotlinLogging.logger (fun(){

    })
    @EventListener(ApplicationReadyEvent::class)
    private fun init(){
//        val members = generateMembers(100)

//        memberRepository.saveAll(members)
//        log.info { "insert: $members" }
//        val posts= generatePosts(100)
//        postRepository.saveAll(posts)
//        log.info { "insert: $posts" }
    }

    private fun generateMembers(cnt: Int): MutableList<Member> {
        val members = mutableListOf<Member>()

        for (i in 1..cnt) {
            val member = generateMember()
            log.info { "insert $member" }
            members.add(member)
        }
        return members
    }

    private fun generatePosts(cnt:Int) : MutableList<Post>{
        val posts = mutableListOf<Post>()

        for(i in 1..cnt){
            val post = generatePosts()
            log.info { "insert $post" }
            posts.add(post)
        }
        return posts
    }

    private fun generateMember(): Member =
        LoginDto(
            email = faker.internet.safeEmail(),
            password = "1234",
            role = Role.USER
        ).toEntity()

    private fun generatePosts(): Post = PostSaveReq(
        title = faker.theExpanse.ships(),
        content = faker.quote.matz(),
        memberId = 1
    ).toEntity()
}
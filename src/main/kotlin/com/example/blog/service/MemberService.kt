package com.example.blog.service

import com.example.blog.domain.member.*
import com.example.blog.exception.MemberNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

//    @Transactional(readOnly = true)
//    fun findAll():MutableList<Member> = memberRepository.findAll()

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<MemberRes> =
        memberRepository.findMembers(pageable).map {
            it.toDto()
        }

    @Transactional
    fun saveMember(dto: LoginDto): MemberRes {

        return memberRepository.save(dto.toEntity()).toDto()

    }

    @Transactional
    fun deleteMember(id:Long){
        return memberRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findMemberById(id:Long):MemberRes{

        return memberRepository.findById(id)
            .orElseThrow{
                throw MemberNotFoundException(id.toString())
            }.toDto()

    }

}
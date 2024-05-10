package com.example.blog.domain.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository:JpaRepository<Member,Long> {



}
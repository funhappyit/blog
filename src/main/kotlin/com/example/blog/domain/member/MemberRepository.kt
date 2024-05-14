package com.example.blog.domain.member

import com.linecorp.kotlinjdsl.query.spec.ExpressionOrderSpec
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository:JpaRepository<Member,Long>, MemberCustomRepository{



}

interface MemberCustomRepository{
    fun findMembers():List<Member>
}

class MemberCustomRepositoryImpl(
    private val queryFactory: SpringDataQueryFactory,
): MemberCustomRepository{

    override fun findMembers(): List<Member> {
        return queryFactory.listQuery {
            select(entity(Member::class))
            from(entity(Member::class))
            orderBy(ExpressionOrderSpec(column(Member::id),false))
        }
    }
}

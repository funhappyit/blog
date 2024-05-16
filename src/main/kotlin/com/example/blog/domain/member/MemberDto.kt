package com.example.blog.domain.member

import jakarta.validation.constraints.NotNull


/*
dto<->entity 간의 매핑할 때, 크게 스타일이 2개 있는거 같더라구요.

1. 각 dto, entity에 책임 할당
2.  entitymanger라는 놈을 하나 만들어서 개가 담당하게끔 하는 스타일도 있는데

*/
data class MemberSaveReq(
    @field:NotNull(message = "require email")
    val email:String?,
    val password:String?,
    val role:Role?
)
fun MemberSaveReq.toEntity():Member{
    return Member(
        email=this.email?:"",
        password=this.password?:"",
        role=this.role?:Role.USER
    )
}
data class MemberRes(
    val id:Long,
    val email: String,
    val password: String,
    val role: Role
)

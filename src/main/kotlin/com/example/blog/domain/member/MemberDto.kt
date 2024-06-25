package com.example.blog.domain.member

import com.example.blog.config.BeanAccessor
import jakarta.validation.constraints.NotNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime


/*
dto<->entity 간의 매핑할 때, 크게 스타일이 2개 있는거 같더라구요.

1. 각 dto, entity에 책임 할당
2.  entitymanger라는 놈을 하나 만들어서 개가 담당하게끔 하는 스타일도 있는데

*/
data class LoginDto(
    @field:NotNull(message = "require email")
    val email:String?,
    val rawPassword:String?,
    val role:Role?
){
    fun toEntity():Member{
        return Member(
            email=this.email?:"",
            password= encodeRawPassword()?:"",
            role=this.role?:Role.USER
        )
    }
    private fun encodeRawPassword(): String = BeanAccessor.getBean(PasswordEncoder::class).encode(this.rawPassword)



}

data class MemberRes(
    val id:Long,
    val email: String,
    val password: String,
    val role: Role,
    val createAt:LocalDateTime,
    val updateAt:LocalDateTime

)

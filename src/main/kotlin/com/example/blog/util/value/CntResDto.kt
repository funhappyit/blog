package com.example.blog.util.value

data class CntResDto<T> (
    val resultCode:T,
    val resultMsg:String,
    val data:T
)
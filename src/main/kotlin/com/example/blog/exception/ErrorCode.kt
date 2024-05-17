package com.example.blog.exception

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    val value: String,
    val message: String
) {

    INVALID_INPUT_VALUE("C001","Invalid input value"),
    EMPTY_INPUT_VALUE("C002","Empty not found"),

}
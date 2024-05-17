package com.example.blog.exception

class EntityNotFoundException(message:String?): BusinessException(message,ErrorCode.EMPTY_INPUT_VALUE){
}
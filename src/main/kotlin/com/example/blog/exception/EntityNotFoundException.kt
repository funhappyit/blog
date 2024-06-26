package com.example.blog.exception

sealed class EntityNotFoundException(message:String?): BusinessException(message,ErrorCode.EMPTY_INPUT_VALUE)

class MemberNotFoundException(id:String): EntityNotFoundException("$id not found")
package com.example.blog.exception

sealed class BusinessException : RuntimeException {

    private var errorCode:ErrorCode
        get() {
            return this.errorCode

        }
    constructor(errorCode: ErrorCode):super(errorCode.message){
        this.errorCode = errorCode
    }
    constructor(message: String?, errorCode: ErrorCode):super(message){
        this.errorCode = errorCode
    }

}
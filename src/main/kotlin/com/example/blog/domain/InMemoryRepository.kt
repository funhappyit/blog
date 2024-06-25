package com.example.blog.domain

import java.util.Optional

interface InMemoryRepository {

    fun clear()
    fun remove(key:String): Any?
    fun findAll():ArrayList<Any>
    fun findByKey(key:String):Optional<Any>
    fun save(key:String,value:Any)
}
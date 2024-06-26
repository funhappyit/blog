package com.example.blog.domain

import mu.KotlinLogging
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

//@Repository
class HashMapRepositoryImpl(

): InMemoryRepository{
    private val log = KotlinLogging.logger{}
    private val store = ConcurrentHashMap<String,Any>()

    override fun save(key: String, value: Any) {
        Thread.sleep(50)
        store[key] = value
    }
    override fun clear() {
        store.clear()
    }

    override fun remove(key: String): Any? {
        return store.remove(key)
    }

    override fun findAll(): ArrayList<Any> {
        return ArrayList<Any>(store.values)
    }

    override fun findByKey(key: String): Optional<Any> {
       return Optional.ofNullable(store.get(key))
    }



}
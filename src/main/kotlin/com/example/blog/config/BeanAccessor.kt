package com.example.blog.config

import com.example.blog.util.value.Cat
import com.example.blog.util.value.Dog
import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass

@Configuration(proxyBeanMethods = true)
class BeanAccessor(
) : ApplicationContextAware{

    private val log = KotlinLogging.logger{ }

    init {
        log.info { "this BeanAccessor=>$this" }
    }



    override fun setApplicationContext(applicationContext: ApplicationContext) {
        BeanAccessor.applicationContext = applicationContext
    }

    companion object{
        private lateinit var applicationContext: ApplicationContext

        fun <T:Any> getBean(type:KClass<T>): T {
            return applicationContext.getBean(type.java)
        }

        fun <T:Any> getBean(name:String,type:KClass<T>): T {
            return applicationContext.getBean(type.java)
        }
    }
}
package com.example.blog.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {


    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()

        mapper.registerModule(JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)

        mapper.registerModules(
            KotlinModule.Builder()
                .configure(KotlinFeature.StrictNullChecks,false)
                .configure(KotlinFeature.NullIsSameAsDefault,false)
                .configure(KotlinFeature.NullToEmptyMap,false)
                .configure(KotlinFeature.NullToEmptyCollection,false)
                .configure(KotlinFeature.SingletonSupport,false)
                .build()

        )

        return mapper
    }

}
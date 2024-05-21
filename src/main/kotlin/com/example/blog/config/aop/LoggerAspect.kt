package com.example.blog.config.aop

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/*
전통적인 방식의 프록시 기반 스프링 aop
*/
@Component
@Aspect
class LoggerAspect {

    val log = KotlinLogging.logger {  }

    @Pointcut("execution(* com.example.blog.api.*Controller.*(..))")
    protected fun controllerCut() = Unit

   @Before("controllerCut()")
    fun controllerLoggerAdvice(joinPoint: JoinPoint){
        val typeName = joinPoint.signature.declaringTypeName
        val methodName = joinPoint.signature.name
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

       log.info {
           """
               request url:${request.servletPath}
               type:$typeName
               method:$methodName
           """.trimIndent()
       }
    }

    @AfterReturning(pointcut = "controllerCut()", returning = "result")
    fun controllerLogAfter(joinPoint: JoinPoint, result: Any){

        val mapper = ObjectMapper()

        log.info {"""
            ${joinPoint.signature.name}
            method return value: ${mapper.writeValueAsString(result)}
        """.trimIndent()
        }

    }


}
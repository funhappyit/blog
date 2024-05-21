package com.example.blog.config.Filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    /*
    이런 필터랑 인터셉터를 이용해서 만든 강력한 인증처리 관련 프레임워크
    스프링 시큐리티
    */


    @Bean
    fun registMyAuthentionFilter(): FilterRegistrationBean<MyAuthentionFilter> {

        val bean = FilterRegistrationBean(MyAuthentionFilter())

        bean.addUrlPatterns("/api/*")
        bean.order = 0

        return bean
    }

}
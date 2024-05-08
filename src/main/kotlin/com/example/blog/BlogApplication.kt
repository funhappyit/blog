package com.example.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogApplication

fun main(args: Array<String>) {
    runApplication<BlogApplication>(*args)
}
/*
이제 사이드 컨텐츠를 생각 중인데
springboot+kotlin+JPA
front:react+typescript+zustand
배포:aws ec2(프리티어,) + s3+ codedeploy+github action
*/

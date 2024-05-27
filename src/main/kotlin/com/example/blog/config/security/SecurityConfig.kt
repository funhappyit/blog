package com.example.blog.config.security

import com.example.blog.domain.member.MemberRepository
import com.example.blog.util.func.responseData
import com.example.blog.util.value.CntResDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val memberRepository: MemberRepository
) {
    private val log = KotlinLogging.logger {}
    //@Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity -> web.ignoring().requestMatchers("/**") }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf{csrf->csrf.disable()}
            .headers { headers -> headers
                .frameOptions { frameOptions -> frameOptions.disable() }
            }
            .formLogin { formLogin->formLogin.disable() }
            .httpBasic { httpBasic->httpBasic.disable() }
            .sessionManagement{sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .cors{cors->cors.configurationSource(corsConfig()) }
            .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
           // .exceptionHandling {accessDeniedHandler->accessDeniedHandler.accessDeniedHandler(CustomAccessDeniedHandler())}
            .exceptionHandling { authenticationEntryPoint->authenticationEntryPoint.authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper)) }
            .authorizeHttpRequests{antMatchers->antMatchers.requestMatchers("/**").authenticated()}
        //.authorizeHttpRequests{antMatchers->antMatchers.anyRequest().permitAll()}


        return http.build();
    }
    class CustomAuthenticationEntryPoint(
        private val objectMapper: ObjectMapper
    ): AuthenticationEntryPoint {
        private val log = KotlinLogging.logger {}
        override fun commence(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authException: AuthenticationException?
        ) {
            log.info { "???access denied!!!" }
            //response.sendError(HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.reasonPhrase)
            response.sendError(HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.reasonPhrase)


          //  val cntResDto = CntResDto(HttpStatus.UNAUTHORIZED,"access denied",authException)
          //  responseData(response, objectMapper.writeValueAsString(cntResDto))
            //response.sendError(HttpServletResponse.SC_FORBIDDEN)
        }

    }



    class CustomAccessDeniedHandler : AccessDeniedHandler {

        private val log = KotlinLogging.logger {}

        override fun handle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            accessDeniedException: AccessDeniedException?
        ) {
            log.info { "access denied!!!" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
        }

    }

    @Bean
    fun authenticationFilter(): CustomBasicAuthenticationFilter {

        return CustomBasicAuthenticationFilter(
            authenticationManager = authenticationManager(),
            memberRepository = memberRepository
        )
    }


    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {

        return BCryptPasswordEncoder()
    }


    @Bean
    fun loginFilter(): UsernamePasswordAuthenticationFilter {

        val authenticationFilter = CustomUserNameAuthenticationFilter(objectMapper)
        authenticationFilter.setAuthenticationManager(authenticationManager())
        authenticationFilter.setFilterProcessesUrl("/login")
        authenticationFilter.setAuthenticationFailureHandler(CustomFailureHandler())
        authenticationFilter.setAuthenticationSuccessHandler(CustomSuccessHandler())

        return authenticationFilter
    }

    class CustomFailureHandler:AuthenticationFailureHandler{
        private val log = KotlinLogging.logger {}
        override fun onAuthenticationFailure(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            exception: AuthenticationException?
        ) {
           log.info { "로그인 실패!!!!!" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"인증 실패")
        }

    }


    class CustomSuccessHandler:AuthenticationSuccessHandler{
        private val log = KotlinLogging.logger {}
        override fun onAuthenticationSuccess(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            authentication: Authentication?
        ) {
            log.info { "login Success !!!" }
        }


    }


    @Bean
    fun corsConfig(): UrlBasedCorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addExposedHeader("authorization")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }





}
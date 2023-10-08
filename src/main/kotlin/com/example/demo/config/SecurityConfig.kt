package com.example.demo.config

import com.example.demo.common.JwtTokenProvider
import com.example.demo.common.LoggerExtensions.createLogger
import com.example.demo.common.Token
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(
    val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .csrf { it.disable() }
//            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeHttpRequests {
                /**
                 * To configure ROLE related stuff, it should be defined before .authenticated()
                 * ex) it.requestMatchers("/api/v1/test02").hasRole("USER")
                 */
                it.requestMatchers( "/signin/**", "/signup/**", "/refresh/**").permitAll()
                it.anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
                it.accessDeniedHandler(CustomAccessDeniedHandler())
            }
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        jwtTokenProvider.resolveToken(request)?.let {
            if (jwtTokenProvider.validateToken(it, Token.ACCESS)) {
                try {
                    val authentication = jwtTokenProvider.getAuthentication(it)
                    SecurityContextHolder.getContext().authentication = authentication
                } catch (e: UsernameNotFoundException) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
                    return
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}

/**
 * 401 error handling
 */
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val log = createLogger()

    override fun commence(
        req: HttpServletRequest?,
        res: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        log.error("CustomAuthenticationEntryPoint - {}", authException?.message)
        res?.sendError(HttpServletResponse.SC_UNAUTHORIZED)
    }
}

/**
 * 403 error handling
 */
class CustomAccessDeniedHandler : AccessDeniedHandler {

    private val log = createLogger()

    override fun handle(
        req: HttpServletRequest?,
        res: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        log.error("CustomAccessDeniedHandler - {}", accessDeniedException?.message)
        res?.sendError(HttpServletResponse.SC_FORBIDDEN)
    }
}
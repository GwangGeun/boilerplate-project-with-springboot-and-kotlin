package com.example.demo.common

import com.example.demo.application.service.CustomUserDetailService
import com.example.demo.common.LoggerExtensions.createLogger
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    private val customUserDetailService: CustomUserDetailService
) {

    private val log = createLogger()

    private val TOKEN_PREFIX: String = "Bearer "
    private val HEADER_STRING: String = "Authorization"
    private val tokenValidTime: Long = 30 * 60 * 1000L // 30 mins
    private val secretKey: String = "auth-jwt-secretKey-ax3et!@5gg5dsf@m3sdf3!!45%%"
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createToken(id: Long): String {
        val claims: Claims = Jwts.claims()
        claims["id"] = id
        val now = Date()
        return TOKEN_PREFIX + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + tokenValidTime))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = customUserDetailService.loadUserByUsername(getEmailFromToken(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getEmailFromToken(token: String): String {
        return Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).body["id"].toString()
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader(HEADER_STRING)
    }

    fun validateToken(token: String): Boolean {
        return try {
            val parseClaimsJws =
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
            !parseClaimsJws.body.expiration.before(Date())
        } catch (e: Exception) {
            log.error("validateToken exception {}", e.message)
            false
        }
    }

}
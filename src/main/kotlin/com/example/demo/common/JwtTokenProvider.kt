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

    private val tokenPrefix: String = "Bearer "
    private val authHeader: String = "Authorization"

    private val accessTokenValidTime: Long = 30 * 60 * 1000L // 30 mins
    private val accessSecretKey: String = "auth-jwt-secretKey-ax3et!@5gg5dsf@m3sdf3!!45%%"
    private val accessKey = Keys.hmacShaKeyFor(accessSecretKey.toByteArray())

    private val refreshTokenValidTime: Long = 7 * 24 * 60 * 60 * 1000L // 7 days
    private val refreshSecretKey: String = "auth-jwt-secretKey-ams@5AtGd21azm3sdfu@!zwlm"
    private val refreshKey = Keys.hmacShaKeyFor(refreshSecretKey.toByteArray())

    fun createToken(id: Long, type: Token): String {
        val claims: Claims = Jwts.claims()
        claims["id"] = id
        claims["type"] = type.name
        val now = Date()

        return if (type == Token.REFRESH) {
            tokenPrefix + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + refreshTokenValidTime))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact()
        } else {
            tokenPrefix + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + accessTokenValidTime))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact()
        }
    }

    fun getAuthentication(token: String, type: Token = Token.ACCESS): Authentication {
        val userDetails = customUserDetailService.loadUserByUsername(getEmailFromToken(token, type))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getEmailFromToken(token: String, type: Token): String {
        return if (type == Token.REFRESH) {
            Jwts.parserBuilder().setSigningKey(refreshKey).build()
                .parseClaimsJws(token.replace(tokenPrefix, "")).body["id"].toString()
        } else {
            Jwts.parserBuilder().setSigningKey(accessKey).build()
                .parseClaimsJws(token.replace(tokenPrefix, "")).body["id"].toString()
        }
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader(authHeader)
    }

    fun validateToken(token: String, type: Token): Boolean {
        return try {
            if (type == Token.REFRESH) {
                val parseClaimsJws =
                    Jwts.parserBuilder().setSigningKey(refreshKey).build()
                        .parseClaimsJws(token.replace(tokenPrefix, ""))
                !parseClaimsJws.body.expiration.before(Date())
            } else {
                val parseClaimsJws =
                    Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token.replace(tokenPrefix, ""))
                !parseClaimsJws.body.expiration.before(Date())
            }
        } catch (e: Exception) {
            log.error("validateToken exception {}", e.message)
            false
        }
    }

}

enum class Token {
    REFRESH,
    ACCESS;
}
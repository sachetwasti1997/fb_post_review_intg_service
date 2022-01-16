package com.example.post_review_int_service.config

import com.example.post_review_int_service.dtos.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import kotlin.collections.HashMap

@Service
class JwtUtil {

    val SECRET_KEY = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun extractUserName(token:String):String{
        return extractClaim(token, Claims::getSubject)
    }

    fun extractExpiration(token: String) : Date {
        return extractClaim(token, Claims::getExpiration)
    }

    fun <T>extractClaim(token: String, claimResolver: Function<Claims, T>): T{
        val claims = extractAllClaims(token)
        return claimResolver.apply(claims)
    }

    fun extractAllClaims(token: String): Claims {
        val key = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray())
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun isTokenExpired(token: String):Boolean{
        return extractExpiration(token).before(Date())
    }

    fun generateToken(userDto: User?):String{
        val claims = HashMap<String, Any>()
        return createToken(claims, userDto)
    }

    fun createToken(claims: Map<String, Any>, userDto: User?):String{
        val key = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray())
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDto?.email)
            .claim("roles", userDto?.roles)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 10 * 60 * 60 * 1000))
            .signWith(key)
            .compact()
    }

    fun validateToken(token:String, userDto: User?):Boolean{
        val userName = extractUserName(token)
        return (userName.equals(userDto?.email) && !isTokenExpired(token))
    }

}
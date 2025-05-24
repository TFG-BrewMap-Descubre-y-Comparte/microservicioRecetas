package com.project.recipes.persistance.models;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
	
	private final String secretKey = "mi_clave_secreta_super_segura";

	public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

          
            return claims.get("id_user", Integer.class);

        } catch (Exception e) {
            System.out.println("Error al leer el token: " + e.getMessage());
            return null;
        }
    }
}

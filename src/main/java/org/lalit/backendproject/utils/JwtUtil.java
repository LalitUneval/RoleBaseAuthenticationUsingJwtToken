package org.lalit.backendproject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {



    public String generateToken(UserDetails userDetails) {



            Map<String, Object> claims = new HashMap<>();

            // Extract authorities and add to claims
            claims.put("roles", userDetails.getAuthorities().stream()
                    .map(authorites->authorites.getAuthority())
                    .collect(Collectors.toList()));


            return Jwts.builder()
                    .claims(claims)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 5* 60 * 1000)) // 5 minutes
                    .signWith(generateKey())
                    .compact();


    }

    private SecretKey generateKey(){
        byte [] decode=Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }
    private String getSecretKey(){
        return "yMSC8Ta3Zo0pIDh0MVVrxghb46ks8pqyxfeuNOnSWlY=";
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

   public String extractUsername(String token){
        return  getClaims(token).getSubject();
   }
   public List<String> extractRoles(String token){
        List<String> listOfRole =getClaims(token).get("roles",List.class);
       System.out.println("roles is :"+listOfRole);
        return listOfRole;
   }
   public boolean validateToken(UserDetails userDetails , String token){
        return extractUsername(token).equals(userDetails.getUsername()) && !tokenExpired(token);
   }
   public boolean tokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
   }
}

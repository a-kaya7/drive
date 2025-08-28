package net.drive.config;

import java.io.IOException;

import java.util.List;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
   

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
        
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    	
    	    String token = request.getHeader("Authorization"); 
    	    if (token != null && token.startsWith("Bearer ")) {
    	        token = token.substring(7);
    	        try {
    	            Claims claims = jwtService.parseToken(token);  
    	            String benutzerkennung = claims.get("benutzerkennung", String.class);
    	            String benutzergruppe = claims.get("benutzergruppe", String.class);
    	            String mandant = claims.get("mandant", String.class);

    	            request.setAttribute("benutzerkennung", benutzerkennung);
    	            request.setAttribute("benutzergruppe", benutzergruppe);
    	            request.setAttribute("mandant", mandant);

    	            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + benutzergruppe));
    	            UsernamePasswordAuthenticationToken auth = 
    	                    new UsernamePasswordAuthenticationToken(benutzerkennung, null, authorities);
    	            SecurityContextHolder.getContext().setAuthentication(auth);
    	        } catch (Exception e) {
    	        	 System.out.println(e.getMessage());    
    	        }
    	     
    	    }

    	    filterChain.doFilter(request, response);
    }
}

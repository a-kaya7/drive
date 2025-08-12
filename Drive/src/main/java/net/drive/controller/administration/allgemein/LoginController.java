package net.drive.controller.administration.allgemein;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.dto.administration.allgemein.LoginDTO;


@RestController
@RequestMapping("/api")
public class LoginController {
	
	 private final AuthenticationManager authenticationManager;

	    public LoginController(AuthenticationManager authenticationManager) {
	        this.authenticationManager = authenticationManager;
	    }
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDto){
		try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDto.benutzerkennung(),
                    loginDto.passwort()
                )
            );
            return ResponseEntity.ok("");

	} catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("");
}
	}
}

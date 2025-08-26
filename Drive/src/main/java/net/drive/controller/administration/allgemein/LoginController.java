package net.drive.controller.administration.allgemein;




import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.dto.administration.allgemein.LoginDTO;

import net.drive.model.dto.administration.allgemein.PasswortWechselDTO;
import net.drive.services.administration.allgemein.innensicht.LoginService;


@RestController
@RequestMapping("/api")
public class LoginController {
	

    
    private final LoginService loginService;
    
   
    public LoginController(LoginService loginService){
    	this.loginService = loginService;
    	
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
        	 Map<String, Object> loginResult = loginService.login(loginDto);
        	return ResponseEntity.ok(loginResult);
        } catch(BadCredentialsException e) {
        	 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    /*
     * für Passwortwechsel, wenn paasswortAenderung true ist.
     */
    @PostMapping("/benutzer/passwortWechsel")
    public ResponseEntity<?> passwortWechsel(@RequestBody PasswortWechselDTO passwortWDto) {
    	 try {
             return ResponseEntity.ok(loginService.passwortWechsel(passwortWDto));
         } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
         }
    }
    /*
     * für MFA wenn Mfa true ist.
     */
    @PostMapping("/login/mfa")
    public ResponseEntity<?> verifyMfa(@RequestBody Map<String, Object> body) {
    	try {
            String benutzerkennung = (String) body.get("benutzerkennung");
            int code = Integer.parseInt(body.get("mfaCode").toString());
            return ResponseEntity.ok(Map.of("token", loginService.verifyMfa(benutzerkennung, code)));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MFA ist ungültig");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

package net.drive.controller.administration.allgemein;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class LogoutController {
  
	
	private final HttpServletRequest request;
	
	public LogoutController(HttpServletRequest request) {
		this.request = request;
	}
	
	@PostMapping("/logout")
	public String logout() {
		HttpSession session = request.getSession(false);
		if(session != null) {
			//session.invalidate();
		}
		return "Ausgeloggt";	
		
	}
	@GetMapping("/check-session")
    public String checkSession() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("Session ID in check: " + session.getId());
            return "Session gültig " + session.getAttribute("username");
        }
        return "Session ungültig";
    }
}

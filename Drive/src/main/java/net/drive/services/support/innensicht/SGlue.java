package net.drive.services.support.innensicht;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;
import net.drive.model.entities.administration.allgemein.Institut;
import net.drive.model.entities.administration.allgemein.Mandant;

public class SGlue {
	// holt den aktuellen Benutzer
	static public User GetUser(HttpSession session ) {
		synchronized(session) {
			 return (User) session.getAttribute("DUser");
			
		}
	}
	static public void SetUser(HttpSession session, User user) {
		synchronized(session) {
            session.setAttribute("DUser", user);
        }
    }
	// holt den aktuellen Mandant
	static public Mandant GetMandant(HttpSession session) {
		synchronized(session) {
			User u = GetUser(session);
			
			if(u != null) {
				return u.mandant;
			}else {
				return null;
			}
		}
	}
	// holt das aktuellen Institut
	static public Institut GetInstitut(HttpSession session) {
		synchronized(session) {
			User u = GetUser(session);
			
			if(u != null && u.mandant != null) {
				return u.mandant.getInstitut();
			}else {
				return null;
			}
		}
	}
	// holt den aktuellen Benutzer ohne Parameter
	public static User GetUser() {
        ServletRequestAttributes attr = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpSession session = attr.getRequest().getSession(false);
            if (session != null) {
                return GetUser(session);
            }
        }
        return null;
    }
	


}

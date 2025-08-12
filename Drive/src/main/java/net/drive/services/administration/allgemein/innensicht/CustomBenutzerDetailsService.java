package net.drive.services.administration.allgemein.innensicht;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.repository.administration.allgemein.IBenutzerRepository;

@Service
public class CustomBenutzerDetailsService implements UserDetailsService {
	
	private final IBenutzerRepository benutzerRepo;

    public CustomBenutzerDetailsService(IBenutzerRepository benutzerRepo) {
        this.benutzerRepo = benutzerRepo;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		  Benutzer benutzer = benutzerRepo.findByBenutzerkennung(username)
		            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		        return new CustomBenutzerDetails(benutzer);
	}

}

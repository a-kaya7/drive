package net.drive.services.administration.allgemein.innensicht;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import net.drive.model.entities.administration.allgemein.Benutzer;

public class CustomBenutzerDetails implements UserDetails {
	
	private final Benutzer benutzer;

    public CustomBenutzerDetails(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return benutzer.getPasswort();
	}

	@Override
	public String getUsername() {
		return benutzer.getBenutzerkennung();
	}

}

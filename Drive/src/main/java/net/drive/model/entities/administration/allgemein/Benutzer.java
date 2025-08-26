package net.drive.model.entities.administration.allgemein;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "benutzer")
@Data
public class Benutzer implements UserDetails {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(updatable = false, nullable = false)
	private UUID benutzerId;

	@Column(name = "benutzerkennung")
	private String benutzerkennung;

	@Column(name = "anrede")
	private String anrede;

	@Column(name = "vorname")
	private String vorname;

	@Column(name = "nachname")
	private String nachname;

	@Column(name = "email")
	private String email;

	@Column(name = "benutzerVon")
	private LocalDate benutzerVon;

	@Column(name = "benutzerBis")
	private LocalDate benutzerBis;

	@Column(name = "passwort")
	private String passwort;

	@Transient
	private String passwortWiederholung;

	@Column(name = "passwortAb")
	private LocalDate passwortAb;

	@Column(name = "zeitraumPasswort")
	private int zeitraumPasswort;

	@Column(name = "passwortAenderung")
	private boolean passwortAenderung;

	@Column(name = "mfa")
	private boolean mfa;
	
	@Column(name = "mfa_secret")
    private String mfaSecret;

	@ManyToOne
	@JoinColumn(name = "benutzergruppe")
	private Benutzergruppe benutzergruppe;
	
	@ManyToOne
	@JoinColumn(name = "mandant")
	private Mandant mandant;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return List.of(new SimpleGrantedAuthority(benutzergruppe.getBenutzergruppe()));
	}


	@Override
	public String getPassword() {
	
		return getPasswort();
	}

	@Override
	public String getUsername() {
		return getBenutzerkennung();
	}
	
	@Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

}

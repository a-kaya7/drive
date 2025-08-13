package net.drive.model.entities.administration.allgemein;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

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
public class Benutzer {

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

}

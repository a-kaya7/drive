package net.drive.model.dto.administration.allgemein;

import java.time.LocalDate;

import java.util.UUID;



public record BenutzerDTO(
		UUID id,
		String benutzerkennung,
		String anrede,
		String vorname,
		String nachname,
		String email,
		LocalDate benutzerVon,
		LocalDate benutzerBis,
		String passwort,
		String passwortWiederholung,
		LocalDate passwortAb,
		int zeitraumPasswort,
		boolean passwortAenderung,
		boolean mfa,
		String benutzergruppe,
		String mandant
		
		) {

}

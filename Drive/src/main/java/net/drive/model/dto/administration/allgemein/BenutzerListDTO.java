package net.drive.model.dto.administration.allgemein;

import java.time.LocalDate;
import java.util.UUID;

public record BenutzerListDTO(
		UUID id,
		String benutzerkennung,
		String vorname,
		LocalDate benutzerBis,
		String benutzergruppe
		) {

}

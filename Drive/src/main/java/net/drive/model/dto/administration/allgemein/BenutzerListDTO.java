package net.drive.model.dto.administration.allgemein;

import java.time.LocalDate;
import java.util.UUID;

public record BenutzerListDTO(
		UUID benutzerId,
		String benutzerkennung,
		String vorname,
		LocalDate benutzerBis,
		String benutzergruppe,
		String mandant
		) {

}

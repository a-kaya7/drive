package net.drive.model.dto.administration.allgemein;

import java.util.UUID;

public record InstitutListDTO(
		UUID InstitutId,
		String institutsname,
		String beschreibung,
		String locale,
		String telefon
		
		) {

}

package net.drive.model.dto.administration.allgemein;

import java.util.UUID;

public record MandantListDTO(
		UUID id,
		String idname,
		String beschreibung,
		String locale,
		String telefon
		
		) {

}

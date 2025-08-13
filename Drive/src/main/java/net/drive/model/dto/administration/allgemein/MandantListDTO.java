package net.drive.model.dto.administration.allgemein;

import java.util.UUID;

public record MandantListDTO(
		UUID mandantId,
		String idname,
		String beschreibung,
		String locale,
		String telefon
		
		) {

}

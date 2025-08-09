package net.drive.model.dto.administration.allgemein;

import java.util.UUID;

import net.drive.model.datentypen.Adresse;

public record MandantDTO(
		UUID id,
		String idname,
		String beschreibung,
		UUID institut,
		String locale,
		String telefon,
		Adresse adresse,
		String email
		
		) {

}

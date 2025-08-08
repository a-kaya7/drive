package net.drive.model.dto.administration.allgemein;

import java.util.UUID;

import net.drive.model.datentypen.Adresse;

public record InstitutDTO(
		UUID id,
		String institutsname,
		String bezeichnung,
		String iban,
		String bic,
		String waehrung,
		String locale,
		Adresse adresse,
		String telefon,
		String email
		
		) {

}

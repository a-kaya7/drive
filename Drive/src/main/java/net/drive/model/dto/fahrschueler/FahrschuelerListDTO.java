package net.drive.model.dto.fahrschueler;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import net.drive.model.datentypen.Adresse;

public record FahrschuelerListDTO(
		UUID fahrschuelerId,
		String vorname,
	    String nachname,
	    LocalDate geburtsdatum,
	    Adresse adress,
	    String telefonnummer,
	    Set<String> fuehrerscheine,
	    boolean bezahlt,
	    String mandant
		) {

}

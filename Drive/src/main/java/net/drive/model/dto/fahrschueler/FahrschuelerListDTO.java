package net.drive.model.dto.fahrschueler;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record FahrschuelerListDTO(
		UUID fahrschuelerId,
	    String nachname,
	    LocalDate geburtsdatum,
	    String telefonnummer,
	    Set<String> fuehrerscheine,
	    boolean bezahlt,
	    String mandant
		) {

}

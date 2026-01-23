package net.drive.model.dto.fahrschueler;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import net.drive.model.datentypen.Adresse;
import net.drive.model.entities.fahrschueler.Pruefungsstatus;

public record FahrschuelerListDTO(
		UUID fahrschuelerId,
		String vorname,
	    String nachname,
	    LocalDate geburtsdatum,
	    Adresse adress,
	    String telefonnummer,
	    Set<String> fuehrerscheine,
	    boolean bezahlt,
	    Pruefungsstatus pruefungsstatus,
	    String mandant
		) {

}

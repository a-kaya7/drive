package net.drive.model.dto.fahrschueler;

import java.time.LocalDate;


import java.util.Set;
import java.util.UUID;

import net.drive.model.datentypen.Adresse;
import net.drive.model.dto.administration.allgemein.organisation.FuehrerscheinDTO;
import net.drive.model.entities.fahrschueler.Pruefungsstatus;

public record FahrschuelerDTO(
		UUID fahrschuelerId,
	    String vorname,
	    String nachname,
	    LocalDate geburtsdatum,
	    Adresse adresse,
	    String telefonnummer,
	    String email,
	    Set<FuehrerscheinDTO> fuehrerscheine,
	    LocalDate anmeldedatum,
	    Pruefungsstatus pruefungsstatus,
	    boolean bezahlt,
	    String dokumente,
	    String hinweis,
	    String notfallkontakt,
	    String ersteller,
	    String mandant
	    
		) {

}

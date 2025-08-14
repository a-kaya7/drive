package net.drive.model.dto.administration.allgemein.organisation;

import java.util.UUID;

public record FuehrerscheinDTO(
		UUID fuehrerscheinId,
		String fuehrerscheinKlasse,
		String fahrzeuge_Ekl,
		int mindestalter,
		String voraussetzung
		) {

}

package net.drive.model.dto.administration.allgemein;

import java.util.UUID;

public record BenutzergruppeDTO(
		UUID benutzergruppeId,
		String benutzergruppe,
		String beschreibung,
		boolean freigabe,
		String mandant
		) {
}

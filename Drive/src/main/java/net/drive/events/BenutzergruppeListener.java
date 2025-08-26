package net.drive.events;

import jakarta.persistence.PreUpdate;
import net.drive.model.entities.administration.allgemein.Benutzer;

public class BenutzergruppeListener {

	@PreUpdate
	public void preUpdate(Benutzer benutzer) {
		benutzer.getMandant().getMandantId();
	}

}

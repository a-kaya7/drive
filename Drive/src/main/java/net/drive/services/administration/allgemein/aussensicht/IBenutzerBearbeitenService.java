package net.drive.services.administration.allgemein.aussensicht;

import net.drive.model.entities.administration.allgemein.Benutzer;

public interface IBenutzerBearbeitenService {

	Benutzer getBenutzerByBenutzerkennung(String benutzerkennung);
	Benutzer updateBenutzer (String benutzerkennung, Benutzer updateBenutzer);
}

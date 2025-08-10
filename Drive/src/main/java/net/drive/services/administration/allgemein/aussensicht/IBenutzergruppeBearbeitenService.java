package net.drive.services.administration.allgemein.aussensicht;

import net.drive.model.entities.administration.allgemein.Benutzergruppe;

public interface IBenutzergruppeBearbeitenService {
	Benutzergruppe updateBGruppe(String benutzergruppe, Benutzergruppe updateBG);
	Benutzergruppe getByBenutzergruppe(String benutzergruppe);

}

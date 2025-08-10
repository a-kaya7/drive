package net.drive.services.administration.allgemein.aussensicht;

import net.drive.model.dto.administration.allgemein.BenutzergruppeDTO;

public interface IBenutzergruppeNeuanlegenService {

	BenutzergruppeDTO createBenutzergruppe(BenutzergruppeDTO benutzergruppeDto);
}

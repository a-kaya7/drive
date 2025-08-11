package net.drive.services.administration.allgemein.aussensicht;

import net.drive.model.dto.administration.allgemein.BenutzerDTO;

public interface IBenutzerNeuanlegenService {

	BenutzerDTO createBenutzer(BenutzerDTO benutzerDto);

}

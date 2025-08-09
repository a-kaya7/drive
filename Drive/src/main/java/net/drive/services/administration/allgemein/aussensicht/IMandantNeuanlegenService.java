package net.drive.services.administration.allgemein.aussensicht;

import net.drive.model.dto.administration.allgemein.MandantDTO;

public interface IMandantNeuanlegenService {
	
	MandantDTO createMandant(MandantDTO mandantDto);

}

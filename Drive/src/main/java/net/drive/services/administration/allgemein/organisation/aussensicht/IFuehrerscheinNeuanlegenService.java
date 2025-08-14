package net.drive.services.administration.allgemein.organisation.aussensicht;

import net.drive.model.dto.administration.allgemein.organisation.FuehrerscheinDTO;

public interface IFuehrerscheinNeuanlegenService {
	
	FuehrerscheinDTO  createFeuhrerschein(FuehrerscheinDTO fuehrerschenDto);

}

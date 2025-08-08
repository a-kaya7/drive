package net.drive.services.administration.allgemein.aussensicht;

import net.drive.model.dto.administration.allgemein.InstitutDTO;

public interface IInstitutNeuanlegenService {

	InstitutDTO createInstitut(InstitutDTO institutDto);
}

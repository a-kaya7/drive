package net.drive.services.administration.allgemein.aussensicht;

import net.drive.model.entities.administration.allgemein.Institut;

public interface IInsitutBearbeitenService {

	Institut updateInsitut(String institutsname, Institut updateData);
	Institut getInstitutByInstitutsname(String institutsname);
	
}

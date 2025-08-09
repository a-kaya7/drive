package net.drive.services.administration.allgemein.aussensicht;

import net.drive.model.entities.administration.allgemein.Mandant;

public interface IMandantBearbeitenService {

	Mandant getMandantByIdname(String idname);

	Mandant updateMandant(String idname, Mandant updateMandant);
}

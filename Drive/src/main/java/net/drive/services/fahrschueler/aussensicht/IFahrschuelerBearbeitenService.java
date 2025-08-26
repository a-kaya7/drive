package net.drive.services.fahrschueler.aussensicht;

import java.util.UUID;

import net.drive.model.dto.fahrschueler.FahrschuelerDTO;
import net.drive.model.entities.fahrschueler.Fahrschueler;

public interface IFahrschuelerBearbeitenService {
 
	Fahrschueler getFahrschueler (UUID fahrschuelerId);
	Fahrschueler updateFahrschueler(Fahrschueler udFahrschueler, UUID fahrschuelerId); 
	
	
}

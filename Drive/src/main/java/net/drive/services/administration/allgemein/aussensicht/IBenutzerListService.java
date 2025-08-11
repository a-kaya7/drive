package net.drive.services.administration.allgemein.aussensicht;


import java.util.List;
import java.util.UUID;

import net.drive.model.dto.administration.allgemein.BenutzerDTO;
import net.drive.model.dto.administration.allgemein.BenutzerListDTO;
public interface IBenutzerListService {
	
	List<BenutzerListDTO> getAllBenutzer();


}

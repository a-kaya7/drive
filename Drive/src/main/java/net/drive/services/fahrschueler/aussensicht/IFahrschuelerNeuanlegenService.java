package net.drive.services.fahrschueler.aussensicht;

import jakarta.servlet.http.HttpServletRequest;
import net.drive.model.dto.fahrschueler.FahrschuelerDTO;

public interface IFahrschuelerNeuanlegenService {
	
	FahrschuelerDTO createFahrschueler(FahrschuelerDTO fahrschuelerDto, HttpServletRequest request);

}

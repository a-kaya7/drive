package net.drive.controller.fahrschueler;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.fahrschueler.aussensicht.IFahrschuelerListService;
import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;

/**
 * REST-Controller zur Bereitstellung einer Fahrschüler-Liste.
 * <p>
 * Dieser Controller stellt einen Endpunkt zur Verfügung, über den alle
 * Fahrschüler in einer für Listenansichten optimierten DTO-Struktur abgerufen
 * werden können.
 * </p>
 */
@RestController
@RequestMapping("/api")
public class FahrschuelerListServiceController {

	private final IFahrschuelerListService fahrschuelerService;

	public FahrschuelerListServiceController(IFahrschuelerListService fahrschuelerService) {
		this.fahrschuelerService = fahrschuelerService;
	}

	@GetMapping("/fahrschuelerlist")
	public ResponseEntity<List<FahrschuelerListDTO>> getAllFarhschueler() {
		return ResponseEntity.ok(fahrschuelerService.getAllFahrschueler());
	}

}

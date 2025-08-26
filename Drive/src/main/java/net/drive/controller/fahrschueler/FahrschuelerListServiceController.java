package net.drive.controller.fahrschueler;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.fahrschueler.aussensicht.IFahrschuelerListService;
import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;

@RestController
@RequestMapping("/api")
public class FahrschuelerListServiceController {
	
	private final IFahrschuelerListService fahrschuelerService;
	public FahrschuelerListServiceController(IFahrschuelerListService fahrschuelerService) {
		this.fahrschuelerService = fahrschuelerService;
	}
	@GetMapping("/fahrschuelerlist")
	public ResponseEntity<List<FahrschuelerListDTO>> getAllFarhschueler(){
		return ResponseEntity.ok(fahrschuelerService.getAllFahrschueler());
	}

}

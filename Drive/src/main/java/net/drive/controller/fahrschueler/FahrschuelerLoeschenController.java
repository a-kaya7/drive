package net.drive.controller.fahrschueler;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.fahrschueler.aussensicht.IFahrschuelerLoeschenService;

@RestController
@RequestMapping("/api")
public class FahrschuelerLoeschenController {
	
	private final IFahrschuelerLoeschenService fahrschuelerService;
	
	public FahrschuelerLoeschenController(IFahrschuelerLoeschenService fahrschuelerService) {
		this.fahrschuelerService = fahrschuelerService;
	}
	
	@DeleteMapping("/fahrschuelerloeschen/{fahrschuelerId}")
	public ResponseEntity<?> deleteFahrschueler(@PathVariable("fahrschuelerId") UUID fahrschuelerId){
		fahrschuelerService.deleteFahrschueler(fahrschuelerId);
		return ResponseEntity.ok().body(null);
	}

}

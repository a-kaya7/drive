package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.allgemein.aussensicht.IBenutzergruppeLoeschenService;

@RestController
@RequestMapping("/api")
public class BenutzergruppeLoeschenController {

	private final IBenutzergruppeLoeschenService bgService;
	
	public BenutzergruppeLoeschenController(IBenutzergruppeLoeschenService bgService) {
		this.bgService = bgService;
	}
	
	@DeleteMapping("/benutzergruppe/{benutzergruppe}")
	public ResponseEntity<?> deleteBGruppe(@PathVariable("benutzergruppe") String benutzergruppe){
		bgService.deleteByBenutzergruppe(benutzergruppe);
		return ResponseEntity.ok().body(null);
	}
}

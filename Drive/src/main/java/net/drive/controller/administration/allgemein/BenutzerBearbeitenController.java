package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.entities.administration.allgemein.Benutzer;
import net.drive.services.administration.allgemein.aussensicht.IBenutzerBearbeitenService;

@RestController
@RequestMapping("/api")
public class BenutzerBearbeitenController {

	private final IBenutzerBearbeitenService benutzerService;
	public BenutzerBearbeitenController(IBenutzerBearbeitenService benutzerService) {
		this.benutzerService = benutzerService;
		
	}
	
	@GetMapping("/benutzer/{benutzerkennung}")
	public ResponseEntity<Benutzer> getBenutzer(@PathVariable("benutzerkennung") String benutzerkennung){
		Benutzer ergebnis = benutzerService.getBenutzerByBenutzerkennung(benutzerkennung);
		return ResponseEntity.ok(ergebnis);
	}
	@PutMapping("/benutzerbearbeiten/{benutzerkennung}")
	public ResponseEntity<Benutzer> updateBenutzer(@PathVariable("benutzerkennung") String benutzerkennung , 
			@RequestBody Benutzer updateBenutzer){
		
		Benutzer benutzer = benutzerService.updateBenutzer(benutzerkennung, updateBenutzer);
		return ResponseEntity.ok(benutzer);
	}
}

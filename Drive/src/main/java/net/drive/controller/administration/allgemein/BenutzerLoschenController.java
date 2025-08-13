package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.allgemein.aussensicht.IBenutzerLoeschenService;

@RestController
@RequestMapping("/api")
public class BenutzerLoschenController {

	private final IBenutzerLoeschenService bLoeschenService;
	
	public BenutzerLoschenController(IBenutzerLoeschenService bLoeschenService) {
		this.bLoeschenService = bLoeschenService;
	}
	@DeleteMapping("/benutzerloeschen/{benutzerkennung}")
	public ResponseEntity<?> deleteBenutzer(@PathVariable("benutzerkennung") String benutzerkennung) {
		bLoeschenService.deleteBenutzer(benutzerkennung);
		return ResponseEntity.ok(null);
	}
}

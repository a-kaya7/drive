package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.entities.administration.allgemein.Benutzergruppe;
import net.drive.services.administration.allgemein.aussensicht.IBenutzergruppeBearbeitenService;

@RestController
@RequestMapping("/api")
public class BenutzergruppeBearbeitenController {

	private final IBenutzergruppeBearbeitenService  bgService;
	
	public BenutzergruppeBearbeitenController(IBenutzergruppeBearbeitenService bgService) {
		this.bgService = bgService;
		
	}
	
	@GetMapping("benutzergruppe/{benutzergruppe}")
	public ResponseEntity<Benutzergruppe>  getBGruppe(@PathVariable("benutzergruppe") String benutzergeruppe){
		Benutzergruppe ergebnis = bgService.getByBenutzergruppe(benutzergeruppe);
		return ResponseEntity.ok(ergebnis);
	}
	
	@PutMapping("/benutzergruppebearbeiten/{benutzergruppe}")
	public ResponseEntity<Benutzergruppe> updateBGruppe(
			@PathVariable("benutzergruppe") String benutzergruppe,
	        @RequestBody Benutzergruppe updateBG
			){
		
		Benutzergruppe bGruppe = bgService.updateBGruppe(benutzergruppe, updateBG);
		return ResponseEntity.ok(bGruppe);
	}
}

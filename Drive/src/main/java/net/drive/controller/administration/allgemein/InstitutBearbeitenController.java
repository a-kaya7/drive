package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.entities.administration.allgemein.Institut;
import net.drive.services.administration.allgemein.aussensicht.IInsitutBearbeitenService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class InstitutBearbeitenController {
	
	private final IInsitutBearbeitenService institutService;
	
	public InstitutBearbeitenController(IInsitutBearbeitenService institutService) {
		this.institutService = institutService;
	}
	
	@GetMapping("/institut/{institutsname}")
	public ResponseEntity<Institut> getInstitut(@PathVariable("institutsname") String institutsname){
		Institut ergebnis = institutService.getInstitutByInstitutsname(institutsname);
		return ResponseEntity.ok(ergebnis);
	}
	
	@PutMapping("/institutbearbeiten/{institutsname}")
	public ResponseEntity<Institut> updateInstitut(
			@PathVariable("institutsname") String institutsname, 
			@RequestBody Institut updated){
		
	Institut update = institutService.updateInsitut(institutsname, updated);
	return ResponseEntity.ok(update);
}
}

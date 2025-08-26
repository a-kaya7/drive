package net.drive.controller.fahrschueler;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.fahrschueler.aussensicht.IFahrschuelerBearbeitenService;
import net.drive.model.entities.fahrschueler.Fahrschueler;

@RestController
@RequestMapping("/api")
public class FahrschuelerBearbeitenController {
	
	private final IFahrschuelerBearbeitenService fahrschuelerService;
	
    public FahrschuelerBearbeitenController(IFahrschuelerBearbeitenService fahrschuelerService) {
    	this.fahrschuelerService = fahrschuelerService;
    }
    @GetMapping("/fahrschueler/{fahrschuelerId}")
    public ResponseEntity<Fahrschueler> getFahrschueler(@PathVariable("fahrschuelerId") UUID fahrschuelerId){
    	return ResponseEntity.ok(fahrschuelerService.getFahrschueler(fahrschuelerId));
    }
    
    @PutMapping(value = "/fahrschuelerbearbeiten/{fahrschuelerId}", consumes = "application/json")
    public ResponseEntity<Fahrschueler> updateFahrschueler(@PathVariable("fahrschuelerId") UUID fahrschuelerId,
    		@RequestBody Fahrschueler udFahrschueler){
    	
    	Fahrschueler fhschueler = fahrschuelerService.updateFahrschueler(udFahrschueler, fahrschuelerId);
    	return ResponseEntity.ok(fhschueler);
    		}
	
}

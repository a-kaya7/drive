package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.dto.administration.allgemein.MandantDTO;
import net.drive.services.administration.allgemein.aussensicht.IMandantNeuanlegenService;

@RestController
@RequestMapping("/api")
public class MandantNeuanlegenController {
	
	
	private final IMandantNeuanlegenService mandantService;
	public MandantNeuanlegenController (IMandantNeuanlegenService mandantService) {
		this.mandantService = mandantService;
	}
	
	@PostMapping("/mandantneuanlage")
	public ResponseEntity<MandantDTO> createMandant(@RequestBody MandantDTO mandant){
		return ResponseEntity.ok(mandantService.createMandant(mandant));
	}
	

}

package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.allgemein.aussensicht.IBenutzerNeuanlegenService;
import net.drive.model.dto.administration.allgemein.BenutzerDTO;

@RestController
@RequestMapping("/api")
public class BenutzerNeuanlegenController {

	private final IBenutzerNeuanlegenService benutzerService;
	public BenutzerNeuanlegenController(IBenutzerNeuanlegenService benutzerService) {
		this.benutzerService =  benutzerService;
	}
	
	@PostMapping("/benutzerneuanlage")
	public ResponseEntity<BenutzerDTO> createBenutzer(@RequestBody BenutzerDTO benutzerDto){
		return ResponseEntity.ok(benutzerService.createBenutzer(benutzerDto));
	}
}

package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.dto.administration.allgemein.BenutzergruppeDTO;
import net.drive.services.administration.allgemein.aussensicht.IBenutzergruppeNeuanlegenService;

@RestController
@RequestMapping("/api")
public class BenutzergruppeNeuanlegenController {

	private final IBenutzergruppeNeuanlegenService bgService;
	
	public BenutzergruppeNeuanlegenController(IBenutzergruppeNeuanlegenService bgService) {
		this.bgService = bgService;
	}
	
	@PostMapping("/benutzergruppeneuanlage")
	public ResponseEntity<BenutzergruppeDTO> createBGruppe(@RequestBody BenutzergruppeDTO  bgDto){
		return ResponseEntity.ok(bgService.createBenutzergruppe(bgDto));
	}
}

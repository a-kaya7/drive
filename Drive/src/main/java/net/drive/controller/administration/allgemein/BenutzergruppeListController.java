package net.drive.controller.administration.allgemein;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.allgemein.aussensicht.IBenutzergruppeListService;
import net.drive.model.dto.administration.allgemein.BenutzergruppeDTO;

@RestController
@RequestMapping("/api")
public class BenutzergruppeListController {

	private final IBenutzergruppeListService  bgService;
	
	public BenutzergruppeListController(IBenutzergruppeListService  bgService) {
		this.bgService = bgService;
		
	}
	
	@GetMapping("/benutzergruppen")
	public ResponseEntity<List<BenutzergruppeDTO>> getAllBenutzergruppe(){
		return ResponseEntity.ok(bgService.getAllBenutzergruppe());
	}
}

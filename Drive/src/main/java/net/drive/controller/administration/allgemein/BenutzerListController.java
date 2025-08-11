package net.drive.controller.administration.allgemein;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.allgemein.aussensicht.IBenutzerListService;
import net.drive.model.dto.administration.allgemein.*;

@RestController
@RequestMapping("/api")
public class BenutzerListController {

	private final IBenutzerListService benutzerService;
	
	public BenutzerListController(IBenutzerListService benutzerService) {
		this.benutzerService = benutzerService;
		
	}
	
	@GetMapping("/benutzerlist")
	public ResponseEntity<List<BenutzerListDTO>>  getAllBenutzer(){
		
		return ResponseEntity.ok(benutzerService.getAllBenutzer());
	}
}

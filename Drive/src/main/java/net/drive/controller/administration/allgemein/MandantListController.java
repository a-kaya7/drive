package net.drive.controller.administration.allgemein;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.services.administration.allgemein.aussensicht.IMandantListService;
import net.drive.model.dto.administration.allgemein.MandantListDTO;

@RestController
@RequestMapping("/api")
public class MandantListController {

	private final IMandantListService mandantService;
	public MandantListController(IMandantListService mandantService) {
		this.mandantService = mandantService;
		
		}
	@GetMapping("/mandantenlist")
	public ResponseEntity<List<MandantListDTO>>  getAllMandanten(){
		return ResponseEntity.ok(mandantService.getAllMandant());
		
	}
}

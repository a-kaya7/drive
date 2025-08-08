package net.drive.controller.administration.allgemein;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.drive.model.dto.administration.allgemein.InstitutDTO;
import net.drive.services.administration.allgemein.aussensicht.IInstitutNeuanlegenService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class InstitutNeuanlegenController {

	
	private final IInstitutNeuanlegenService institutservice;
	
	public InstitutNeuanlegenController(IInstitutNeuanlegenService institutservice) {
		this.institutservice = institutservice;
	}
	
	@PostMapping("/institutneuanlage")
	public ResponseEntity<InstitutDTO> institutNeuanlegen(@RequestBody InstitutDTO institutDto ){
		
		return ResponseEntity.ok(institutservice.createInstitut(institutDto));
	}
}

